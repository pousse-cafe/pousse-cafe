package poussecafe.source.generation.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;
import org.eclipse.text.edits.TextEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poussecafe.files.TextFiles;
import poussecafe.source.analysis.Name;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public class CompilationUnitEditor {

    public void setPackage(String packageName) {
        PackageDeclaration packageDeclaration = rewrite.ast().newPackageDeclaration();
        packageDeclaration.setName(rewrite.ast().newName(packageName));
        rewrite.set(CompilationUnit.PACKAGE_PROPERTY, packageDeclaration);
    }

    private CompilationUnitRewrite rewrite;

    public AstWrapper ast() {
        return ast;
    }

    private AstWrapper ast;

    public void addImport(String name) {
        if(!hasImport(name)) {
            ImportDeclaration importDeclaration = rewrite.ast().newImportDeclaration();
            importDeclaration.setName(rewrite.ast().newName(name));

            ListRewrite listRewrite = rewrite.listRewrite(CompilationUnit.IMPORTS_PROPERTY);
            listRewrite.insertLast(importDeclaration, null);
        }
    }

    public boolean hasImport(String name) {
        return rewrite.listRewrite(CompilationUnit.IMPORTS_PROPERTY).getRewrittenList().stream()
                .anyMatch(importDeclaration -> importsName(importDeclaration, name));
    }

    private boolean importsName(Object importDeclarationObject, String name) {
        ImportDeclaration importDeclaration = (ImportDeclaration) importDeclarationObject;
        return importDeclaration.getName().getFullyQualifiedName().equals(name);
    }

    public void addImport(Name name) {
        addImport(name.toString());
    }

    public void addImport(Class<?> importedClass) {
        addImport(importedClass.getCanonicalName());
    }

    public void setDeclaredType(TypeDeclaration typeDeclaration) {
        ListRewrite typesRewrite = rewrite.listRewrite(CompilationUnit.TYPES_PROPERTY);
        var types = typesRewrite.getOriginalList();
        if(types.isEmpty()) {
            typesRewrite.insertFirst(typeDeclaration, null);
        } else {
            throw new CodeGenerationException("Unexpected number of types in compilation unit");
        }
    }

    public TypeDeclarationEditor typeDeclaration() {
        var existingType = rewrite.compilationUnit().types().stream()
                .findFirst();
        if(existingType.isPresent()) {
            var type = (TypeDeclaration) existingType.get();
            var nodeRewriter = new NodeRewrite(rewrite.rewrite(), type);
            return new TypeDeclarationEditor(nodeRewriter, false);
        } else {
            var newTypeDeclaration = rewrite.ast().newTypeDeclaration();
            var nodeRewriter = new NodeRewrite(rewrite.rewrite(), newTypeDeclaration);
            rewrite.listRewrite(CompilationUnit.TYPES_PROPERTY).insertLast(newTypeDeclaration, null);
            return new TypeDeclarationEditor(nodeRewriter, true);
        }
    }

    public void flush() {
        try {
            if(isNew) {
                organizeImports();
                var edits = rewrite.rewrite(document);
                formatCode(edits);
                writeDocumentToFile();
            } else {
                var edits = rewrite.rewrite(document);
                if(edits.getChildrenSize() > 0) {
                    if(logger.isDebugEnabled()) {
                        logger.debug("Detected changes in {}, re-writing document", filePath);
                        logger.debug(edits.toString());
                    }
                    formatCode(edits);
                    resetRewrite();
                    organizeImports();
                    rewrite.rewrite(document);
                    writeDocumentToFile();
                } else {
                    logger.debug("No change in {}, no rewrite needed", filePath);
                }
            }
        } catch (BadLocationException e) {
            throw new CodeGenerationException("Unable to apply changes and format code", e);
        }
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private void organizeImports() {
        var importNames = listImportNamesAndClearImports();
        importNames.sort(this::compareImports);
        importNames.forEach(this::addNewImport);
    }

    private List<ImportDeclaration> listImportNamesAndClearImports() {
        var importNames = new ArrayList<ImportDeclaration>();
        ListRewrite listRewrite = rewrite.listRewrite(CompilationUnit.IMPORTS_PROPERTY);
        for(Object importObject : listRewrite.getRewrittenList()) {
            ImportDeclaration declaration = (ImportDeclaration) importObject;
            importNames.add(declaration);
            listRewrite.remove(declaration, null);
        }
        return importNames;
    }

    private int compareImports(ImportDeclaration d1, ImportDeclaration d2) {
        if(d1.isStatic() == d2.isStatic()) {
            return d1.getName().getFullyQualifiedName().compareTo(d2.getName().getFullyQualifiedName());
        } else if(d1.isStatic() && !d2.isStatic()) {
            return 1;
        } else { // !d1.isStatic() && d2.isStatic()
            return -1;
        }
    }

    private void addNewImport(ImportDeclaration declaration) {
        rewrite.listRewrite(CompilationUnit.IMPORTS_PROPERTY).insertLast(declaration, null);
    }

    private void formatCode(TextEdit edits) throws BadLocationException {
        var options = new HashMap<>(JavaCore.getDefaultOptions());

        options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGN_WITH_SPACES, "true");

        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_PACKAGE, "0");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_PACKAGE, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_IMPORTS, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_IMPORTS, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_TYPE_DECLARATIONS, "1");

        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIRST_CLASS_BODY_DECLARATION, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_LAST_CLASS_BODY_DECLARATION, "0");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_NEW_CHUNK, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_MEMBER_TYPE, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIELD, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_ABSTRACT_METHOD, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "1");

        options.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaCore.INSERT);

        CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
        String code = document.get();
        TextEdit codeFormatEdit;
        if(isNew) {
            codeFormatEdit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, code, 0, code.length(), 0, null);
        } else {
            IRegion[] regions = extractRegions(edits);
            int indentationLevel = 0;
            codeFormatEdit = codeFormatter.format(CodeFormatter.K_UNKNOWN, code, regions, indentationLevel, null);
        }
        codeFormatEdit.apply(document);
    }

    private IRegion[] extractRegions(TextEdit edits) {
        var regions = new ArrayList<IRegion>();
        extractRegions(edits, regions);
        return regions.toArray(new IRegion[0]);
    }

    private void extractRegions(TextEdit edits, ArrayList<IRegion> regions) {
        int childrenSize = edits.getChildrenSize();
        if(childrenSize == 0 && edits.getOffset() != -1) {
            regions.add(edits.getRegion());
        } else {
            for(TextEdit child : edits.getChildren()) {
                if(child.getOffset() != -1) {
                    extractRegions(child, regions);
                }
            }
        }
    }

    private void writeDocumentToFile() {
        try {
            fileDirectory.toFile().mkdirs();
            try(var writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                writer.write(document.get());
            }
        } catch (IOException e) {
            throw new CodeGenerationException("Unable to write file", e);
        }
    }

    private Document document;

    private Path fileDirectory;

    private Path filePath;

    public static class Builder {

        private CompilationUnitEditor editor = new CompilationUnitEditor();

        public CompilationUnitEditor build() {
            requireNonNull(packageName);
            requireNonNull(fileName);

            if(editor.fileDirectory == null) {
                requireNonNull(sourceDirectory);
                editor.fileDirectory = sourceDirectory;
                String[] packageSegments = packageName.split("\\.");
                for(String segment : packageSegments) {
                    editor.fileDirectory = editor.fileDirectory.resolve(segment);
                }
            }
            editor.filePath = editor.fileDirectory.resolve(fileName);

            editor.prepareEdit();

            return editor;
        }

        public Builder sourceDirectory(Path sourceDirectory) {
            this.sourceDirectory = sourceDirectory;
            return this;
        }

        private Path sourceDirectory;

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        private String packageName;

        public Builder className(String className) {
            fileName = className + ".java";
            return this;
        }

        public Builder fileDirectory(Path fileDirectory) {
            editor.fileDirectory = fileDirectory;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        private String fileName;
    }

    private CompilationUnitEditor() {

    }

    protected void prepareEdit() {
        document = document();
        resetRewrite();
    }

    private void resetRewrite() {
        ASTParser parser = ASTParser.newParser(AST.JLS14);

        Map<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_11, options);
        parser.setCompilerOptions(options);

        parser.setSource(document.get().toCharArray());

        rewrite = new CompilationUnitRewrite((CompilationUnit) parser.createAST(null));
        ast = new AstWrapper(rewrite.ast());
    }

    private Document document() {
        File file = filePath.toFile();
        if(file.exists()) {
            isNew = false;
            try {
                return new Document(TextFiles.readContent(file));
            } catch (IOException e) {
                throw new CodeGenerationException("Unable to load document content", e);
            }
        } else {
            isNew = true;
            return new Document();
        }
    }

    private boolean isNew;

    public boolean isNew() {
        return isNew;
    }
}
