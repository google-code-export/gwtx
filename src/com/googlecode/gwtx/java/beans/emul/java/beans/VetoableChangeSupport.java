/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Based on http://svn.apache.org/repos/asf/harmony/enhanced/classlib/trunk/modules/beans/src/main/java/java/beans/VetoableChangeSupport.java@r19032
 */

package java.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is a utility class that can be used by beans that support constrained properties.
 */
public class VetoableChangeSupport implements Serializable {

    private Object source;

    private List allVetoableChangeListeners = new ArrayList();

    private Map selectedVetoableChangeListeners = new HashMap();

    public VetoableChangeSupport(final Object source) {
        if (source == null) {
            throw new NullPointerException();
        }
        this.source = source;
    }

    public void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
        PropertyChangeEvent event = createPropertyChangeEvent(propertyName, oldValue, newValue);
        doFirePropertyChange(event);
    }

    public synchronized void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        if ((propertyName != null) && (listener != null)) {
            List listeners = (List)selectedVetoableChangeListeners.get(propertyName);

            if (listeners != null) {
                listeners.remove(listener);
            }
        }
    }

    public synchronized void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        if (propertyName != null && listener != null) {
            List listeners = (List)selectedVetoableChangeListeners.get(propertyName);

            if (listeners == null) {
                listeners = new ArrayList();
                selectedVetoableChangeListeners.put(propertyName, listeners);
            }

            listeners.add(listener);
        }
    }

    public synchronized VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
        List listeners = null;

        if (propertyName != null) {
            listeners = (List)selectedVetoableChangeListeners.get(propertyName);
        }

        return (listeners == null) ? new VetoableChangeListener[]{}
                : getAsVetoableChangeListenerArray(listeners);

    }

    public void fireVetoableChange(String propertyName, boolean oldValue, boolean newValue) throws PropertyVetoException {
        PropertyChangeEvent event = createPropertyChangeEvent(propertyName, oldValue, newValue);
        doFirePropertyChange(event);
    }

    public void fireVetoableChange(String propertyName, int oldValue, int newValue) throws PropertyVetoException {
        PropertyChangeEvent event = createPropertyChangeEvent(propertyName, oldValue, newValue);
        doFirePropertyChange(event);
    }

    public synchronized boolean hasListeners(String propertyName) {
        boolean result = allVetoableChangeListeners.size() > 0;
        if (!result && propertyName != null) {
            List listeners = (List)selectedVetoableChangeListeners.get(propertyName);
            if (listeners != null) {
                result = listeners.size() > 0;
            }
        }
        return result;
    }

    public synchronized void removeVetoableChangeListener(VetoableChangeListener listener) {
        if (listener != null) {
            Iterator iterator = allVetoableChangeListeners.iterator();
            while (iterator.hasNext()) {
                VetoableChangeListener pcl = (VetoableChangeListener)iterator.next();
                if (pcl == listener) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    public synchronized void addVetoableChangeListener(VetoableChangeListener listener) {
        if (listener != null) {
            allVetoableChangeListeners.add(listener);
        }
    }

    public synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        List result = new ArrayList(allVetoableChangeListeners);

        Iterator keysIterator = selectedVetoableChangeListeners.keySet().iterator();
        while (keysIterator.hasNext()) {
            String propertyName = (String)keysIterator.next();

            List selectedListeners = (List)selectedVetoableChangeListeners.get(propertyName);
            if (selectedListeners != null) {
                Iterator iter = selectedListeners.iterator();
                while (iter.hasNext()) {
                    VetoableChangeListener listener = (VetoableChangeListener)iter.next();
                    result.add(new VetoableChangeListenerProxy(propertyName, listener));
                }

            }

        }

        return getAsVetoableChangeListenerArray(result);
    }

    public void fireVetoableChange(PropertyChangeEvent event) throws PropertyVetoException {
        doFirePropertyChange(event);
    }

    private PropertyChangeEvent createPropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
        return new PropertyChangeEvent(source, propertyName, oldValue, newValue);
    }

    private PropertyChangeEvent createPropertyChangeEvent(String propertyName, boolean oldValue, boolean newValue) {
        return new PropertyChangeEvent(source, propertyName, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }

    private PropertyChangeEvent createPropertyChangeEvent(String propertyName, int oldValue, int newValue) {
        return new PropertyChangeEvent(source, propertyName, new Integer(oldValue), new Integer(newValue));
    }

    private void doFirePropertyChange(PropertyChangeEvent event) throws PropertyVetoException {
        String propName = event.getPropertyName();
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && oldValue != null && newValue.equals(oldValue)) {
            return;
        }

        /* Take note of who we are going to notify (and potentially un-notify) */

        VetoableChangeListener[] listensToAll; // Listeners to all property
        // change events
        VetoableChangeListener[] listensToOne = null; // Listens to a given
        // property change
        synchronized (this) {
            listensToAll = new VetoableChangeListener[allVetoableChangeListeners.size()];
            Iterator iter = allVetoableChangeListeners.iterator();
            int i=0;
            while (iter.hasNext()) {
                listensToAll[i++] = (VetoableChangeListener)iter.next();
            }

            List listeners = (List)selectedVetoableChangeListeners.get(event.getPropertyName());
            if (listeners != null) {
                listensToOne = new VetoableChangeListener[listeners.size()];
                iter = listeners.iterator();
                i = 0;
                while (iter.hasNext()) {
                    listensToOne[i++] = (VetoableChangeListener)iter.next();
                }
            }
        }

        try {
            for (int i1 = 0; i1 < listensToAll.length; i1++) {
                VetoableChangeListener listener = listensToAll[i1];
                listener.vetoableChange(event);
            }
            if (listensToOne != null) {
                for (int i = 0; i < listensToOne.length; i++) {
                    VetoableChangeListener listener = listensToOne[i];
                    listener.vetoableChange(event);
                }
            }
        } catch (PropertyVetoException pve) {
            // Tell them we have changed it back
            PropertyChangeEvent revertEvent = createPropertyChangeEvent(
                    propName, newValue, oldValue);
            for (int i1 = 0; i1 < listensToAll.length; i1++) {
                VetoableChangeListener listener = listensToAll[i1];
                try {
                    listener.vetoableChange(revertEvent);
                } catch (PropertyVetoException ignored) {
                }
            }
            if (listensToOne != null) {
                for (int i = 0; i < listensToOne.length; i++) {
                    VetoableChangeListener listener = listensToOne[i];
                    try {
                        listener.vetoableChange(revertEvent);
                    } catch (PropertyVetoException ignored) {
                    }
                }
            }
            throw pve;
        }
    }

    private static VetoableChangeListener[] getAsVetoableChangeListenerArray(List listeners) {
        Object[] objects = listeners.toArray();
        VetoableChangeListener[] arrayResult = new VetoableChangeListener[objects.length];

        for (int i = 0; i < objects.length; ++i) {
            arrayResult[i] = (VetoableChangeListener)objects[i];
        }

        return arrayResult;
    }
}