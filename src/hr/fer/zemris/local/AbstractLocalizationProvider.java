package hr.fer.zemris.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements methods for adding and removing listeners. Also has a method that informs all listeners about change.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    /** List of listeners */
    final private List<ILocalizationListener> listeners = new ArrayList<>();

    @Override
    public abstract String getString(final String key);

    @Override
    public void addLocalizationListener(final ILocalizationListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeLocalizationListener(final ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Informs all listeners that listen to this provider.
     */
    void fire() {
        listeners.forEach(ILocalizationListener::localizationChanged);
    }

}
