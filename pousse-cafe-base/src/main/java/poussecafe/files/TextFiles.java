package poussecafe.files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class TextFiles {

    public static String readContent(File file) throws IOException {
            return streamLines(file).collect(joining("\n"));
    }

    public static Stream<String> streamLines(File file) throws IOException {
        return Files.lines(file.toPath(), StandardCharsets.UTF_8);
    }

    private TextFiles() {

    }
}
