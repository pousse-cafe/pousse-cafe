package poussecafe.maven;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import poussecafe.storage.internal.InternalStorage;

import static poussecafe.collection.Collections.asSet;

/**
 * <p>Generates all required classes to represent a new Aggregate. Given Aggregate name <code>MyAggregate</code>,
 * the following classes will be created:</p>
 * <ul>
 *  <li><code>MyAggregate</code>: the Aggregate Root,</li>
 *  <li><code>MyAggregateId</code>: the Aggregate's identifier type,</li>
 *  <li><code>MyAggregateFactory</code>: the Aggregate Factory,</li>
 *  <li><code>MyAggregateRepository</code>: the Aggregate Repository,</li>
 *  <li><code>MyAggregateDataAccess</code>: the interface describing Repository's requirements for commands and queries
 *  on stored data,</li>
 *  <li><code>MyAggregateAttributes</code>: the implementation for Aggregate's attributes (i.e. the data actually stored).</li>
 * </ul>
 * <p>Depending on chosen storage adapters, additional specific classes may be created in addition to the list mentioned above.</p>
 */
@Mojo(name = "add-aggregate")
public class AddAggregateMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException {
        AddAggregateExecutor executor = new AddAggregateExecutor.Builder()
                .sourceDirectory(sourceDirectory)
                .packageName(aggregatePackage)
                .name(aggregateName)
                .storageAdapters(asSet(storageAdapters))
                .missingAdaptersOnly(missingAdaptersOnly)
                .demoAttribute(demoAttribute)
                .build();
        executor.execute();
    }

    /**
     * Path of the folder containing the Model's source code. Classes and packages will be created in this folder.
     *
     * @since 0.3
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourceDirectory", required = true)
    private File sourceDirectory;

    /**
     * Enclosing package for the new classes and sub-packages. The package must not already exist.
     *
     * @since 0.3
     */
    @Parameter(property = "aggregatePackage", required = true)
    private String aggregatePackage;

    /**
     * Name of the new Aggregate. Aggregate's name is used to name all linked classes (Factory, Repository, ...).
     *
     * @since 0.3
     */
    @Parameter(property = "aggregateName", required = true)
    private String aggregateName;

    /**
     * List of storage adapters to create. Storage name is used to select them. By default, only internal storage
     * classes are generated. Currently, supported storage names are: "internal" and "mongo".
     *
     * @since 0.3
     */
    @Parameter(defaultValue = InternalStorage.NAME, property = "storageAdapters", required = true)
    private String[] storageAdapters;

    /**
     * Flag telling to add missing storage adapters. This is useful when you already have your aggregate ready but only
     * want to add support for a new storage.
     *
     * @since 0.3
     */
    @Parameter(defaultValue = "false", property = "missingAdaptersOnly", required = true)
    private boolean missingAdaptersOnly;

    /**
     * Adds a "demo" attribute to the Aggregate. This option may be used to generate an example when learning how
     * Aggregates should be written.
     *
     * @since 0.5
     */
    @Parameter(defaultValue = "false", property = "demoAttribute", required = true)
    private boolean demoAttribute;
}
