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
import com.google.gwt.core.ext.typeinfo.JParameter;
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
        composerFactory.addImport( "com.googlecode.gwtx.java.introspection.client.GwtBeanInfo" );
        composerFactory.addImport( "com.googlecode.gwtx.java.introspection.client.GwtIntrospector" );

        return composerFactory.createSourceWriter( context, printWriter );
    }

    /**
     * @param logger
     * @param w
     * @param typeOracle
     */
    private void write( TreeLogger logger, SourceWriter w, JClassType type )
    {
        Collection<Property> properties = lookupJavaBeanPropertyAccessors( logger, type );

        w.println( "// automatically register BeanInfos for bean properties" );
        w.println( "static {" );
        w.indent();
        w.println( "GwtBeanInfo beanInfo = new GwtBeanInfo();");
        for ( Property property : properties )
        {
            w.println("try {");
            w.indent();
            w.print( "beanInfo.addPropertyDescriptor( " );
            writePropertyDescriptor( w, type, property.name, property.propertyType, property.getter, property.setter );
            w.println( " );" );
            w.outdent();
            w.println("} catch (Exception e) {}");
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
    private void writePropertyDescriptor( SourceWriter sw, JClassType type, String propertyName, String propertyType,
                                          JMethod getter, JMethod setter )
    {
        sw.print( "new PropertyDescriptor( \"" + propertyName + "\", " +  propertyType + ".class, " );
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
            JType argType = setter.getParameters()[0].getType().getErasedType();
            String argTypeName ;
            if (argType.isPrimitive() != null)
            {
                argTypeName = argType.isPrimitive().getQualifiedBoxedSourceName();
            }
            else
            {
                argTypeName = argType.getQualifiedSourceName();
            }
            sw.println( "( (" + type.getName() + ") bean)." + setter.getName() + "( (" + argTypeName + ") args[0] );" );
            sw.println( "return null;" );
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
    protected Collection<Property> lookupJavaBeanPropertyAccessors( TreeLogger logger, JClassType type )
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
                String propertyType = null;
                JParameter[] parameters = method.getParameters();
                if ( parameters.length == 1 )
                {
                    JParameter parameter = parameters[0];
                    propertyType =
                        parameter.getType().getErasedType().getQualifiedSourceName();
                }
                else
                {
                    logger.log( Type.WARN, "Property '" + name + "' has "
                        + parameters.length + " parameters: " + parameters + "!" );
                    continue;
                }
                Property property = properties.get( name );
                if ( property == null )
                {
                    property = new Property( name );
                    properties.put( name, property );
                }
                property.setter = method;
                if ( property.propertyType == null )
                {
                    property.propertyType = propertyType;
                }
                else if ( ! property.propertyType.equals( propertyType ) )
                {
                    logger.log(Type.WARN, "Property '" + name
                        + "' has an invalid setter: " + propertyType + " was expected, "
                        + property.propertyType + " found!");
                    continue;
                }
            }
            else if ( method.getName().startsWith( "get" ) && method.getParameters().length == 0 )
            {
                String name = Introspector.decapitalize( method.getName().substring( 3 ) );
                String propertyType =
                    method.getReturnType().getErasedType().getQualifiedSourceName();
                Property property = properties.get( name );
                if ( property == null )
                {
                    property = new Property( name );
                    properties.put( name, property );
                }
                property.getter = method;
                if ( property.propertyType == null )
                {
                    property.propertyType = propertyType;
                }
                else if ( ! property.propertyType.equals( propertyType ) )
                {
                    logger.log(Type.WARN, "Property '" + name
                        + "' has an invalid getter: " + propertyType + " was expected, "
                        + property.propertyType + " found!");
                    continue;
                }
            }
            else if ( method.getName().startsWith( "is" ) && method.getParameters().length == 0 )
            {
                String name = Introspector.decapitalize( method.getName().substring( 2 ) );
                String propertyType =
                    method.getReturnType().getErasedType().getQualifiedSourceName();
                Property property = properties.get( name );
                if ( property == null )
                {
                    property = new Property( name );
                    properties.put( name, property );
                }
                property.getter = method;
                if ( property.propertyType == null )
                {
                    property.propertyType = propertyType;
                }
                else if ( ! property.propertyType.equals( propertyType ) )
                {
                    logger.log( Type.WARN, "Property '" + name
                        + "' has an invalid 'is' getter: " + propertyType + " was expected, "
                        + property.propertyType + " found!");
                    continue;
                }
            }
        }
        return properties.values();
    }

    private class Property
    {
        public String name;
        public String propertyType;
        public JMethod getter;
        public JMethod setter;

        public Property( String name )
        {
            super();
            this.name = name;
        }
    }
}
