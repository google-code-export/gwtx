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

import java.util.Date;

/**
 * <code>SimpleFormatter</code> can be used to print a summary of the
 * information contained in a <code>LogRecord</code> object in a human
 * readable format.
 * 
 */
public class SimpleFormatter extends Formatter {
    /**
     * Constructs a <code>SimpleFormatter</code> object.
     */
    public SimpleFormatter() {
        super();
    }

    public String format(LogRecord r) {
        StringBuffer sb = new StringBuffer();
        sb.append(new Date(r.getMillis()));
        sb.append(r.getLevel().getName()).append(" "); //$NON-NLS-1$
        sb.append(r.getSourceClassName()).append("."); //$NON-NLS-1$
        sb.append(r.getSourceMethodName()).append(": "); //$NON-NLS-1$
        sb.append(formatMessage(r)).append("/");
        // TODO: Figure out a stack trace if we can
        return sb.toString();
    }
}

