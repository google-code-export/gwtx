/* 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package java.util.logging;

import com.google.gwt.core.client.GWT;

import java.io.UnsupportedEncodingException;

/**
 * A <code>Handler</code> object accepts a logging request and exports the
 * desired messages to a target, for example, a file, the console, etc. It can
 * be disabled by setting its logging level to <code>Level.OFF</code>.
 * 
 */
public abstract class Handler {

    /*
     * -------------------------------------------------------------------
     * Constants
     * -------------------------------------------------------------------
     */
    private static final Level DEFAULT_LEVEL = Level.ALL;

    /*
     * -------------------------------------------------------------------
     * Instance variables
     * -------------------------------------------------------------------
     */

    // the error manager to report errors during logging
    private ErrorManager errorMan;

    // the character encoding used by this handler
    private String encoding;

    // the logging level
    private Level level;

    // the formatter used to export messages
    private Formatter formatter;

    // the filter used to filter undesired messages
    private Filter filter;

    // class name, used for property reading
    private String prefix;

    /*
     * -------------------------------------------------------------------
     * Constructors
     * -------------------------------------------------------------------
     */

    /**
     * Constructs a <code>Handler</code> object with a default error manager,
     * the default encoding, and the default logging level
     * <code>Level.ALL</code>. It has no filter and no formatter.
     */
    protected Handler() {
        this.errorMan = new ErrorManager();
        this.level = DEFAULT_LEVEL;
        this.encoding = null;
        this.filter = null;
        this.formatter = new SimpleFormatter();
        this.prefix = GWT.getTypeName(this);
    }

    /*
     * -------------------------------------------------------------------
     * Methods
     * -------------------------------------------------------------------
     */

    // print error message in some format
    void printInvalidPropMessage(String key, String value, Exception e) {
        // logging.12=Invalid property value for
        String msg = new StringBuffer().append("Invalid property value for")  //$NON-NLS-1$
                .append(prefix).append(":").append(key).append("/").append( //$NON-NLS-1$//$NON-NLS-2$
                        value).toString();
        errorMan.error(msg, e, ErrorManager.GENERIC_FAILURE);
    }

    /**
     * Closes this handler. A flush operation will usually be performed and all
     * the associated resources will be freed. Client applications should not
     * use a handler after closing it.
     * 
     * @throws SecurityException
     *             If a security manager determines that the caller does not
     *             have the required permission.
     */
    public abstract void close();

    /**
     * Flushes any buffered output.
     */
    public abstract void flush();

    /**
     * Accepts an actual logging request.
     * 
     * @param record
     *            the log record to be logged
     */
    public abstract void publish(LogRecord record);

    /**
     * Gets the character encoding used by this handler.
     * 
     * @return the character encoding used by this handler
     */
    public String getEncoding() {
        return this.encoding;
    }

    /**
     * Gets the error manager used by this handler to report errors during
     * logging.
     * 
     * @return the error manager used by this handler
     * @throws SecurityException
     *             If a security manager determines that the caller does not
     *             have the required permission.
     */
    public ErrorManager getErrorManager() {
        return this.errorMan;
    }

    /**
     * Gets the filter used by this handler.
     * 
     * @return the filter used by this handler
     */
    public Filter getFilter() {
        return this.filter;
    }

    /**
     * Gets the formatter used by this handler to format the logging messages.
     * 
     * @return the formatter used by this handler
     */
    public Formatter getFormatter() {
        return this.formatter;
    }

    /**
     * Gets the logging level of this handler.
     * 
     * @return the logging level of this handler
     */
    public Level getLevel() {
        return this.level;
    }

    /**
     * Determines whether the supplied log record need to be logged. The logging
     * levels will be checked as well as the filter.
     * 
     * @param record
     *            the log record to be checked
     * @return <code>true</code> if the supplied log record need to be logged,
     *         otherwise <code>false</code>
     */
    public boolean isLoggable(LogRecord record) {
        if (null == record) {
            throw new NullPointerException();
        }
        if (this.level.intValue() == Level.OFF.intValue()) {
            return false;
        } else if (record.getLevel().intValue() >= this.level.intValue()) {
            return null == this.filter || this.filter.isLoggable(record);
        }
        return false;
    }

    /**
     * Report an error to the error manager associated with this handler.
     * 
     * @param msg
     *            the error message
     * @param ex
     *            the associated exception
     * @param code
     *            the error code
     */
    protected void reportError(String msg, Exception ex, int code) {
        this.errorMan.error(msg, ex, code);
    }

    /**
     * Sets the character encoding used by this handler. A <code>null</code>
     * value indicates the using of the default encoding. This internal method
     * does not check security.
     * 
     * @param newEncoding
     *            the character encoding to set
     * @throws UnsupportedEncodingException
     *             If the specified encoding is not supported by the runtime.
     */
    void internalSetEncoding(String newEncoding)
            throws UnsupportedEncodingException {
        // accepts "null" because it indicates using default encoding
        if (null == newEncoding) {
            this.encoding = null;
        } else {
            // logging.13=The encoding "{0}" is not supported.
            throw new UnsupportedEncodingException("The encoding \"" + newEncoding + "\" is not supported.");
        }
    }

    /**
     * Sets the character encoding used by this handler. A <code>null</code>
     * value indicates the using of the default encoding.
     * 
     * @param encoding
     *            the character encoding to set
     * @throws SecurityException
     *             If a security manager determines that the caller does not
     *             have the required permission.
     * @throws UnsupportedEncodingException
     *             If the specified encoding is not supported by the runtime.
     */
    public void setEncoding(String encoding) throws Exception, UnsupportedEncodingException {
        internalSetEncoding(encoding);
    }

    /**
     * Sets the error manager for this handler.
     * 
     * @param em
     *            the error manager to set
     * @throws SecurityException
     *             If a security manager determines that the caller does not
     *             have the required permission.
     */
    public void setErrorManager(ErrorManager em) {
        if (null == em) {
            throw new NullPointerException();
        }
        this.errorMan = em;
    }

    /**
     * Sets the filter to be used by this handler.
     * 
     * @param newFilter
     *            the filter to set
     * @throws SecurityException
     *             If a security manager determines that the caller does not
     *             have the required permission.
     */
    public void setFilter(Filter newFilter) {
        this.filter = newFilter;
    }

    /**
     * Sets the formatter to be used by this handler. This internal method does
     * not check security.
     * 
     * @param newFormatter
     *            the formatter to set
     */
    void internalSetFormatter(Formatter newFormatter) {
        if (null == newFormatter) {
            throw new NullPointerException();
        }
        this.formatter = newFormatter;
    }

    /**
     * Sets the formatter to be used by this handler.
     * 
     * @param newFormatter
     *            the formatter to set
     * @throws SecurityException
     *             If a security manager determines that the caller does not
     *             have the required permission.
     */
    public void setFormatter(Formatter newFormatter) {
        internalSetFormatter(newFormatter);
    }

    /**
     * Sets the logging level of this handler.
     * 
     * @param newLevel
     *            the logging level to set
     * @throws SecurityException
     *             If a security manager determines that the caller does not
     *             have the required permission.
     */
    public void setLevel(Level newLevel) {
        if (null == newLevel) {
            throw new NullPointerException();
        }
        this.level = newLevel;
    }


    public String toString() {
        return "Handler{" +
                "errorMan=" + errorMan +
                (encoding != null ? ", encoding='" + encoding + '\'' : "") +
                ", level=" + level +
                ", formatter=" + formatter +
                (filter != null ?", filter=" + filter : "") +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}

