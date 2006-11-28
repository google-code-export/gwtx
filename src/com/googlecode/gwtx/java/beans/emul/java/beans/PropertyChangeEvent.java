/*
 * Copyright 2006 Sandy McArthur, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package java.beans;

import java.util.EventObject;

/**
 * A "PropertyChange" event gets delivered whenever a "bound" or "constrained" property changes.
 *
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/beans/PropertyChangeEvent.html">Sun's Javadocs</a>
 */
public class PropertyChangeEvent extends EventObject {
    private final String propertyName;
    private final Object oldValue;
    private final Object newValue;
    private Object propagationId;

    public PropertyChangeEvent(final Object source, final String propertyName, final Object oldValue, final Object newValue) {
        super(source);
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getPropagationId() {
        return propagationId;
    }

    public void setPropagationId(final Object propagationId) {
        this.propagationId = propagationId;
    }
}