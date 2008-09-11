package com.googlecode.gwtx.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author ndeloof
 *
 */
public class BeansGwtTest
    extends GWTTestCase
{

    /**
     * {@inheritDoc}
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName()
    {
        return "com.googlecode.gwtx.BeansTestModule";
    }

    public void testBeans()
        throws Exception
    {
        MyBean bean = new MyBean();
        MyListener listener = new MyListener();
        bean.addPropertyChangeListener( listener );
        bean.setData( "test" );
        PropertyChangeEvent event = listener.getLastEvent();
        assertNotNull( event );
        assertEquals( bean, event.getSource() );
        assertEquals( "test", event.getNewValue() );
        assertEquals( null, event.getOldValue() );
    }
}
