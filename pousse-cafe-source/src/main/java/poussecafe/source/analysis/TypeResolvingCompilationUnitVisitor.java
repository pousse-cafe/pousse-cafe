package poussecafe.source.analysis;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import static java.util.Objects.requireNonNull;

public abstract class TypeResolvingCompilationUnitVisitor extends ASTVisitor {

    @Override
    public boolean visit(ImportDeclaration node) {
        resolver.tryRegister(node);
        return false;
    }

    private CompilationUnitResolver resolver;

    private CompilationUnit compilationUnit;

    public CompilationUnit compilationUnit() {
        return compilationUnit;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        ++typeLevel;
        pushResolver(node);
        return visitTypeDeclarationOrSkip(node);
    }

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

    public TypeDeclarationResolver currentResolver() {
        return typeDeclarationResolvers.peek();
    }

    private Class<?> getRootClass(TypeDeclaration typeDeclaration) {
        var className = compilationUnit.getPackage().getName().getFullyQualifiedName()
                + "."
                + typeDeclaration.getName().getFullyQualifiedName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ResolutionException("Unable to resolve root class " + className);
        }
    }

    private Class<?> getInnerClass(Class<?> containerClass, TypeDeclaration node) {
        var innerClassName = node.getName().getFullyQualifiedName();
        return getDeclaredClass(containerClass, innerClassName);
    }

    private Class<?> getDeclaredClass(Class<?> containerClass, String innerClassName) {
        return Arrays.stream(containerClass.getDeclaredClasses())
                .filter(innerClass -> innerClass.getSimpleName().equals(innerClassName))
                .findFirst().orElseThrow();
    }

    protected abstract boolean visitTypeDeclarationOrSkip(TypeDeclaration node);

    public ResolvedTypeDeclaration resolve(TypeDeclaration node) {
        return new ResolvedTypeDeclaration.Builder()
                .withResolver(currentResolver())
                .withDeclaration(node)
                .build();
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        endTypeDeclarationVisit(node);
        typeDeclarationResolvers.pop();
        --typeLevel;
    }

    protected void endTypeDeclarationVisit(TypeDeclaration node) {

    }

    public int lineNumber(ASTNode node) {
        return compilationUnit.getLineNumber(node.getStartPosition());
    }

    protected TypeResolvingCompilationUnitVisitor(CompilationUnit compilationUnit) {
        requireNonNull(compilationUnit);
        this.compilationUnit = compilationUnit;
        resolver = new CompilationUnitResolver(compilationUnit);
    }
}
