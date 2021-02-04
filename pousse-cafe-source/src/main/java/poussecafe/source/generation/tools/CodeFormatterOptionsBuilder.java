package poussecafe.source.generation.tools;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CodeFormatterOptionsBuilder {

    public CodeFormatterOptionsBuilder withProfile(Path codeFormatterProfile) {
        try {
            var documentBuilder = safeDocumentBuilder();
            var document = documentBuilder.parse(codeFormatterProfile.toFile());
            loadSettings(document);
            return this;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to read profile from file " + codeFormatterProfile, e);
        }
    }

    private DocumentBuilder safeDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); // NOSONAR - XXE prevented by the 2 following lines
        dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return dbFactory.newDocumentBuilder();
    }

    private void loadSettings(Document doc) {
        var profilesElement = (Element) doc.getElementsByTagName("profiles").item(0);
        var profilesList = profilesElement.getElementsByTagName("profile");
        for(int i = 0; i < profilesList.getLength(); ++i) {
            var profileElement = (Element) profilesList.item(i);
            var profileKind = profileElement.getAttribute("kind");
            if("CodeFormatterProfile".equals(profileKind)) {
                options.putAll(getProfileSettings(profileElement));
                return; // Skip next profiles if any
            }
        }
    }

    private Map<String, String> getProfileSettings(Element profileElement) {
        var settings = new HashMap<String, String>();
        var settingsList = profileElement.getElementsByTagName("setting");
        for(int i = 0; i < settingsList.getLength(); ++i) {
            var settingElement = (Element) settingsList.item(i);
            var id = settingElement.getAttribute("id");
            var value = settingElement.getAttribute("value");
            settings.put(id, value);
        }
        return settings;
    }

    public CodeFormatterOptionsBuilder withProfile(InputStream codeFormatterProfile) {
        try {
            var documentBuilder = safeDocumentBuilder();
            var document = documentBuilder.parse(codeFormatterProfile);
            loadSettings(document);
            return this;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to read profile from file " + codeFormatterProfile, e);
        }
    }

    public Map<String, String> build() {
        return options;
    }

    private Map<String, String> options = defaultOptions();

    public CodeFormatterOptionsBuilder withContext(IScopeContext context) {
        String nodeQualifier = JavaCore.PLUGIN_ID + ".formatter";
        var node = context.getNode(nodeQualifier);
        try {
            for(String key : node.keys()) {
                String propertyName = nodeQualifier + "." + key;
                String propertyValue = node.get(key, null);
                logger.debug("Code formatting property {} = {}", propertyName, propertyValue);
                options.put(propertyName, propertyValue);
            }
        } catch (BackingStoreException e) {
            logger.error("Unable to load code formatter preferences", e);
        }
        return this;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, String> defaultOptions() {
        var defaultOptions = new HashMap<String, String>();

        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_ALIGN_WITH_SPACES, "true");

        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_PACKAGE, "0");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_PACKAGE, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_IMPORTS, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_IMPORT_GROUPS, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_IMPORTS, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BETWEEN_TYPE_DECLARATIONS, "1");

        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIRST_CLASS_BODY_DECLARATION, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_LAST_CLASS_BODY_DECLARATION, "0");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_NEW_CHUNK, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_MEMBER_TYPE, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_FIELD, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_ABSTRACT_METHOD, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_METHOD, "1");

        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_NUMBER_OF_EMPTY_LINES_TO_PRESERVE, "1");
        defaultOptions.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AT_END_OF_FILE_IF_MISSING, JavaCore.INSERT);

        return defaultOptions;
    }
}
