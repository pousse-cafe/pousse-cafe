package poussecafe.doc;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Reporter;
import poussecafe.doc.model.ClassDocRepository;
import poussecafe.doc.model.DocletAccess;
import poussecafe.doc.options.BasePackageOption;
import poussecafe.doc.options.CustomDotExecutableOption;
import poussecafe.doc.options.CustomFdpExecutableOption;
import poussecafe.doc.options.DomainOption;
import poussecafe.doc.options.IncludeGeneratedDateOption;
import poussecafe.doc.options.OutputPathOption;
import poussecafe.doc.options.PdfFileNameOption;
import poussecafe.doc.options.SourcePathOption;
import poussecafe.doc.options.VersionOption;
import poussecafe.exception.PousseCafeException;
import poussecafe.runtime.Runtime;

public class PousseCafeDoclet implements Doclet {

    public PousseCafeDoclet() {
        configBuilder = new PousseCafeDocletConfiguration.Builder();
    }

    private PousseCafeDocletConfiguration.Builder configBuilder;

    private Runtime runtime;

    @Override
    public void init(Locale locale,
            Reporter reporter) {
        Logger.setRootDoc(reporter);
    }

    @Override
    public String getName() {
        return "DDD Documentation";
    }

    @Override
    public Set<? extends Option> getSupportedOptions() {
        Set<Option> supportedOptions = new HashSet<>();
        supportedOptions.add(new BasePackageOption(configBuilder));
        supportedOptions.add(new DomainOption(configBuilder));
        supportedOptions.add(new IncludeGeneratedDateOption(configBuilder));
        supportedOptions.add(new OutputPathOption(configBuilder));
        supportedOptions.add(new VersionOption(configBuilder));
        supportedOptions.add(new SourcePathOption(configBuilder));
        supportedOptions.add(new CustomDotExecutableOption(configBuilder));
        supportedOptions.add(new CustomFdpExecutableOption(configBuilder));
        supportedOptions.add(new PdfFileNameOption(configBuilder));
        return supportedOptions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_9;
    }

    @Override
    public boolean run(DocletEnvironment environment) {
        this.environment = environment;
        configuration = configBuilder.build();

        runtime = new Runtime.Builder()
                .withBundle(PousseCafeDoc.configure().defineAndImplementDefault().build())
                .withInjectableService(DocletEnvironment.class, environment)
                .withInjectableService(configuration)
                .build();

        Logger.info("Starting Pousse-Café doclet...");
        try {
            runtime.start();

            registerClassDocs();
            analyzeCode();
            createOutputFolder();

            writeGraphs();
            writeHtml();
            writePdf();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private DocletEnvironment environment;

    private PousseCafeDocletConfiguration configuration;

    private void registerClassDocs() {
        Set<TypeElement> typeElements = runtime.environment().service(DocletAccess.class).orElseThrow(NoSuchElementException::new).typeElements();
        runtime.environment()
                .service(ClassDocRepository.class)
                .orElseThrow(PousseCafeException::new)
                .registerTypeElements(typeElements);
    }

    private void analyzeCode() {
        detectModules();
        detectModulesComponents();
        detectDomainProcesses();
        detectRelations();
    }

    private void detectModules() {
        PackageInfoModuleDocCreator packageInfoModuleDocCreator = new PackageInfoModuleDocCreator();
        runtime.injector().injectDependenciesInto(packageInfoModuleDocCreator);

        PackagesAnalyzer codeAnalyzer = new PackagesAnalyzer.Builder()
                .packageDocConsumer(packageInfoModuleDocCreator)
                .build();
        runtime.injector().injectDependenciesInto(codeAnalyzer);
        codeAnalyzer.analyzeCode();

        ClassModuleDocCreator moduleDocCreator = new ClassModuleDocCreator();
        runtime.injector().injectDependenciesInto(moduleDocCreator);

        ClassesAnalyzer classCodeAnalyzer = new ClassesAnalyzer.Builder()
                .classDocConsumer(moduleDocCreator)
                .build();
        runtime.injector().injectDependenciesInto(classCodeAnalyzer);
        classCodeAnalyzer.analyzeCode();
    }

    private void detectModulesComponents() {
        AggregateDocCreator aggregateDocCreator = new AggregateDocCreator(environment);
        runtime.injector().injectDependenciesInto(aggregateDocCreator);

        ServiceDocCreator serviceDocCreator = new ServiceDocCreator(environment);
        runtime.injector().injectDependenciesInto(serviceDocCreator);

        EntityDocCreator entityDocCreator = new EntityDocCreator(environment);
        runtime.injector().injectDependenciesInto(entityDocCreator);

        ValueObjectDocCreator valueObjectDocCreator = new ValueObjectDocCreator(environment);
        runtime.injector().injectDependenciesInto(valueObjectDocCreator);

        ProcessStepDocCreator messageListenerDocCreator = new ProcessStepDocCreator(environment);
        runtime.injector().injectDependenciesInto(messageListenerDocCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .classDocConsumer(aggregateDocCreator)
                .classDocConsumer(serviceDocCreator)
                .classDocConsumer(entityDocCreator)
                .classDocConsumer(valueObjectDocCreator)
                .classDocConsumer(messageListenerDocCreator)
                .build();
        runtime.injector().injectDependenciesInto(codeAnalyzer);
        codeAnalyzer.analyzeCode();
    }

    private void detectDomainProcesses() {
        DomainProcessDocCreator domainProcessDocCreator = new DomainProcessDocCreator(environment);
        runtime.injector().injectDependenciesInto(domainProcessDocCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .classDocConsumer(domainProcessDocCreator)
                .build();
        runtime.injector().injectDependenciesInto(codeAnalyzer);
        codeAnalyzer.analyzeCode();
    }

    private void detectRelations() {
        RelationCreator relationCreator = new RelationCreator();
        runtime.injector().injectDependenciesInto(relationCreator);

        ClassesAnalyzer codeAnalyzer = new ClassesAnalyzer.Builder()
                .classDocConsumer(relationCreator)
                .build();
        runtime.injector().injectDependenciesInto(codeAnalyzer);
        codeAnalyzer.analyzeCode();
    }

    private void createOutputFolder() {
        File outputDirectory = new File(configuration.outputDirectory());
        outputDirectory.mkdirs();
    }

    private void writeGraphs() {
        GraphImagesWriter graphsWriter = new GraphImagesWriter(configuration);
        runtime.injector().injectDependenciesInto(graphsWriter);
        graphsWriter.writeImages();
    }

    private void writeHtml() {
        HtmlWriter htmlWriter = new HtmlWriter();
        runtime.injector().injectDependenciesInto(htmlWriter);
        htmlWriter.writeHtml();
    }

    private void writePdf() {
        PdfWriter pdfWriter = new PdfWriter();
        runtime.injector().injectDependenciesInto(pdfWriter);
        pdfWriter.writePdf();
    }
}
