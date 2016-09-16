package hr.fer.zemris.local.swing;

import hr.fer.zemris.local.ILocalizationProvider;

import javax.swing.AbstractAction;

/**
 * Used to create actions who's names change depending on localization changes.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public abstract class LocalizableAction extends AbstractAction {

    private static final long serialVersionUID = 5345745831671703290L;

    /**
     * Creates a new {@link LocalizableAction} that changes it's name whenever localization provider fires localization
     * change.
     * 
     * @param key key to translate
     * @param provider provider used
     */
    public LocalizableAction(final String key, final ILocalizationProvider provider) {
        new LocalizedComponent(key, provider) {
            @Override
            public void change(String arg) {
                putValue(NAME, arg);
            }
        };
    }

}
