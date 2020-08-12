package poussecafe.source.generation;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import poussecafe.discovery.DefaultModule;
import poussecafe.domain.AggregateRoot;
import poussecafe.domain.EntityAttributes;
import poussecafe.source.model.Aggregate;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class AggregateRootEditor {

    public void edit() {
        compilationUnitEditor.setPackage(aggregate.packageName());

        compilationUnitEditor.addImportLast(poussecafe.discovery.Aggregate.class.getCanonicalName());
        compilationUnitEditor.addImportLast(poussecafe.discovery.DefaultModule.class.getCanonicalName());
        compilationUnitEditor.addImportLast(AggregateRoot.class.getCanonicalName());
        compilationUnitEditor.addImportLast(poussecafe.domain.EntityAttributes.class.getCanonicalName());

        ParameterizedType aggregateRootSupertype = aggregateRootSupertype();

        var aggregateAnnotation = aggregateAnnotation();

        var attributesType = attributesType();

        var aggregateRootTypeDeclaration = new TypeDeclarationBuilder(compilationUnitEditor.ast())
            .addModifier(aggregateAnnotation)
            .addModifier(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD))
            .setName(aggregate.name())
            .setSuperclass(aggregateRootSupertype)
            .addDeclaredType(attributesType)
            .build();
        compilationUnitEditor.setDeclaredType(aggregateRootTypeDeclaration);

        compilationUnitEditor.flush();
    }

    private Aggregate aggregate;

    private NormalAnnotation aggregateAnnotation() {
        var aggregateAnnotation = ast.newNormalAnnotation();
        aggregateAnnotation.setTypeName(ast.newSimpleName(Aggregate.class.getSimpleName()));

        var factoryClassName = AggregateCodeGenerationConventions.aggregateFactoryTypeName(aggregate);
        var factoryValuePair = typeLiteralAttribute("factory", ast.newSimpleName(factoryClassName.getIdentifier().toString()));
        aggregateAnnotation.values().add(factoryValuePair);

        var repositoryClassName = AggregateCodeGenerationConventions.aggregateRepositoryTypeName(aggregate);
        var repositoryValuePair = typeLiteralAttribute("repository", ast.newSimpleName(repositoryClassName.getIdentifier().toString()));
        aggregateAnnotation.values().add(repositoryValuePair);

        var moduleValuePair = typeLiteralAttribute("module", ast.newSimpleName(DefaultModule.class.getSimpleName()));
        aggregateAnnotation.values().add(moduleValuePair);

        return aggregateAnnotation;
    }

    private MemberValuePair typeLiteralAttribute(String name, Name typeName) {
        var factoryValuePair = ast.newMemberValuePair();
        factoryValuePair.setName(ast.newSimpleName(name));
        var factoryTypeLiteral = ast.newTypeLiteral();
        var factoryClass = ast.newSimpleType(typeName);
        factoryTypeLiteral.setType(factoryClass);
        factoryValuePair.setValue(factoryTypeLiteral);
        return factoryValuePair;
    }

    private ParameterizedType aggregateRootSupertype() {
        Type superclassType = ast.newSimpleType(ast.newSimpleName(AggregateRoot.class.getSimpleName()));
        ParameterizedType parametrizedRootType = ast.newParameterizedType(superclassType);

        parametrizedRootType.typeArguments().add(aggregateIdentifierType());

        var aggregateRootTypeName = ast.newSimpleName(aggregate.name());
        var attributesTypeName = ast.newSimpleName(AggregateCodeGenerationConventions.ATTRIBUTES_CLASS_NAME);
        var attributesType = ast.newSimpleType(ast.newQualifiedName(aggregateRootTypeName,
                attributesTypeName));
        parametrizedRootType.typeArguments().add(attributesType);
        return parametrizedRootType;
    }

    private SimpleType aggregateIdentifierType() {
        return ast.newSimpleType(ast.newSimpleName(AggregateCodeGenerationConventions.aggregateIdentifierTypeName(aggregate).getIdentifier().toString()));
    }

    private TypeDeclaration attributesType() {
        return new TypeDeclarationBuilder(compilationUnitEditor.ast())
                .addModifier(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD))
                .addModifier(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD))
                .setInterface(true)
                .setName(AggregateCodeGenerationConventions.ATTRIBUTES_CLASS_NAME)
                .addSuperinterface(entityAttributesType())
                .build();
    }

    private ParameterizedType entityAttributesType() {
        Type simpleType = ast.newSimpleType(ast.newSimpleName(EntityAttributes.class.getSimpleName()));
        ParameterizedType parametrizedType = ast.newParameterizedType(simpleType);
        parametrizedType.typeArguments().add(aggregateIdentifierType());
        return parametrizedType;
    }

    public static class Builder {

        private AggregateRootEditor editor = new AggregateRootEditor();

        public AggregateRootEditor build() {
            requireNonNull(editor.compilationUnitEditor);
            requireNonNull(editor.aggregate);

            editor.ast = editor.compilationUnitEditor.ast();

            return editor;
        }

        public Builder compilationUnitEditor(ComilationUnitEditor compilationUnitEditor) {
            editor.compilationUnitEditor = compilationUnitEditor;
            return this;
        }

        public Builder aggregate(Aggregate aggregate) {
            editor.aggregate = aggregate;
            return this;
        }
    }

    private AggregateRootEditor() {

    }

    private ComilationUnitEditor compilationUnitEditor;

    private AST ast;
}
