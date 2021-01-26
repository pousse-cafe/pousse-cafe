package poussecafe.source.analysis;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.source.SourceFile;

import static java.util.Objects.requireNonNull;

public class TypeResolvingCompilationUnitVisitor {

    /**
     * @param sourceFile The source file to visit
     * @return True if at least one visitor found content of interest
     */
    public boolean visit(SourceFile sourceFile) {
        currentSourceFile = sourceFile;
        resolver = new CompilationUnitResolver.Builder()
                .compilationUnit(sourceFile.tree())
                .classResolver(classResolver)
                .build();
        sourceFile.tree().accept(astVisitor);
        for(ResolvedCompilationUnitVisitor visitor : visitors) {
            if(visitor.foundContent()) {
                return true;
            }
        }
        return false;
    }

    private SourceFile currentSourceFile;

    private CompilationUnitResolver resolver;

    private ClassResolver classResolver;

    private ASTVisitor astVisitor = new ASTVisitor() {

        @Override
        public boolean visit(CompilationUnit node) {
            var unit = new ResolvedCompilationUnit.Builder()
                    .withResolver(resolver)
                    .withSourceFile(currentSourceFile)
                    .build();
            for(ResolvedCompilationUnitVisitor visitor : visitors) {
                try {
                    visitor.visit(unit);
                } catch (Exception e) {
                    errors.add(e);
                }
            }
            return true;
        }

        @Override
        public boolean visit(ImportDeclaration node) {
            resolver.tryRegister(node);
            return false;
        }

        @Override
        public boolean visit(TypeDeclaration node) {
            ++typeLevel;
            pushResolver(node);
            var resolvedTypeDeclaration = resolve(node);
            pushResolvedTypeDeclaration(resolvedTypeDeclaration);
            return visitTypeDeclarationOrSkip();
        }

        @Override
        public void endVisit(TypeDeclaration node) {
            var resolvedTypeDeclaration = resolvedTypeDeclarations.pop();
            for(ResolvedCompilationUnitVisitor visitor : visitors) {
                try {
                    visitor.endVisit(resolvedTypeDeclaration);
                } catch (Exception e) {
                    errors.add(e);
                }
            }
            typeDeclarationResolvers.pop();
            --typeLevel;
        }

        @Override
        public boolean visit(EnumDeclaration node) {
            return false;
        }

        @Override
        public boolean visit(MethodDeclaration node) {
            var method = currentResolver().resolve(node);
            for(ResolvedCompilationUnitVisitor visitor : visitors) {
                try {
                    visitor.visit(method);
                } catch (Exception e) {
                    errors.add(e);
                }
            }
            return false;
        }
    };

    private int typeLevel = -1;

    public int typeLevel() {
        return typeLevel;
    }

    private void pushResolver(TypeDeclaration node) {
        TypeDeclarationResolver typeDeclarationResolver;
        if(typeDeclarationResolvers.isEmpty()) {
            typeDeclarationResolver = new TypeDeclarationResolver.Builder()
                    .parent(resolver)
                    .typeDeclaration(node)
                    .containerClass(getRootClass(node))
                    .build();
        } else {
            typeDeclarationResolver = new TypeDeclarationResolver.Builder()
                    .parent(resolver)
                    .typeDeclaration(node)
                    .containerClass(getInnerClass(typeDeclarationResolvers.peek().containerClass(), node))
                    .build();
        }
        typeDeclarationResolvers.push(typeDeclarationResolver);
    }

    private Deque<TypeDeclarationResolver> typeDeclarationResolvers = new ArrayDeque<>();

    private TypeDeclarationResolver currentResolver() {
        return typeDeclarationResolvers.peek();
    }

    private ResolvedClass getRootClass(TypeDeclaration typeDeclaration) {
        var className = resolver.compilationUnit().getPackage().getName().getFullyQualifiedName()
                + "."
                + typeDeclaration.getName().getFullyQualifiedName();
        try {
            return resolver.classResolver().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ResolutionException("Unable to resolve root class " + className);
        }
    }

    private ResolvedClass getInnerClass(ResolvedClass containerClass, TypeDeclaration node) {
        var innerClassName = node.getName().getFullyQualifiedName();
        return getDeclaredClass(containerClass, innerClassName);
    }

    private ResolvedClass getDeclaredClass(ResolvedClass containerClass, String innerClassName) {
        return containerClass.innerClasses().stream()
                .filter(innerClass -> innerClass.name().simple().equals(innerClassName))
                .findFirst().orElseThrow();
    }

    private void pushResolvedTypeDeclaration(ResolvedTypeDeclaration resolvedTypeDeclaration) {
        resolvedTypeDeclarations.push(resolvedTypeDeclaration);
    }

    private Deque<ResolvedTypeDeclaration> resolvedTypeDeclarations = new ArrayDeque<>();

    private boolean visitTypeDeclarationOrSkip() {
        var resolvedTypeDeclaration = resolvedTypeDeclarations.peek();
        boolean mustVisitChildren = false;
        for(ResolvedCompilationUnitVisitor visitor : visitors) {
            try {
                if(visitor.visit(resolvedTypeDeclaration)) {
                    mustVisitChildren = true;
                }
            } catch (Exception e) {
                errors.add(e);
            }
        }
        return mustVisitChildren;
    }

    private List<ResolvedCompilationUnitVisitor> visitors = new ArrayList<>();

    private List<Exception> errors = new ArrayList<>();

    public ResolvedTypeDeclaration resolve(TypeDeclaration node) {
        return new ResolvedTypeDeclaration.Builder()
                .withResolver(currentResolver())
                .withDeclaration(node)
                .build();
    }

    public static class Builder {

        public TypeResolvingCompilationUnitVisitor build() {
            requireNonNull(scanner.classResolver);
            return scanner;
        }

        private TypeResolvingCompilationUnitVisitor scanner = new TypeResolvingCompilationUnitVisitor();

        public Builder withClassResolver(ClassResolver classResolver) {
            scanner.classResolver = classResolver;
            return this;
        }

        public Builder withVisitor(ResolvedCompilationUnitVisitor visitor) {
            scanner.visitors.add(visitor);
            return this;
        }
    }

    private TypeResolvingCompilationUnitVisitor() {

    }
}
