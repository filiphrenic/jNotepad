package hr.fer.zemris.local;

/**
 * Used as a decorator, a wrapper around some other provider.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
class LocalizationProviderBridge extends AbstractLocalizationProvider {

    private final ILocalizationProvider parent;
    private final ILocalizationListener listener = // notify all listeners
            this::fire;

    /** Indicates if the bridge connects something. */
    private boolean connected;

    /**
     * Creates a new {@link LocalizationProviderBridge} that has one parent {@link ILocalizationProvider}.
     * 
     * @param parent decorated provider
     */
    LocalizationProviderBridge(final ILocalizationProvider parent) {
        this.parent = parent;
    }

    @Override
    public String getString(final String key) {
        return parent.getString(key);
    }

    /**
     * Connects this bridge with some other provider.
     */
    void connect() {
        if (connected) {
            return; // already connected
        }
        connected = true;
        parent.addLocalizationListener(listener);
    }

    /**
     * Disconnects this bridge from some other provider.
     */
    void disconnect() {
        if (!connected) {
            return; // already disconnected
        }
        connected = false;
        parent.removeLocalizationListener(listener);
    }

}
