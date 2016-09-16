package hr.fer.zemris.local;

/**
 * Classes that implement this interface must have a single method that will be run when a change happens in the object
 * that they are observing.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public interface ILocalizationListener {

    /**
     * It is assumed that when observed object changes a property that this listener is observing, it will invoke this
     * method and notify this listener that localization changed.
     */
    void localizationChanged();

}
