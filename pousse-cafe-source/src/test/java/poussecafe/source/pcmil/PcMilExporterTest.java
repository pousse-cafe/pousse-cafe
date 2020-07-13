package poussecafe.source.pcmil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;
import poussecafe.source.DiscoveryTest;
import poussecafe.source.Scanner;
import poussecafe.source.model.Model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PcMilExporterTest {

    @Test
    public void toPcMil() throws IOException, URISyntaxException {
        givenModel();
        whenExportingPcMil();
        thenPcMilIsExpected();
    }

    private void givenModel() throws IOException {
        var scanner = new Scanner.Builder().build();
        scanner.includeTree(DiscoveryTest.testModelDirectory);
        model = scanner.model();
    }

    private Model model;

    private void whenExportingPcMil() {
        exported = new PcMilExporter.Builder()
                .model(model)
                .processName("Process1")
                .build()
                .toPcMil();
    }

    private String exported;

    private void thenPcMilIsExpected() throws IOException, URISyntaxException {
        assertThat(exported, equalTo(readResource("Process1.pcm")));
    }

    private String readResource(String string) throws IOException, URISyntaxException {
        return new String(Files.readString(Path.of(this.getClass().getResource("/" + string).toURI())));
    }
}
