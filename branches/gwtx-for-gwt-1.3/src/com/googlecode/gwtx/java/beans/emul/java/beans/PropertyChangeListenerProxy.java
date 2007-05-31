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

import java.util.EventListenerProxy;
import java.util.EventListener;

/**
 * A class which extends the EventListenerProxy specifically for adding a named PropertyChangeListener.
 *
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/beans/PropertyChangeListenerProxy.html">Sun's Javadocs</a>
 */
public class PropertyChangeListenerProxy extends EventListenerProxy implements PropertyChangeListener {
    private final String propertyName;

    public PropertyChangeListenerProxy(final String propertyName, final PropertyChangeListener listener) {
        super((EventListener)listener); // the cast is necessary for some reason to get it to compile.
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void propertyChange(final PropertyChangeEvent event) {
        final PropertyChangeListener listener = (PropertyChangeListener)getListener();
        listener.propertyChange(event);
    }
}
