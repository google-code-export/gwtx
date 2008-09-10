package com.googlecode.gwtx.client;

import java.beans.PropertyChangeEvent;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author ndeloof
 *
 */
public class IntegrationGwtTest
    extends GWTTestCase
{

    /**
     * {@inheritDoc}
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    @Override
    public String getModuleName()
    {
        return "com.googlecode.gwtx.TestModule";
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
