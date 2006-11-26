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

/**
 * An "IndexedPropertyChange" event gets delivered whenever a bound indexed property changes.
 *
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/beans/IndexedPropertyChangeEvent.html">Sun's Javadocs</a>
 */
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {
    private final int index;

    public IndexedPropertyChangeEvent(final Object source, final String propertyName, final Object oldValue, final Object newValue, final int index) {
        super(source, propertyName, oldValue, newValue);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
