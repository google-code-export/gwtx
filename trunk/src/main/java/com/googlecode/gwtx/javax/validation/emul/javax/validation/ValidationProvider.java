package javax.validation;

/**
 * --
 * This interface is NOT part of the bean_validation spec and might not be supported
 * as soon as the specification contains detailed information about "Bootstrapping".
 * --
 *
 * Description: validator object factory<br/>
 * User: roman.stumm <br/>
 * Date: 02.04.2008 <br/>
 * Time: 09:39:36 <br/>
 *
 */
public interface ValidationProvider {
    /**
     * factory method - 
     * @param aBeanClass
     * @return
     */
    Validator createValidator(Class aBeanClass);
}
