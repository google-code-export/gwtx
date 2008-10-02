/*
 * Copyright 2006 Robert Hanson <iamroberthanson AT gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Based on GWT-Widget project
 * https://gwt-widget.svn.sourceforge.net/svnroot/gwt-widget/trunk/src/java/org/gwtwidgets/client/util/
 */

package java.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.googlecode.gwtx.java.text.client.Locale;

public class SimpleDateFormat extends DateFormat {

    private final static String LITERAL = "\\";

    public final static String TOKEN_DAY_OF_WEEK = "E";

    public final static String TOKEN_DAY_OF_MONTH = "d";

    public final static String TOKEN_MONTH = "M";

    public final static String TOKEN_YEAR = "y";

    public final static String TOKEN_HOUR_12 = "h";

    public final static String TOKEN_HOUR_24 = "H";

    public final static String TOKEN_MINUTE = "m";

    public final static String TOKEN_SECOND = "s";

    public final static String TOKEN_MILLISECOND = "S";

    public final static String TOKEN_AM_PM = "a";

    public final static String AM = "AM";

    public final static String PM = "PM";

    public final static List SUPPORTED_DF_TOKENS = Arrays.asList(new String[] {
            TOKEN_DAY_OF_WEEK, TOKEN_DAY_OF_MONTH, TOKEN_MONTH, TOKEN_YEAR,
            TOKEN_HOUR_12, TOKEN_HOUR_24, TOKEN_MINUTE, TOKEN_SECOND,
            TOKEN_AM_PM });

    private Locale locale = GWT.create( Locale.class );

    public String[] MONTH_LONG = {
        locale.Month_Long_January(),
        locale.Month_Long_February(),
        locale.Month_Long_March(),
        locale.Month_Long_April(),
        locale.Month_Long_May(),
        locale.Month_Long_June(),
        locale.Month_Long_July(),
        locale.Month_Long_August(),
        locale.Month_Long_September(),
        locale.Month_Long_October(),
        locale.Month_Long_November(),
        locale.Month_Long_December() };


    public String[] MONTH_SHORT = {
        locale.Month_Short_January(),
        locale.Month_Short_February(),
        locale.Month_Short_March(),
        locale.Month_Short_April(),
        locale.Month_Short_May(),
        locale.Month_Short_June(),
        locale.Month_Short_July(),
        locale.Month_Short_August(),
        locale.Month_Short_September(),
        locale.Month_Short_October(),
        locale.Month_Short_November(),
        locale.Month_Short_December() };

    public String[] WEEKDAY_LONG = {
        locale.Weekday_Long_Sunday(),
        locale.Weekday_Long_Monday(),
        locale.Weekday_Long_Tuesday(),
        locale.Weekday_Long_Wednesday(),
        locale.Weekday_Long_Thursday(),
        locale.Weekday_Long_Friday(),
        locale.Weekday_Long_Saturday() };

    public String[] WEEKDAY_SHORT = {
        locale.Weekday_Short_Sunday(),
        locale.Weekday_Short_Monday(),
        locale.Weekday_Short_Tuesday(),
        locale.Weekday_Short_Wednesday(),
        locale.Weekday_Short_Thursday(),
        locale.Weekday_Short_Friday(),
        locale.Weekday_Short_Saturday() };


    private String format;

    public SimpleDateFormat(String pattern) {
        format = pattern;
    }

    public String format(Date date) {
        String f = "";
        if (format != null && format.length() > 0) {
            String lastTokenType = null;
            String currentToken = "";
            for (int i = 0; i < format.length(); i++) {
                String thisChar = format.substring(i, i + 1);
                String currentTokenType = SUPPORTED_DF_TOKENS
                        .contains(thisChar) ? thisChar : "";
                if (currentTokenType.equals(lastTokenType) || i == 0) {
                    currentToken += thisChar;
                    lastTokenType = currentTokenType;
                } else {
                    if ("".equals(lastTokenType))
                        f += currentToken;
                    else
                        f += handleToken(currentToken, date);
                    currentToken = thisChar;
                    lastTokenType = currentTokenType;
                }
            }
            if ("".equals(lastTokenType))
                f += currentToken;
            else
                f += handleToken(currentToken, date);
        }
        return f;
    }

