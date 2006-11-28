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
// TODO: Make this detect the browser's logging tool if it has one.
public class ConsoleHandler extends Handler {

    /**
     * Constructs a <code>ConsoleHandler</code> object.
     */
    public ConsoleHandler() {
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
                String msg = null;
                try {
                    msg = getFormatter().format(record);
                } catch (Exception e) {
                    // logging.17=Exception occurred while formatting the log record.
                    getErrorManager().error("Exception occurred while formatting the log record.", //$NON-NLS-1$
                            e, ErrorManager.FORMAT_FAILURE);
                }
                write(msg);
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

    // Write a string to the output stream.
    private void write(String s) {
        // FIXME: Implement me.
        log(s);
        try {
            //this.writer.write(s);
        } catch (Exception e) {
            // logging.14=Exception occurred when writing to the output stream.
            getErrorManager().error("Exception occurred when writing to the output stream.", e, //$NON-NLS-1$
                    ErrorManager.WRITE_FAILURE);
        }
    }


    public String toString() {
        return "ConsoleHandler{" + super.toString() + "}";
    }

    private static native void log(String s) /*-{
    //    $wnd.console.log(s);
    $wnd.alert(S);
    }-*/;
}
