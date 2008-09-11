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

    private VetoableChangeSupport vetoableSupport = new VetoableChangeSupport( this );

    /**
     * @param propertyName
     * @param listener
     * @see java.beans.VetoableChangeSupport#addVetoableChangeListener(java.lang.String, java.beans.VetoableChangeListener)
     */
    public void addVetoableChangeListener( String propertyName, VetoableChangeListener listener )
    {
        vetoableSupport.addVetoableChangeListener( propertyName, listener );
    }

    /**
     * @param listener
     * @see java.beans.VetoableChangeSupport#addVetoableChangeListener(java.beans.VetoableChangeListener)
     */
    public void addVetoableChangeListener( VetoableChangeListener listener )
    {
        vetoableSupport.addVetoableChangeListener( listener );
    }

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
        vetoableSupport.fireVetoableChange( "data", this.data, data );
        this.data = data;
    }
}
