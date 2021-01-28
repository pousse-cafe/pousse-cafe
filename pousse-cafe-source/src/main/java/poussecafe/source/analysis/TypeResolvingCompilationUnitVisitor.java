package poussecafe.source.analysis;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
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
            return visitTypeDeclarationOrSkip();
        }

        private void pushResolver(TypeDeclaration node) {
            var typeResolverBuilder = new TypeDeclarationResolver.Builder()
                    .typeDeclaration(node);
            if(typeDeclarationResolvers.isEmpty()) {
                var packageName = resolver.compilationUnit().getPackage().getName().getFullyQualifiedName();
                var typeName = node.getName().getIdentifier();
                typeResolverBuilder
                        .parent(resolver)
                        .parentTypeDeclaration(Optional.empty())
                        .safeClassName(SafeClassName.ofRootClass(new Name(packageName + "." + typeName)));
            } else {
                var declaringTypeResolver = typeDeclarationResolvers.peek();
                var declaringType = declaringTypeResolver.resolvedTypeDeclaration();
                typeResolverBuilder
                        .parent(declaringTypeResolver)
                        .parentTypeDeclaration(Optional.of(declaringType))
                        .safeClassName(declaringType.unresolvedName().withLastSegment(node.getName().toString()));
            }
            typeDeclarationResolvers.push(typeResolverBuilder.build());
        }

        private Deque<TypeDeclarationResolver> typeDeclarationResolvers = new ArrayDeque<>();

        private TypeDeclarationResolver currentResolver() {
            return typeDeclarationResolvers.peek();
        }

        private boolean visitTypeDeclarationOrSkip() {
            var resolvedTypeDeclaration = typeDeclarationResolvers.peek().resolvedTypeDeclaration();
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

        @Override
        public void endVisit(TypeDeclaration node) {
            var resolvedTypeDeclaration = typeDeclarationResolvers.pop().resolvedTypeDeclaration();
            for(ResolvedCompilationUnitVisitor visitor : visitors) {
                try {
                    visitor.endVisit(resolvedTypeDeclaration);
                } catch (Exception e) {
                    errors.add(e);
                }
            }
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

    private List<ResolvedCompilationUnitVisitor> visitors = new ArrayList<>();

    private int typeLevel = -1;

    public int typeLevel() {
        return typeLevel;
    }

    private List<Exception> errors = new ArrayList<>();

    public void forget(String sourceId) {
        visitors.forEach(visitor -> visitor.forget(sourceId));
    }

    public static class Builder {

        public TypeResolvingCompilationUnitVisitor build() {
            requireNonNull(compilationUnitVisitor.classResolver);
            return compilationUnitVisitor;
        }

        private TypeResolvingCompilationUnitVisitor compilationUnitVisitor = new TypeResolvingCompilationUnitVisitor();

        public Builder withClassResolver(ClassResolver classResolver) {
            compilationUnitVisitor.classResolver = classResolver;
            return this;
        }

        public Builder withVisitor(ResolvedCompilationUnitVisitor visitor) {
            compilationUnitVisitor.visitors.add(visitor);
            return this;
        }
    }

    private TypeResolvingCompilationUnitVisitor() {

    }
}
