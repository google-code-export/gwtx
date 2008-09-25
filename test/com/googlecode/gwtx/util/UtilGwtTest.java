package com.googlecode.gwtx.util;

import java.util.StringTokenizer;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author ndeloof
 */
public class UtilGwtTest
    extends GWTTestCase
{

    /**
     * {@inheritDoc}
     *
     * @see com.google.gwt.junit.client.GWTTestCase#getModuleName()
     */
    public String getModuleName()
    {
        return "com.googlecode.gwtx.UtilTestModule";
    }

    public void testTokenizer()
        throws Exception
    {
        StringTokenizer tokenizer = new StringTokenizer( "a:bcd::ef", ":" );
        assertEquals( 3, tokenizer.countTokens() );
        assertTrue( tokenizer.hasMoreTokens() );
        assertEquals( "a", tokenizer.nextToken() );
        assertTrue( tokenizer.hasMoreTokens() );
        assertEquals( "bcd", tokenizer.nextToken() );
        assertTrue( tokenizer.hasMoreTokens() );
        assertEquals( "ef", tokenizer.nextToken() );
        assertTrue( ! tokenizer.hasMoreTokens() );
    }

}
