package hr.fer.zemris.local.swing;

import hr.fer.zemris.local.ILocalizationProvider;

/**
 * This class provides one constructor and one method. When it is constructed and every time
 * localization provider fires a localization change, the method is invoked.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
abstract class LocalizedComponent {

    /**
     * Creates a new {@link LocalizedComponent} that does something every time provider fire's localization change.
     * 
     * @param key key to translate
     * @param provider localization provider
     */
    public LocalizedComponent(final String key, final ILocalizationProvider provider) {
        change(provider.getString(key));

        provider.addLocalizationListener(() -> change(provider.getString(key)));
    }

    /**
     * This method will be invoked when localization changes.
     * 
     * @param arg some new property that will be set
     */
    public abstract void change(String arg);

}
