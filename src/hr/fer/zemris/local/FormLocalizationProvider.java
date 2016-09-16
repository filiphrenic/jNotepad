package hr.fer.zemris.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Used to connect/disconnect frames from localization providers.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    /**
     * Creates a new {@link FormLocalizationProvider}
     * 
     * @param parent localization provider that will notify the frame when something changes
     * @param frame frame which listens to the parent's changes
     */
    public FormLocalizationProvider(final ILocalizationProvider parent, final JFrame frame) {
        super(parent);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(final WindowEvent e) {
                // when a window is opened, frame is connected to the parent
                connect();
            }

            @Override
            public void windowClosed(final WindowEvent e) {
                // when a window is closed, frame is disconnected to the parent
                disconnect();
            }

        });

    }

}