    /**
     * takes a date format string and returns the formatted portion of the date.
     * For instance if the token is MMMM then the full month name is returned.
     *
     * @param token
     *            date format token
     * @param date
     *            date to format
     * @return formatted portion of the date
     */
    private String handleToken(String token, Date date) {
        String response = token;
        String tc = token.substring(0, 1);
        if (TOKEN_DAY_OF_WEEK.equals(tc)) {
            if (token.length() > 3)
                response = WEEKDAY_LONG[date.getDay()];
            else
                response = WEEKDAY_SHORT[date.getDay()];
        } else if (TOKEN_DAY_OF_MONTH.equals(tc)) {
            if (token.length() == 1)
                response = Integer.toString(date.getDate());
            else
                response = twoCharDateField(date.getDate());
        } else if (TOKEN_MONTH.equals(tc)) {
            switch (token.length()) {
            case 1:
                response = Integer.toString(date.getMonth() + 1);
                break;
            case 2:
                response = twoCharDateField(date.getMonth() + 1);
                break;
            case 3:
                response = MONTH_SHORT[date.getMonth()];
                break;
            default:
                response = MONTH_LONG[date.getMonth()];
                break;
            }
        } else if (TOKEN_YEAR.equals(tc)) {
            if (token.length() > 2)
                response = Integer.toString(date.getYear() + 1900);
            else
                response = twoCharDateField(date.getYear());
        } else if (TOKEN_HOUR_12.equals(tc)) {
            int h = date.getHours();
            if (h == 0)
                h = 12;
            else if (h > 12)
                h -= 12;
            if (token.length() > 1)
                response = twoCharDateField(h);
            else
                response = Integer.toString(h);
        } else if (TOKEN_HOUR_24.equals(tc)) {
            if (token.length() > 1)
                response = twoCharDateField(date.getHours());
            else
                response = Integer.toString(date.getHours());
        } else if (TOKEN_MINUTE.equals(tc)) {
            if (token.length() > 1)
                response = twoCharDateField(date.getMinutes());
            else
                response = Integer.toString(date.getMinutes());
        } else if (TOKEN_SECOND.equals(tc)) {
            if (token.length() > 1)
                response = twoCharDateField(date.getSeconds());
            else
                response = Integer.toString(date.getSeconds());
        } else if (TOKEN_AM_PM.equals(tc)) {
            int hour = date.getHours();
            if (hour > 11)
                response = PM;
            else
                response = AM;
        }
        return response;
    }

    /**
     * This is basically just a sneaky way to guarantee that our 1 or 2 digit
     * numbers come out as a 2 character string. we add an arbitrary number
     * larger than 100, convert this new number to a string, then take the right
     * most 2 characters.
     *
     * @param num
     * @return
     */
    private String twoCharDateField(int num) {
        String res = Integer.toString(num + 1900);
        res = res.substring(res.length() - 2);
        return res;
    }

    private static Date newDate(long time) {
        return new Date(time);
    }

    /**
     * Parses text and returns the corresponding date object.
     *
     * @param source
     * @return java.util.Date
     */
    public Date parse(String source){
        return SimpleDateParser.parse(source, format);
    };

    private static class SimpleDateParser {

        private final static String DAY_IN_MONTH = "d";

        private final static String MONTH = "M";

        private final static String YEAR = "y";

        private final static String LITERAL = "\\";

        private final static int DATE_PATTERN = 0;

        private final static int REGEX_PATTERN = 1;

        private final static int COMPONENT = 2;

        private final static int REGEX = 0;

        private final static int INSTRUCTION = 1;

