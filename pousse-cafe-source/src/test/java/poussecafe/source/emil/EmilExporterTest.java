package poussecafe.source.emil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.Test;
import poussecafe.source.DiscoveryTest;
import poussecafe.source.SourceModelBuilder;
import poussecafe.source.analysis.ClassLoaderClassResolver;
import poussecafe.source.model.Model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmilExporterTest {

    @Test
    public void exportSingleProcess() throws IOException, URISyntaxException {
        givenModel();
        whenExporting(Optional.of("Process1"));
        thenEmilIsExpected("Process1.emil");
    }

    private void givenModel() throws IOException {
        var builder = new SourceModelBuilder(new ClassLoaderClassResolver());
        builder.includeTree(DiscoveryTest.testModelDirectory);
        model = builder.build();
    }

    private Model model;

    private void whenExporting(Optional<String> process) {
        exported = new EmilExporter.Builder()
                .model(model)
                .processName(process)
                .build()
                .toEmil();
    }

    private String exported;

    private void thenEmilIsExpected(String resourceName) throws IOException, URISyntaxException {
        assertThat(exported, equalTo(readResource(resourceName)));
    }

    private String readResource(String string) throws IOException, URISyntaxException {
        return new String(Files.readString(Path.of(this.getClass().getResource("/" + string).toURI())));
    }

    @Test
    public void exportAll() throws IOException, URISyntaxException {
        givenModel();
        whenExporting(Optional.empty());
        thenEmilIsExpected("all.emil");
    }
}
