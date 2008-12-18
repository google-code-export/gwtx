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

package java.beans;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;

public class PropertyDescriptor
{
    private Method getter;

    private Method setter;

    private String name;
	
	private Class<?> propertyType;

    public PropertyDescriptor( String propertyName, Class<?> propertyType, Method getter, Method setter )
        throws IntrospectionException
    {
        super();
        this.name = propertyName;
		this.propertyType = propertyType;
        this.getter = getter;
        this.setter = setter;
    }

	public Class<?> getPropertyType() 
	{
		return propertyType;
	}
		
    public Method getWriteMethod()
    {
        return setter;
    }

    public void setWriteMethod( Method writeMethod )
        throws IntrospectionException
    {
        this.setter = writeMethod;
    }

    public Method getReadMethod()
    {
        return getter;
    }

    public void setReadMethod( Method readMethod )
        throws IntrospectionException
    {
        this.getter = readMethod;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

}
