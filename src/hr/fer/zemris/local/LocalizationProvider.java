package hr.fer.zemris.local;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Used to provide localized text. It is a singleton class.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public final class LocalizationProvider extends AbstractLocalizationProvider {

    /** Default langauge is set to 'en' -> english */
    private static final String DEFAULT_LANGUAGE = "en";
    /** Needed for the bundle */
    private static final String PACKAGE = LocalizationProvider.class.getPackage().getName();
    /** Appended to the package name to get the correct bundle */
    private static final String LANG = ".lang";

    /** Used as a baseName to get the wanted bundle */
    private static final String BASE_NAME = PACKAGE + LANG;

    private final static LocalizationProvider instance = new LocalizationProvider();
    private String language;
    private ResourceBundle bundle;

    /**
     * Used to create a new instance of the {@link LocalizationProvider}, only done once. Sets the language to the
     * default.
     */
    private LocalizationProvider() {
        setLanguage(DEFAULT_LANGUAGE);
    }

    /**
     * Returns the text stored in the resource bundle if there is a given key in the bundle. Otherwise it returns ?key?
     * if something unkown was passed as a key.
     * 
     * @param key key used to get some text
     */
    @Override
    public String getString(String key) {
        String text;
        try {
            try {
                text = new String(bundle.getString(key).getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                text = "unknown encoding";
            }
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            text = "?" + key + "?";
        }
        return text;

    }

    /**
     * Sets the wanted language.
     * 
     * @param language language to set
     */
    public void setLanguage(final String language) {
        if (language.equalsIgnoreCase(this.language)) {
            return; // language doesn't need to change
        }

        this.language = language;
        final Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle(BASE_NAME, locale);

        fire(); // notify all listeners
    }

    /**
     * Returns an instance of this class.
     * 
     * @return an instance of {@link LocalizationProvider}
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

}
