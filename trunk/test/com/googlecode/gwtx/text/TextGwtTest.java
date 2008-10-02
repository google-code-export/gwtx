package com.googlecode.gwtx.text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author ndeloof
 */
public class TextGwtTest
    extends GWTTestCase
{

    /**
     * {@inheritDoc}
     *
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    public String getModuleName()
    {
        return "com.googlecode.gwtx.TextTestModule";
    }

    public void testSimpleDateFormat()
        throws Exception
    {
        DateFormat format = new SimpleDateFormat( "E dd/MM/yyyy HH:mm:ss.SSS" );
        Date now = new Date();
        String formatted = format.format( now );
        System.out.println( "formatted date : " + formatted );
        assertEquals( now, format.parse( formatted ) );
    }

}
