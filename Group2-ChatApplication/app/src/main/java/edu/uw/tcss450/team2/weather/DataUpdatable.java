package edu.uw.tcss450.team2.weather;

/**
 * An interface for classes that contain data which needs to be told to update by other classes.
 *
 * @author Sam Spillers
 * @version 1.0
 */
public interface DataUpdatable {
    /**
     * Tells the class to update its contained data.
     *
     * @author Sam Spillers
     * @version 1.0
     */
    void updateData();
}