        private final static String[] TOKENS[] = {
        { "SSSS", "(\\d\\d\\d\\d)",TOKEN_MILLISECOND },
        { "SSS", "(\\d\\d\\d)", TOKEN_MILLISECOND },
        { "SS", "(\\d\\d)", TOKEN_MILLISECOND },
        { "S", "(\\d)", TOKEN_MILLISECOND },
        { "ss", "(\\d\\d)", TOKEN_SECOND },
        { "s", "(\\d)", TOKEN_SECOND },
        { "mm", "(\\d\\d)", TOKEN_MINUTE },
        { "m", "(\\d)", TOKEN_MINUTE},
        { "HH", "(\\d\\d)", TOKEN_HOUR_24},
        { "H", "(\\d)", TOKEN_HOUR_24 },
        { "dd", "(\\d\\d)", TOKEN_DAY_OF_MONTH },
        { "d", "(\\d)", TOKEN_DAY_OF_MONTH },
        { "MM", "(\\d\\d)", TOKEN_MONTH },
        { "M", "(\\d)", TOKEN_MONTH },
        { "yyyy", "(\\d\\d\\d\\d)", TOKEN_YEAR },
        { "yyy", "(\\d\\d\\d)", TOKEN_YEAR },
        { "yy", "(\\d\\d)", TOKEN_YEAR },
        { "y", "(\\d)", TOKEN_YEAR }
        };

        private Pattern regularExpression;

        private String instructions = "";

        private static void _parse(String format, String[] args) {
            if (format.length() == 0)
                return;
            if (format.startsWith("'")){
                format = format.substring(1);
                int end = format.indexOf("'");
                if (end == -1)
                    throw new IllegalArgumentException("Unmatched single quotes.");
                args[REGEX]+=Pattern.quote(format.substring(0,end));
                format = format.substring(end+1);
            }
            for (int i = 0; i < TOKENS.length; i++) {
                String[] row = TOKENS[i];
                String datePattern = row[DATE_PATTERN];
                if (!format.startsWith(datePattern))
                    continue;
                format = format.substring(datePattern.length());
                args[REGEX] += row[REGEX_PATTERN];
                args[INSTRUCTION] += row[COMPONENT];
                _parse(format, args);
                return;
            }
            args[REGEX] += Pattern.quote(""+format.charAt(0));
            format = format.substring(1);
            _parse(format, args);
        }

        private static void load(Date date, String text, String component) {
            if (component.equals(TOKEN_MILLISECOND)) {
                //TODO: implement
            }

            if (component.equals(TOKEN_SECOND)) {
                date.setSeconds(Integer.parseInt(text));
            }

            if (component.equals(TOKEN_MINUTE)) {
                date.setMinutes(Integer.parseInt(text));
            }

            if (component.equals(TOKEN_HOUR_24)) {
                date.setHours(Integer.parseInt(text));
            }

            if (component.equals(TOKEN_DAY_OF_MONTH)) {
                date.setDate(Integer.parseInt(text));
            }
            if (component.equals(TOKEN_MONTH)) {
                date.setMonth(Integer.parseInt(text)-1);
            }
            if (component.equals(TOKEN_YEAR)) {
                //TODO: fix for short patterns
                date.setYear(Integer.parseInt(text)-1900);
            }

        }

        public SimpleDateParser(String format) {
            String[] args = new String[] { "", "" };
            _parse(format, args);
            regularExpression = new Pattern(args[REGEX]);
            instructions = args[INSTRUCTION];
        }

        public Date parse(String input) {
            Date date = new Date(0, 0, 1, 0, 0, 0);
            String matches[] = regularExpression.match(input);
            if (matches == null)
                throw new IllegalArgumentException(input+" does not match "+regularExpression.pattern());
            if (matches.length-1!=instructions.length())
                throw new IllegalArgumentException("Different group count - "+input+" does not match "+regularExpression.pattern());
            for (int group = 0; group < instructions.length(); group++) {
                String match = matches[group + 1];
                load(date, match, ""+instructions.charAt(group));
            }
            return date;
        }

        public static Date parse(String input, String pattern){
            return new SimpleDateParser(pattern).parse(input);
        }
    }

