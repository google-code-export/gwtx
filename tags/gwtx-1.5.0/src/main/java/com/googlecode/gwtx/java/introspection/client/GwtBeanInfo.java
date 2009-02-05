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
 */

package com.googlecode.gwtx.java.introspection.client;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ndeloof
 *
 */
public class GwtBeanInfo implements BeanInfo
{

    private List<PropertyDescriptor> descriptors;

    /**
     * {@inheritDoc}
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        return descriptors.toArray( new PropertyDescriptor[ descriptors.size() ] );
    }

    public void addPropertyDescriptor( PropertyDescriptor descriptor )
    {
        if (descriptors == null)
        {
            descriptors = new ArrayList<PropertyDescriptor>();
        }
        descriptors.add( descriptor );
    }
}
