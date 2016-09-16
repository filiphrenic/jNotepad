package hr.fer.zemris.local.swing;

import hr.fer.zemris.local.ILocalizationProvider;

import javax.swing.JToolBar;

/**
 * This class has a property that it's name changes every time localization changes.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class LJToolBar extends JToolBar {

    private static final long serialVersionUID = 6549262174007028198L;

    /**
     * Creates a new {@link LJToolBar} that changes it's name every time provider fire's localization change.
     * 
     * @param key key to translate
     * @param provider localization provider
     */
    public LJToolBar(final String key, final ILocalizationProvider provider) {
        new LocalizedComponent(key, provider) {
            @Override
            public void change(final String arg) {
                setName(arg);
            }
        };
    }

}
