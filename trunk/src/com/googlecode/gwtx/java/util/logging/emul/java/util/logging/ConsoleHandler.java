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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.core.client.GWT;

import com.googlecode.gwtx.java.util.logging.client.ConsoleHandlerImpl;

/**
 * A handler that writes log messages to the standard output stream
 * <code>System.err</code>.
 * <p>
 * This handler reads the following properties from the log manager to
 * initialize itself:
 * <ul>
 * <li>java.util.logging.ConsoleHandler.level specifies the logging level,
 * defaults to <code>Level.INFO</code> if this property is not found or has an
 * invalid value;
 * <li>java.util.logging.ConsoleHandler.filter specifies the name of the filter
 * class to be associated with this handler, defaults to <code>null</code> if
 * this property is not found or has an invalid value;
 * <li>java.util.logging.ConsoleHandler.formatter specifies the name of the
 * formatter class to be associated with this handler, defaults to
 * <code>java.util.logging.SimpleFormatter</code> if this property is not
 * found or has an invalid value;
 * <li>java.util.logging.ConsoleHandler.encoding specifies the encoding this
 * handler will use to encode log messages, defaults to <code>null</code> if
 * this property is not found or has an invalid value.
 * </ul>
 * </p>
 * <p>
 * This class is not thread-safe.
 * </p>
 * 
 */
public class ConsoleHandler extends Handler {

    /**
     * Load the browser's logging tool impl.
     */
    private ConsoleHandlerImpl handlerImpl = (ConsoleHandlerImpl)GWT.create(ConsoleHandlerImpl.class);

    /**
     * Constructs a <code>ConsoleHandler</code> object.
     */
    public ConsoleHandler() {
        final Element[] metas = LogManager.getMetas();
        if (metas != null) {
            final String typeName = GWT.getTypeName(this);
            for (int i=0; i < metas.length; i++) {
                final Element meta = metas[i];
                final String name = DOM.getAttribute(meta, "name");
                if ("gwtx:property".equals(name)) {
                    final String content = DOM.getAttribute(meta, "content");
                    final int eq = content.indexOf("=");
                    if (eq != -1) {
                        final String cname = content.substring(0, eq);
                        final String cvalue = content.substring(eq+1);
                        
                        //"java.util.logging.ConsoleHandler.level"
                        if ((typeName + ".level").equals(cname)) {
                            setLevel(Level.parse(cvalue));
                        }
                    }
                }
            }
        }

        handlerImpl.init(this);
    }

    /**
     * Closes this handler. The <code>System.err</code> is flushed but not
     * closed.
     */
    public void close() {
    }

    /**
     * Logs a record if necessary. A flush operation will be done.
     * 
     * @param record the log record to be logged
     */
    public void publish(LogRecord record) {
        try {
            if (this.isLoggable(record)) {
                // XXX: Do we need to catch formatting exceptions?
                handlerImpl.publish(record, getFormatter());
            }
        } catch (Exception e) {
            // logging.18=Exception occurred while logging the record.
            getErrorManager().error("Exception occurred while logging the record.", e, //$NON-NLS-1$
                    ErrorManager.GENERIC_FAILURE);
        }
        flush();
    }

    public void flush() {
    }

    public String toString() {
        return "ConsoleHandler{" + super.toString() + "}";
    }
}
