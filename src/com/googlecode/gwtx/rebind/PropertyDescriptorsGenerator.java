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

package com.googlecode.gwtx.rebind;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * @author ndeloof
 */
public class PropertyDescriptorsGenerator
    extends Generator
{

    public String generate( TreeLogger logger, GeneratorContext context, String typeName )
        throws UnableToCompleteException
    {
        try
        {
            logger.log( Type.INFO, "Enable " + typeName + " for introspection" );
            return doGenerate( logger, context, typeName );
        }
        catch ( Exception e )
        {
            logger.log( TreeLogger.ERROR, null, e );
            throw new UnableToCompleteException();
        }
    }

    public String doGenerate( TreeLogger logger, GeneratorContext context, String typeName )
        throws Exception
    {
        TypeOracle typeOracle = context.getTypeOracle();
        JClassType type = typeOracle.getType( typeName );
        String packageName = type.getPackage().getName();
        String simpleClassName = type.getSimpleSourceName();

        String className = simpleClassName + "Introspector";
        String qualifiedBeanClassName = packageName + "." + className;
        SourceWriter sourceWriter = getSourceWriter( logger, context, packageName, className, type );
        if ( sourceWriter == null )
        {
            return qualifiedBeanClassName;
        }
        write( logger, sourceWriter, type );
        sourceWriter.commit( logger );
        return qualifiedBeanClassName;
    }

    protected SourceWriter getSourceWriter( TreeLogger logger, GeneratorContext context, String packageName,
                                            String beanClassName, JClassType superType )
    {
        PrintWriter printWriter = context.tryCreate( logger, packageName, beanClassName );
        if ( printWriter == null )
        {
            return null;
        }
        ClassSourceFileComposerFactory composerFactory =
            new ClassSourceFileComposerFactory( packageName, beanClassName );

        composerFactory.addImport( PropertyDescriptor.class.getName() );
        composerFactory.addImport( Method.class.getName() );
        // Do not use GwtBeanInfo.class as the BeanInfo interface is NOT fully implemented
        composerFactory.addImport( "com.googlecode.gwtx.java.client.GwtBeanInfo" );
        composerFactory.addImport( "com.googlecode.gwtx.java.client.GwtIntrospector" );

        return composerFactory.createSourceWriter( context, printWriter );
    }

    /**
     * @param logger
     * @param w
     * @param typeOracle
     */
    private void write( TreeLogger logger, SourceWriter w, JClassType type )
    {
        Collection<Property> properties = lookupJavaBeanPropertyAccessors( type );

        w.println( "public static void setupBeanInfo()" );
        w.println( "{" );
        w.indent();
        w.println( "GwtBeanInfo beanInfo = new GwtBeanInfo();");
        int i = properties.size();
        for ( Property property : properties )
        {
            w.print( "beanInfo.addPropertyDescriptor( " );
            writePropertyDescriptor( w, type, property.name, property.getter, property.setter );
            w.println( " );" );
        }
        w.println( "GwtIntrospector.setBeanInfo( " + type.getName() + ".class, beanInfo );" );
        w.outdent();
        w.println( "}" );
    }

    /**
     * @param sw
     * @param type
     * @param propertyName
     * @param getter
     * @param setter
     */
    private void writePropertyDescriptor( SourceWriter sw, JClassType type, String propertyName, JMethod getter,
                                          JMethod setter )
    {
        sw.print( "new PropertyDescriptor( \"" + propertyName + "\", " );
        if ( getter != null )
        {
            sw.println( "new Method() " );
            sw.println( "{" );
            sw.indent();
            sw.println( "public Object invoke( Object bean, Object... args )" );
            sw.println( "{" );
            sw.indent();
            sw.println( "return ( (" + type.getName() + ") bean)." + getter.getName() + "();" );
            sw.outdent();
            sw.println( "}" );
            sw.outdent();
            sw.print( "}, " );
        }
        else
        {
            sw.print( "null, " );
        }
        if ( setter != null )
        {
            sw.println( "new Method() " );
            sw.println( "{" );
            sw.indent();
            sw.println( "public Object invoke( Object bean, Object... args )" );
            sw.println( "{" );
            sw.indent();
            JType argType = setter.getParameters()[0].getType();
            sw.println( "( (" + type.getName() + ") bean)." + setter.getName() + "( (" + argType.getQualifiedSourceName() + ") args[0] );" );
            sw.println( "return null" );
            sw.outdent();
            sw.println( "}" );
            sw.outdent();
            sw.print( "} )" );
        }
        else
        {
            sw.print( "null )" );
        }
    }

    /**
     * Lookup any public method in the type to match JavaBeans accessor convention
     * @param type
     * @return Collection of javabean properties
     */
    protected Collection<Property> lookupJavaBeanPropertyAccessors( JClassType type )
    {
        Map<String, Property> properties = new HashMap<String, Property>();

        JMethod[] methods = type.getMethods();
        for ( JMethod method : methods )
        {
            if ( ! method.isPublic() || method.isStatic()  )
            {
                continue;
            }
            if ( method.getName().startsWith( "set" ) && method.getParameters().length == 1 )
            {
                String name = Introspector.decapitalize( method.getName().substring( 3 ) );
                Property property = properties.get( name );
                if ( property == null )
                {
                    property = new Property( name );
                    properties.put( name, property );
                }
                property.setter = method;
            }
            else if ( method.getName().startsWith( "get" ) && method.getParameters().length == 0 )
            {
                String name = Introspector.decapitalize( method.getName().substring( 3 ) );
                Property property = properties.get( name );
                if ( property == null )
                {
                    property = new Property( name );
                    properties.put( name, property );
                }
                property.getter = method;
            }
            else if ( method.getName().startsWith( "is" ) && method.getParameters().length == 0 )
            {
                String name = Introspector.decapitalize( method.getName().substring( 2 ) );
                Property property = properties.get( name );
                if ( property == null )
                {
                    property = new Property( name );
                    properties.put( name, property );
                }
                property.getter = method;
            }
        }
        return properties.values();
    }

    private class Property
    {
        public String name;
        public JMethod getter;
        public JMethod setter;

        public Property( String name )
        {
            super();
            this.name = name;
        }
    }
}
