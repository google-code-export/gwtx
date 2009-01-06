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

import java.beans.BeanInfo;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.gwtx.java.introspection.client.GwtIntrospector;

public class Introspector extends GwtIntrospector {

    private Introspector() {
        super();
    }

    /**
     * Gets the <code>BeanInfo</code> object which contains the information of
     * the properties, events and methods of the specified bean class.
     *
     * <p>
     * The <code>Introspector</code> will cache the <code>BeanInfo</code>
     * object. Subsequent calls to this method will be answered with the cached
     * data.
     * </p>
     *
     * @param beanClass
     *            the specified bean class.
     * @return the <code>BeanInfo</code> of the bean class.
     * @throws IntrospectionException
     */
    public static BeanInfo getBeanInfo(Class<?> beanClass)
            throws IntrospectionException {
        return __getBeanInfo(beanClass);
    }

/*
	Based on Harmony code
	http://svn.apache.org/repos/asf/harmony/enhanced/classlib/trunk/modules/beans/src/main/java/java/beans/Introspector.java
*/	
			
    /**
     * Decapitalizes a given string according to the rule:
     * <ul>
     * <li>If the first or only character is Upper Case, it is made Lower Case
     * <li>UNLESS the second character is also Upper Case, when the String is
     * returned unchanged <eul>
     * 
     * @param name -
     *            the String to decapitalize
     * @return the decapitalized version of the String
     */
    public static String decapitalize(String name) {

        if (name == null)
            return null;
        // The rule for decapitalize is that:
        // If the first letter of the string is Upper Case, make it lower case
        // UNLESS the second letter of the string is also Upper Case, in which case no
        // changes are made.
        if (name.length() == 0 || (name.length() > 1 && Character.isUpperCase(name.charAt(1)))) {
            return name;
        }
        
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }						
}