    // Candidate to emulate java.util.regexp ???
    private static class Pattern {

        /**
         * Declares that regular expressions should be matched across line borders.
         */
        public final static int MULTILINE = 1;

        /**
         * Declares that characters are matched reglardless of case.
         */
        public final static int CASE_INSENSITIVE = 2;

        private JavaScriptObject regExp;

        private static JavaScriptObject createExpression(String pattern, int flags) {
            String sFlags = "";
            if ((flags & MULTILINE) != 0)
                sFlags += "m";
            if ((flags & CASE_INSENSITIVE) != 0)
                sFlags += "i";
            return _createExpression(pattern, sFlags);
        }

        private static native JavaScriptObject _createExpression(String pattern,
                String flags)/*-{
         return new RegExp(pattern, flags);
         }-*/;

        private native void _match(String text, List matches)/*-{
         var regExp = this.@org.gwtwidgets.client.util.regex.Pattern::regExp;
         var result = text.match(regExp);
         if (result == null) return;
         for (var i=0;i<result.length;i++)
         matches.@java.util.ArrayList::add(Ljava/lang/Object;)(result[i]);
         }-*/;

        /**
         * Determines wether the specified regular expression is validated by the
         * provided input.
         * @param regex Regular expression
         * @param input String to validate
         * @return <code>true</code> if matched.
         */
        public static boolean matches(String regex, String input) {
            return new Pattern(regex).matches(input);
        }

        /**
         * Escape a provided string so that it will be interpreted as a literal
         * in regular expressions.
         * The current implementation does escape each character even if not neccessary,
         * generating verbose literals.
         * @param input
         * @return
         */
        public static String quote(String input) {
            String output = "";
            for (int i = 0; i < input.length(); i++) {
                output += "\\" + input.charAt(i);
            }
            return output;
        }

        /**
         * Class constructor
         * @param pattern Regular expression
         */
        public Pattern(String pattern) {
            this(pattern, 0);
        }

        /**
         * Class constructor
         * @param pattern Regular expression
         * @param flags
         */
        public Pattern(String pattern, int flags) {
            regExp = createExpression(pattern, flags);
        }

        /**
         * This method is borrowed from the JavaScript RegExp object.
         * It parses a string and returns as an array any assignments to parenthesis groups
         * in the pattern's regular expression
         * @param text
         * @return Array of strings following java's Pattern convention for groups:
         * Group 0 is the entire input string and the remaining groups are the matched parenthesis.
         * In case nothing was matched an empty array is returned.
         */
        public String[] match(String text) {
            List matches = new ArrayList();
            _match(text, matches);
            String arr[] = new String[matches.size()];
            for (int i = 0; i < matches.size(); i++)
                arr[i] = matches.get(i).toString();
            return arr;
        }

        /**
         * Determines wether a provided text matches the regular expression
         * @param text
         * @return
         */
        public native boolean matches(String text)/*-{
         var regExp = this.@org.gwtwidgets.client.util.regex.Pattern::regExp;
         return regExp.test(text);
         }-*/;

        /**
         * Returns the regular expression for this pattern
         * @return
         */
        public native String pattern()/*-{
         var regExp = this.@org.gwtwidgets.client.util.regex.Pattern::regExp;
         return regExp.source;
         }-*/;

        private native void _split(String input, List results)/*-{
         var regExp = this.@org.gwtwidgets.client.util.regex.Pattern::regExp;
         var parts = input.split(regExp);
         for (var i=0;i<parts.length;i++)
         results.@java.util.ArrayList::add(Ljava/lang/Object;)(parts[i] );
         }-*/;

        /**
         * Split an input string by the pattern's regular expression
         * @param input
         * @return Array of strings
         */
        public String[] split(String input){
            List results = new ArrayList();
            _split(input, results);
            String[] parts = new String[results.size()];
            for (int i=0;i<results.size();i++)
                parts[i] = (String)results.get(i);
            return parts;
        }

        public String toString() {
            return regExp.toString();
        }
    }

}
