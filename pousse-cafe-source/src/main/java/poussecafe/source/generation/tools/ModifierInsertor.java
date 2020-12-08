package poussecafe.source.generation.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("rawtypes")
public class ModifierInsertor {

    public void set(ModifierKeyword keyword, boolean enabled) {
        List<Modifier> modifiersToReplace = modifiersToReplace();
        if(enabled && modifiersToReplace.isEmpty()) {
            insertNewModifier(keyword);
        } else if(!enabled && !modifiersToReplace.isEmpty()) {
            removeModifiers(modifiersToReplace);
        }
    }

    public List<Modifier> modifiersToReplace() {
        var modifiersToReplace = new ArrayList<Modifier>();
        for(Object modifierObject : modifiers) {
            if(modifierObject instanceof Modifier) {
                Modifier modifier = (Modifier) modifierObject;
                if(toReplace.contains(modifier.getKeyword())) {
                    modifiersToReplace.add(modifier);
                }
            }
        }
        return modifiersToReplace;
    }

    private List modifiers;

    private Set<ModifierKeyword> toReplace;

    private void insertNewModifier(ModifierKeyword keyword) {
        Optional<ASTNode> pivot = pivotProvider.get();
        var newModifier = rewrite.ast().newModifier(keyword);
        if(pivot.isPresent()) {
            var pivotInsertionMode = pivotInsertionModeProvider.apply(pivot.get());
            if(pivotInsertionMode == InsertionMode.AFTER) {
                listRewrite.insertAfter(newModifier, pivot.get(), null);
            } else if(pivotInsertionMode == InsertionMode.BEFORE) {
                listRewrite.insertBefore(newModifier, pivot.get(), null);
            }
        } else {
            if(defaultInsertionMode == DefaultInsertionMode.FIRST) {
                listRewrite.insertFirst(newModifier, null);
            } else if(defaultInsertionMode == DefaultInsertionMode.LAST) {
                listRewrite.insertLast(newModifier, null);
            }
        }
    }

    private Supplier<Optional<ASTNode>> pivotProvider;

    private NodeRewrite rewrite;

    private Function<ASTNode, InsertionMode> pivotInsertionModeProvider;

    private ListRewrite listRewrite;

    private DefaultInsertionMode defaultInsertionMode;

    private void removeModifiers(List<Modifier> modifiersToReplace) {
        for(Modifier modifier : modifiersToReplace) {
            listRewrite.remove(modifier, null);
        }
    }

    public static class Builder {

        public ModifierInsertor insertor = new ModifierInsertor();

        public ModifierInsertor build() {
            requireNonNull(insertor.modifiers);
            requireNonNull(insertor.toReplace);
            requireNonNull(insertor.pivotProvider);
            requireNonNull(insertor.rewrite);
            requireNonNull(insertor.pivotInsertionModeProvider);
            requireNonNull(insertor.listRewrite);
            requireNonNull(insertor.defaultInsertionMode);
            return insertor;
        }

        public Builder modifiers(List modifiers) {
            insertor.modifiers = modifiers;
            return this;
        }

        public Builder toReplace(Set<ModifierKeyword> toReplace) {
            insertor.toReplace = toReplace;
            return this;
        }

        public Builder pivotProvider(Supplier<Optional<ASTNode>> pivotProvider) {
            insertor.pivotProvider = pivotProvider;
            return this;
        }

        public Builder rewrite(NodeRewrite rewrite) {
            insertor.rewrite = rewrite;
            return this;
        }

        public Builder pivotInsertionModeProvider(Function<ASTNode, InsertionMode> pivotInsertionModeProvider) {
            insertor.pivotInsertionModeProvider = pivotInsertionModeProvider;
            return this;
        }

        public Builder listRewrite(ListRewrite listRewrite) {
            insertor.listRewrite = listRewrite;
            return this;
        }

        public Builder defaultInsertionMode(DefaultInsertionMode defaultInsertionMode) {
            insertor.defaultInsertionMode = defaultInsertionMode;
            return this;
        }
     }

    private ModifierInsertor() {

    }
}
