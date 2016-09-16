package hr.fer.zemris.local.swing;

import hr.fer.zemris.local.ILocalizationProvider;

import javax.swing.JMenu;

/**
 * This class has a property that it's text changes every time localization changes.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class LJMenu extends JMenu {

    private static final long serialVersionUID = 7812624397177115265L;

    /**
     * Creates a new {@link LJMenu} which changes it's text every time provider fires localization change.
     * 
     * @param key key to translate
     * @param provider localization provider
     */
    public LJMenu(final String key, final ILocalizationProvider provider) {
        new LocalizedComponent(key, provider) {
            @Override
            public void change(final String arg) {
                setText(arg);
            }
        };
    }

}
