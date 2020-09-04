package poussecafe.source.generation.tools;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CompilationUnitEditorTest {

    @Test
    public void noEditOnlyOrdersImports() {
        givenExistingCodeWithOrderedImports();
        whenEditingWithoutChange();
        thenContentUnchanged();
    }

    private void givenExistingCodeWithOrderedImports() {
        try {
            sourceFile = File.createTempFile(getClass().getSimpleName(), ".java");
            sourceFile.deleteOnExit();
            referenceFile = new File("src/test/java/poussecafe/source/generation/existingcode/myaggregate/adapters/MyAggregateAttributes.java");
            Files.copy(referenceFile, sourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File sourceFile;

    private File referenceFile;

    private void whenEditingWithoutChange() {
        var editor = new CompilationUnitEditor.Builder()
                .packageName("poussecafe.source.generation.existingcode.myaggregate.adapters")
                .fileDirectory(sourceFile.getParentFile().toPath())
                .fileName(sourceFile.getName())
                .build();
        editor.flush();
    }

    private void thenContentUnchanged() {
        try {
            assertTrue(Arrays.equals(Files.toByteArray(referenceFile), Files.toByteArray(sourceFile)));
        } catch (IOException e) {
            assertTrue(false);
        }
    }
}
