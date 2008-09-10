package com.googlecode.gwtx.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ndeloof
 *
 */
public class MyListener implements PropertyChangeListener
{
    List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();

    /**
     * {@inheritDoc}
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange( PropertyChangeEvent evt )
    {
        events.add( evt );
    }

    /**
     * @return the events
     */
    public PropertyChangeEvent getLastEvent()
    {
        return events.get( events.size() - 1 );
    }
}
