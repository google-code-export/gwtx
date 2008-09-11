package com.googlecode.gwtx.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

/**
 * @author ndeloof
 *
 */
public class NotNullVetoableListener
    implements VetoableChangeListener
{

    /**
     * {@inheritDoc}
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange( PropertyChangeEvent evt )
        throws PropertyVetoException
    {
        if (evt.getNewValue() == null)
        {
            throw new PropertyVetoException( "Cannot be null", evt );
        }
    }

}
