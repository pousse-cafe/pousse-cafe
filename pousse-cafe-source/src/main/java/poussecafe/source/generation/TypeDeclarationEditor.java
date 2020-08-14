package poussecafe.source.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import poussecafe.source.analysis.Name;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class TypeDeclarationEditor {

    public TypeDeclarationEditor setName(String name) {
        rewrite.set(TypeDeclaration.NAME_PROPERTY, rewrite.ast().newSimpleName(name));
        return this;
    }

    public TypeDeclarationEditor setName(Name name) {
        setName(name.getIdentifier().toString());
        return this;
    }

    public TypeDeclarationEditor setSuperclass(Type superclassType) {
        rewrite.set(TypeDeclaration.SUPERCLASS_TYPE_PROPERTY, superclassType);
        return this;
    }

    public InnerTypeDeclarationEditor declaredType(String name) {
        var existingTypeDeclaration = findTypeDeclarationByName(name);
        if(existingTypeDeclaration.isPresent()) {
            return new InnerTypeDeclarationEditor(new NodeRewrite(rewrite.rewrite(), existingTypeDeclaration.get()), this);
        } else {
            var newTypeDeclaration = rewrite.ast().newTypeDeclaration();
            newTypeDeclaration.setName(rewrite.ast().newSimpleName(name));
            rewrite.listRewrite(TypeDeclaration.BODY_DECLARATIONS_PROPERTY).insertLast(newTypeDeclaration, null);
            return new InnerTypeDeclarationEditor(new NodeRewrite(rewrite.rewrite(), newTypeDeclaration), this);
        }
    }

    private Optional<TypeDeclaration> findTypeDeclarationByName(String name) {
        for(Object declaration : typeDeclaration.bodyDeclarations()) {
            if(declaration instanceof TypeDeclaration) {
                TypeDeclaration typeDeclaration = (TypeDeclaration) declaration;
                if(typeDeclaration.getName().getIdentifier().equals(name)) {
                    return Optional.of(typeDeclaration);
                }
            }
        }
        return Optional.empty();
    }

    public TypeDeclarationEditor setInterface(boolean isInterface) {
        rewrite.set(TypeDeclaration.INTERFACE_PROPERTY, isInterface);
        return this;
    }

    public TypeDeclarationEditor addSuperinterface(Type superinterfaceType) {
        if(!alreadyImplements(superinterfaceType)) {
            rewrite.listRewrite(TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY).insertLast(superinterfaceType, null);
        }
        return this;
    }

    private boolean alreadyImplements(Type superinterfaceType) {
        return containsNode(typeDeclaration.superInterfaceTypes(), superinterfaceType);
    }

    public TypeDeclarationEditor addSuperinterfaceFirst(SimpleType superinterfaceType) {
        if(!alreadyImplements(superinterfaceType)) {
            rewrite.listRewrite(TypeDeclaration.SUPER_INTERFACE_TYPES_PROPERTY).insertFirst(superinterfaceType, null);
        }
        return this;

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean containsNode(List list, Object node) {
        return list.stream().anyMatch(listNode -> listNode.toString().equals(node.toString()));
    }

    private TypeDeclaration typeDeclaration;

    public TypeDeclarationEditor setTypeParameter(int index, TypeParameter parameter) {
        var listRewrite = rewrite.listRewrite(TypeDeclaration.TYPE_PARAMETERS_PROPERTY);
        while(listRewrite.getRewrittenList().size() <= index) {
            var typeParameter = rewrite.ast().newTypeParameter();
            listRewrite.insertLast(typeParameter, null);
        }
        listRewrite.replace((ASTNode) listRewrite.getRewrittenList().get(index), parameter, null);
        return this;
    }

    public TypeDeclarationEditor addField(FieldDeclaration field) {
        rewrite.listRewrite(TypeDeclaration.BODY_DECLARATIONS_PROPERTY).insertLast(field, null);
        return this;
    }

    public ModifiersEditor modifiers() {
        return new ModifiersEditor(rewrite, TypeDeclaration.MODIFIERS2_PROPERTY);
    }

    public List<MethodDeclarationEditor> method(String methodName) {
        var methods = findMethods(methodName);
        var newMethod = createMethodIfAbsent(methodName, methods);
        return methods.stream()
                .map(method -> new MethodDeclarationEditor(new NodeRewrite(rewrite.rewrite(), method), method == newMethod))
                .collect(toList());
    }

    private MethodDeclaration createMethodIfAbsent(String methodName, List<MethodDeclaration> methods) {
        MethodDeclaration newMethod = null;
        if(methods.isEmpty()) {
            newMethod = rewrite.ast().newMethodDeclaration();
            newMethod.setName(rewrite.ast().newSimpleName(methodName));
            rewrite.listRewrite(TypeDeclaration.BODY_DECLARATIONS_PROPERTY).insertLast(newMethod, null);
            methods.add(newMethod);
        }
        return newMethod;
    }

    private List<MethodDeclaration> findMethods(String methodName) {
        var methods = new ArrayList<MethodDeclaration>();
        for(Object declaration : typeDeclaration.bodyDeclarations()) {
            if(declaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
                if(methodDeclaration.getName().getIdentifier().equals(methodName)) {
                    methods.add(methodDeclaration);
                }
            }
        }
        return methods;
    }

    public List<FieldDeclarationEditor> field(String fieldName) {
        var fields = findFields(fieldName);
        if(fields.isEmpty()) {
            var fragment = rewrite.ast().newVariableDeclarationFragment();
            fragment.setName(rewrite.ast().newSimpleName(fieldName));

            var method = rewrite.ast().newFieldDeclaration(fragment);
            rewrite.listRewrite(TypeDeclaration.BODY_DECLARATIONS_PROPERTY).insertLast(method, null);
            fields.add(method);
        }

        return fields.stream()
                .map(method -> new FieldDeclarationEditor(new NodeRewrite(rewrite.rewrite(), method)))
                .collect(toList());
    }

    private List<FieldDeclaration> findFields(String methodName) {
        var methods = new ArrayList<FieldDeclaration>();
        for(Object declaration : typeDeclaration.bodyDeclarations()) {
            if(declaration instanceof FieldDeclaration) {
                FieldDeclaration methodDeclaration = (FieldDeclaration) declaration;
                if(isSingleField(methodDeclaration, methodName)) {
                    methods.add(methodDeclaration);
                }
            }
        }
        return methods;
    }

    private boolean isSingleField(FieldDeclaration fieldDeclaration, String methodName) {
        if(fieldDeclaration.fragments().size() == 1) {
            VariableDeclarationFragment fragment = (VariableDeclarationFragment) fieldDeclaration.fragments().get(0);
            return fragment.getName().getIdentifier().equals(methodName);
        } else {
            return false;
        }
    }

    public List<MethodDeclarationEditor> constructors(String typeName) {
        var methods = findConstructors();
        var newConstructor = createConstructorIfAbsent(typeName, methods);
        return methods.stream()
                .map(method -> new MethodDeclarationEditor(new NodeRewrite(rewrite.rewrite(), method), method == newConstructor))
                .collect(toList());
    }

    private MethodDeclaration createConstructorIfAbsent(String typeName, List<MethodDeclaration> methods) {
        MethodDeclaration newConstructor = null;
        if(methods.isEmpty()) {
            newConstructor = rewrite.ast().newMethodDeclaration();
            newConstructor.setConstructor(true);
            newConstructor.setName(rewrite.ast().newSimpleName(typeName));
            rewrite.listRewrite(TypeDeclaration.BODY_DECLARATIONS_PROPERTY).insertLast(newConstructor, null);
            methods.add(newConstructor);
        }
        return newConstructor;
    }

    private List<MethodDeclaration> findConstructors() {
        var methods = new ArrayList<MethodDeclaration>();
        for(Object declaration : typeDeclaration.bodyDeclarations()) {
            if(declaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
                if(methodDeclaration.isConstructor()) {
                    methods.add(methodDeclaration);
                }
            }
        }
        return methods;
    }

    public TypeDeclarationEditor(NodeRewrite rewrite) {
        requireNonNull(rewrite);
        this.rewrite = rewrite;

        typeDeclaration = (TypeDeclaration) rewrite.node();
    }

    private NodeRewrite rewrite;
}
