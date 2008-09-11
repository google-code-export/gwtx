package com.googlecode.gwtx.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/**
 * @author ndeloof
 *
 */
public class MyBean
{
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport( this );

    /**
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener( PropertyChangeListener listener )
    {
        changeSupport.addPropertyChangeListener( listener );
    }

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener )
    {
        changeSupport.addPropertyChangeListener( propertyName, listener );
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
    public void setData( String data ) throws PropertyVetoException
    {
        changeSupport.firePropertyChange( "data", this.data, data );
        this.data = data;
    }
}
