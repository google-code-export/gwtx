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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 * This is a utility class that can be used by beans that support bound properties.
 *
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/beans/PropertyChangeSupport.html">Sun's Javadocs</a>
 */
public class PropertyChangeSupport implements Serializable {
    private final Object source;

    private List listeners;
    private Map propertyListeners = new HashMap();

    public PropertyChangeSupport(final Object source) {
        this.source = source;
    }

    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (listeners == null) {
            listeners = new ArrayList();
        }
        listeners.add(listener);
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        if (listener == null || listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        if (listeners == null && propertyListeners == null) {
            return new PropertyChangeListener[0];
        }
        final List l = new ArrayList();

        if (listeners != null) {
            final Iterator iter = listeners.iterator();
            while (iter.hasNext()) {
                l.add(iter.next());
            }
        }

        if (propertyListeners != null) {
            final Iterator entryIter = propertyListeners.entrySet().iterator();
            while (entryIter.hasNext()) {
                final Map.Entry entry = (Map.Entry)entryIter.next();
                final String name = (String)entry.getKey();
                final PropertyChangeSupport pcs = (PropertyChangeSupport)entry.getValue();
                final Iterator pcsIter = pcs.listeners.iterator();
                while (pcsIter.hasNext()) {
                    final PropertyChangeListenerProxy proxy = new PropertyChangeListenerProxy(name, (PropertyChangeListener)entryIter.next());
                    l.add(proxy);
                }
            }
        }

        final PropertyChangeListener[] pcl = new PropertyChangeListener[l.size()];
        final Iterator iter = l.iterator();
        int i = 0;
        while (iter.hasNext()) {
            pcl[i++] = (PropertyChangeListener)iter.next();
        }
        return pcl;
    }

    public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        if (propertyName == null || listener == null) {
            return;
        }
        if (propertyListeners == null) {
            propertyListeners = new HashMap();
        }

        PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
        if (propertyListener == null) {
            propertyListener = new PropertyChangeSupport(source);
            propertyListeners.put(propertyName, propertyListener);
        }
        propertyListener.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        if (propertyListeners == null || propertyName == null || listener == null) {
            return;
        }
        final PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
        if (propertyListener != null) {
            propertyListener.removePropertyChangeListener(listener);
            // Remove key/value if no longer used.
            if (propertyListener.listeners != null && propertyListener.listeners.size() == 0) {
                propertyListeners.remove(propertyName);
            }
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners(final String propertyName) {
        if (propertyListeners == null || propertyName == null) {
            return new PropertyChangeListener[0];
        }

        final PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
        if (propertyListener == null) {
            return new PropertyChangeListener[0];
        }
        return propertyListener.getPropertyChangeListeners();
    }

    public void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }
        firePropertyChange(new PropertyChangeEvent(source, propertyName, oldValue, newValue));
    }

    public void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, new Integer(oldValue), new Integer(newValue));
        }
    }

    public void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
        if (oldValue != newValue) {
            firePropertyChange(propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    public void firePropertyChange(final PropertyChangeEvent event) {
        final Object oldValue = event.getOldValue();
        final Object newValue = event.getNewValue();
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }

        if (listeners != null) {
            final Iterator iter = listeners.iterator();
            while (iter.hasNext()) {
                final PropertyChangeListener listener = (PropertyChangeListener)iter.next();
                listener.propertyChange(event);
            }
        }

        if (propertyListeners != null) {
            final String propertyName = event.getPropertyName();
            if (propertyName != null) {
                final PropertyChangeSupport propertyListener = (PropertyChangeSupport)propertyListeners.get(propertyName);
                if (propertyListener != null) {
                    propertyListener.firePropertyChange(event);
                }
            }
        }
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final Object oldValue, final Object newValue) {
        if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
            // don't fire if nothing really changed
            return;
        }
        firePropertyChange(new IndexedPropertyChangeEvent(source, propertyName, oldValue, newValue, index));
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final int oldValue, final int newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, new Integer(oldValue), new Integer(newValue));
        }
    }

    public void fireIndexedPropertyChange(final String propertyName, final int index, final boolean oldValue, final boolean newValue) {
        if (oldValue != newValue) {
            fireIndexedPropertyChange(propertyName, index, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
        }
    }

    public boolean hasListeners(final String propertyName) {
        if (listeners != null && listeners.size() > 0) {
            return true;
        } else if (propertyName != null) {
            return propertyListeners.containsKey(propertyName)
                    && ((PropertyChangeSupport)propertyListeners.get(propertyName)).hasListeners(null);
        } else {
            return false;
        }
    }
}
