package dktrotti.transitr;

/**
 * Custom Observable due to Java Observable being a class.
 *
 * Created by Dominic on 16-01-30.
 */
public interface Observable {
    void updateObservers();
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
}
