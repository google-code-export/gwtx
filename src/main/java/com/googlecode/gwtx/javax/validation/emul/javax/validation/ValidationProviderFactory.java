package javax.validation;


/**
 * --
 * This class is NOT part of the bean_validation spec and might not be supported
 * as soon as the specification contains detailed information about "Bootstrapping".
 * --
 *
 * Description: Simple Factory using JDK Service discovery mechanism
 * to discover the ValidationProvider.
 * <br/>
 * User: roman.stumm <br/>
 * Date: 02.04.2008 <br/>
 * Time: 09:44:12 <br/>
 *
 */
public class ValidationProviderFactory {
    private static ValidationProvider provider;

    public static ValidationProvider getProvider() {
        return provider;
    }

    public static void setProvider(ValidationProvider provider) {
        ValidationProviderFactory.provider = provider;
    }

    /** Validator object factory method - */
    public static Validator createValidator(Class aBeanClass) {
        return getProvider().createValidator(aBeanClass);
    }
}
