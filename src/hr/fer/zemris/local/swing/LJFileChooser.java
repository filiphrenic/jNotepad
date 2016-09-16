package hr.fer.zemris.local.swing;

import hr.fer.zemris.local.ILocalizationProvider;

import javax.swing.JFileChooser;

/**
 * This class has a property that it's dialog title changes every time localization changes.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class LJFileChooser extends JFileChooser {

    private static final long serialVersionUID = 8461456821835458602L;

    /**
     * Creates a new {@link LJFileChooser} that change's its dialog title whenever provider fires a localization change.
     * 
     * @param key key to find the translation
     * @param provider localization provider
     */
    public LJFileChooser(final String key, final ILocalizationProvider provider) {
        new LocalizedComponent(key, provider) {
            @Override
            public void change(final String arg) {
                setDialogTitle(arg);
            }
        };
    }
}
