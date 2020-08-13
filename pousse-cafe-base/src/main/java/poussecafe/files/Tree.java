package poussecafe.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class Tree {

    public static List<Difference> compareTrees(Path targetTree, Path expectedTree, String... sortContent) throws IOException {
        var treeComparatorBuilder = new TreeComparator.Builder()
                .targetDirectory(targetTree)
                .expectedDirectory(expectedTree);
        for(String extension : sortContent) {
            treeComparatorBuilder.sortContent(extension);
        }
        var treeComparator = treeComparatorBuilder.build();
        Files.walkFileTree(expectedTree, treeComparator);
        return treeComparator.differences();
    }

    private Tree() {

    }

    public static void delete(Path sourceDirectory) throws IOException {
        if(sourceDirectory.toFile().exists()) {
            Files.walk(sourceDirectory) // NOSONAR
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
    }
}
