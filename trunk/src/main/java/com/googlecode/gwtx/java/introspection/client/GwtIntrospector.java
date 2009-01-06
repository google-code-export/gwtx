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

import java.util.HashMap;
import java.util.Map;

import java.beans.IntrospectionException;

/**
 * @author ndeloof
 */
public class GwtIntrospector
{
    private static Map<Class<?>, GwtBeanInfo> beanInfos = new HashMap<Class<?>, GwtBeanInfo>();

    protected static GwtBeanInfo __getBeanInfo( Class<?> beanClass )
        throws IntrospectionException
    {
        GwtBeanInfo beanInfo = beanInfos.get( beanClass );
        if ( beanInfo == null )
        {
            throw new IntrospectionException( "Class has not been enabled for introspection in GWT "
                + beanClass.getName() );
        }
        return beanInfo;
    }

    public static void setBeanInfo( Class<?> beanClass, GwtBeanInfo beanInfo )
    {
        beanInfos.put( beanClass, beanInfo );
    }
}
