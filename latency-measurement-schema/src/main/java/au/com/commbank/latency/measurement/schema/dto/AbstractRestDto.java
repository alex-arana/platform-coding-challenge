package au.com.commbank.latency.measurement.schema.dto;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * The superclass of all of the REST data transfer objects.
 */
public abstract class AbstractRestDto implements Serializable {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = -3502419262656774769L;

    /**
     * Default constructor for instances of <code>AbstractRestDto</code>.
     */
    public AbstractRestDto() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
