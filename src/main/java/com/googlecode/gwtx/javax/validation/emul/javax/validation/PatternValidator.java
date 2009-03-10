package javax.validation;

/**
 * <pre>--
 * As soon as the classes in javax.validation are available from official sites, this
 * class will be removed from this compilation unit.
 * --</pre>
 */
public class PatternValidator implements Constraint<PatternImpl> {

    private String regexp;
    private int flags;

    public void initialize(PatternImpl params) {
        this.regexp = params.regex();
        this.flags = params.flags();
    }

    public boolean isValid(Object ovalue) {
        return isValid((String) ovalue);
    }

    private native boolean isValid(String s); /*-{
        return new RegExp(regexp).test(s);
    }-*/;
}
