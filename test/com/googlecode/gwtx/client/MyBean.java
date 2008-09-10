package com.googlecode.gwtx.client;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author ndeloof
 *
 */
public class MyBean
{
    PropertyChangeSupport beanSupport = new PropertyChangeSupport( this );

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        beanSupport.addPropertyChangeListener( listener );
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        beanSupport.addPropertyChangeListener( propertyName, listener );
    }

    private String data;

    /**
     * @return the data
     */
    public String getData()
    {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData( String data )
    {
        beanSupport.firePropertyChange( "data", this.data, data );
        this.data = data;
    }
}
