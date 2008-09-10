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

/*
 * This file is based on code from the Apache Harmony Project.
 * http://svn.apache.org/repos/asf/harmony/enhanced/classlib/trunk/modules/luni/src/main/java/java/util/Locale.java
 */

package java.util;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;


/**
 * Locale represents a language/country/variant combination. It is an identifier
 * which dictates particular conventions for the presentation of information.
 * The language codes are two letter lowercase codes as defined by ISO-639. The
 * country codes are three letter uppercase codes as defined by ISO-3166. The
 * variant codes are unspecified.
 *
 * @see ResourceBundle
 */
public final class Locale implements Cloneable, Serializable {

    private static volatile Locale[] availableLocales;

    // Initialize a default which is used during static
    // initialization of the default for the platform.
    private static Locale defaultLocale = new Locale();

    /**
     * Locale constant for en_CA.
     */
    public static final Locale CANADA = new Locale("en", "CA"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for fr_CA.
     */
    public static final Locale CANADA_FRENCH = new Locale("fr", "CA"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for zh_CN.
     */
    public static final Locale CHINA = new Locale("zh", "CN"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for zh.
     */
    public static final Locale CHINESE = new Locale("zh", ""); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for en.
     */
    public static final Locale ENGLISH = new Locale("en", ""); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for fr_FR.
     */
    public static final Locale FRANCE = new Locale("fr", "FR"); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for fr.
     */
    public static final Locale FRENCH = new Locale("fr", ""); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for de.
     */
    public static final Locale GERMAN = new Locale("de", ""); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for de_DE.
     */
    public static final Locale GERMANY = new Locale("de", "DE"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for it.
     */
    public static final Locale ITALIAN = new Locale("it", ""); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for it_IT.
     */
    public static final Locale ITALY = new Locale("it", "IT"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for ja_JP.
     */
    public static final Locale JAPAN = new Locale("ja", "JP"); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for ja.
     */
    public static final Locale JAPANESE = new Locale("ja", ""); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for ko_KR.
     */
    public static final Locale KOREA = new Locale("ko", "KR"); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for ko.
     */
    public static final Locale KOREAN = new Locale("ko", ""); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for zh_CN.
     */
    public static final Locale PRC = new Locale("zh", "CN"); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for zh_CN.
     */
    public static final Locale SIMPLIFIED_CHINESE = new Locale("zh", "CN"); //$NON-NLS-1$//$NON-NLS-2$

    /**
     * Locale constant for zh_TW.
     */
    public static final Locale TAIWAN = new Locale("zh", "TW"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for zh_TW.
     */
    public static final Locale TRADITIONAL_CHINESE = new Locale("zh", "TW"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for en_GB.
     */
    public static final Locale UK = new Locale("en", "GB"); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * Locale constant for en_US.
     */
    public static final Locale US = new Locale("en", "US"); //$NON-NLS-1$//$NON-NLS-2$


    static {
        defaultLocale = new Locale();
    }

    private transient String countryCode;
    private transient String languageCode;
    private transient String variantCode;

    /**
     * Constructs a default which is used during static initialization of the
     * default for the platform.
     */
    private Locale() {
        languageCode = "en"; //$NON-NLS-1$
        countryCode = "US"; //$NON-NLS-1$
        variantCode = ""; //$NON-NLS-1$
    }

    /**
     * Constructs a new Locale using the specified language.
     *
     * @param language
     *
     */
    public Locale(String language) {
        this(language, "", ""); //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * Constructs a new Locale using the specified language and country codes.
     *
     * @param language
     * @param country
     *
     */
    public Locale(String language, String country) {
        this(language, country, ""); //$NON-NLS-1$
    }

    /**
     * Constructs a new Locale using the specified language, country, and
     * variant codes.
     *
     * @param language
     * @param country
     * @param variant
     * @throws NullPointerException
     *             if <code>language</code>, <code>country</code> or
     *             <code>variant</code> is <code>null</code>.
     */
    public Locale(String language, String country, String variant) {
        if (language == null || country == null || variant == null) {
            throw new NullPointerException();
        }
        if(language.length() == 0 && country.length() == 0){
            languageCode = "";
            countryCode = "";
            variantCode = variant;
            return;
        }
        languageCode = language.toLowerCase();
        // Map new language codes to the obsolete language
        // codes so the correct resource bundles will be used.
        if (languageCode.equals("he")) {//$NON-NLS-1$
            languageCode = "iw"; //$NON-NLS-1$
        } else if (languageCode.equals("id")) {//$NON-NLS-1$
            languageCode = "in"; //$NON-NLS-1$
        } else if (languageCode.equals("yi")) {//$NON-NLS-1$
            languageCode = "ji"; //$NON-NLS-1$
        }

        // countryCode is defined in ASCII character set
        countryCode = country.toLowerCase();

        // Work around for be compatible with RI
        variantCode = variant;
    }

    /**
     * Answers a new Locale with the same language, country and variant codes as
     * this Locale.
     *
     * @return a shallow copy of this Locale
     *
     * @see java.lang.Cloneable
     */
    @Override
    public Object clone() {
        return new Locale( languageCode, countryCode, variantCode );
    }

    /**
     * Compares the specified object to this Locale and answer if they are
     * equal. The object must be an instance of Locale and have the same
     * language, country and variant.
     *
     * @param object
     *            the object to compare with this object
     * @return true if the specified object is equal to this Locale, false
     *         otherwise
     *
     * @see #hashCode
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Locale) {
            Locale o = (Locale) object;
            return languageCode.equals(o.languageCode)
                    && countryCode.equals(o.countryCode)
                    && variantCode.equals(o.variantCode);
        }
        return false;
    }

    /**
     * Gets the country code for this Locale.
     *
     * @return a country code
     */
    public String getCountry() {
        return countryCode;
    }

    /**
     * Gets the default Locale.
     *
     * @return the default Locale
     */
    public static Locale getDefault() {
        return defaultLocale;
    }

    /**
     * Gets the language code for this Locale.
     *
     * @return a language code
     */
    public String getLanguage() {
        return languageCode;
    }

    /**
     * Gets the variant code for this Locale.
     *
     * @return a variant code
     */
    public String getVariant() {
        return variantCode;
    }

    /**
     * Answers an integer hash code for the receiver. Objects which are equal
     * answer the same value for this method.
     *
     * @return the receiver's hash
     *
     * @see #equals
     */
    @Override
    public synchronized int hashCode() {
        return countryCode.hashCode() + languageCode.hashCode()
                + variantCode.hashCode();
    }

    /**
     * Sets the default Locale to the specified Locale.
     *
     * @param locale
     *            the new default Locale
     *
     * @exception SecurityException
     *                when there is a security manager which does not allow this
     *                operation
     */
    public synchronized static void setDefault(Locale locale) {
        if (locale != null) {
            defaultLocale = locale;
        } else {
            throw new NullPointerException();
        }
    }

    /**
     * Answers the string representation of this Locale.
     *
     * @return the string representation of this Locale
     */
    @Override
    public final String toString() {
        StringBuilder result = new StringBuilder();
        result.append(languageCode);
        if (countryCode.length() > 0) {
            result.append('_');
            result.append(countryCode);
        }
        if (variantCode.length() > 0 && result.length() > 0) {
            if (0 == countryCode.length()) {
                result.append("__"); //$NON-NLS-1$
            } else {
                result.append('_');
            }
            result.append(variantCode);
        }
        return result.toString();
    }
}
