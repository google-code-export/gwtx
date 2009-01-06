package javax.validation;

/**
 * <pre>--
 * As soon as the classes in javax.validation are available from official sites, this
 * class will be removed from this compilation unit.
 *
 * The edr-spec 1.0 is unclear here, how to call the interface-method (getStandardConstraints())
 * --</pre>
 *
 * Indicates the availability of standard constraint metadata.
 */
public interface StandardConstraint {
    /**
     *
     * @return A StandardConstraintDescriptor containing metadata
     *         for this constraint definition.
     */
    StandardConstraintDescriptor getStandardConstraintDescriptor();
}