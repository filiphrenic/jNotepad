package hr.fer.zemris.local;

/**
 * Classes that implement this interface know how to get a valid string from given key, but also know how to add
 * observers (listeners) that they will notify when localization changes.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public interface ILocalizationProvider {

    /**
     * Gets the translation for the given key.
     * 
     * @param key used key
     * @return translation of the key
     */
    String getString(final String key);

    /**
     * Adds the given listener.
     * 
     * @param listener listener to add
     */
    void addLocalizationListener(final ILocalizationListener listener);

    /**
     * Removes the given listener.
     * 
     * @param listener listener to remove
     */
    void removeLocalizationListener(final ILocalizationListener listener);

}
