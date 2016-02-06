package au.com.commbank.latency.measurement.schema.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


/**
 * JAXB custom type adapter to convert betweeen instances of Joda's {@link DateTime} and their corresponding
 * {@link String} values.
 *
 * The fragment below can be used in a custom bindings file to replace XML date types with Joda's DateTime:
 *
 * <pre>
 *     &lt;jaxb:javaType name="org.joda.time.DateTime" xmlType="xs:dateTime"
 *                    parseMethod="au.com.commbank.latency.measurement.schema.util.JodaDateTimeAdapter.parseDateTime"
 *                    printMethod="au.com.commbank.latency.measurement.schema.util.JodaDateTimeAdapter.printDateTime" /&gt;
 * </pre>
 */
public class JodaDateTimeAdapter extends XmlAdapter<String, DateTime> {
    private static final DateTimeFormatter DATE_PATTERN = ISODateTimeFormat.dateTime();
    private static final JodaDateTimeAdapter INSTANCE = new JodaDateTimeAdapter();

    /**
     * {@inheritDoc}
     */
    @Override
    public DateTime unmarshal(final String string) {
        return DATE_PATTERN.parseDateTime(string);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String marshal(final DateTime dateTime) {
        return DATE_PATTERN.print(dateTime);
    }

    /**
     * Exposes the {@link #unmarshal(String)} instance method as a static helper method to convert a String
     * value to an instance of {@link DateTime} with the time set to the start of the day.
     *
     * @param string the value to be converted (can be null or empty)
     * @return a new {@link DateTime} instance representing the specified String (or null) and with the time
     *         set to the start of the day
     */
    public static DateTime parseDate(final String string) {
        return StringUtils.isBlank(string) ? null : INSTANCE.unmarshal(string).withTimeAtStartOfDay();
    }

    /**
     * Exposes the {@link #marshal(DateTime)} instance method as a static helper method to convert a
     * {@link DateTime} instance to a suitable String representation. Prior to serialising the input date
     * parameter its time is set to the start of the day.
     *
     * @param dateTime the DateTime instance to be converted to a String (can be null or empty)
     * @return a String representation of the specified <code>dateTime</code> (or null)
     */
    public static String printDate(final DateTime dateTime) {
        return dateTime == null ? null : INSTANCE.marshal(dateTime.withTimeAtStartOfDay());
    }

    /**
     * Exposes the {@link #unmarshal(String)} instance method as a static helper method to convert a String
     * value to an instance of {@link DateTime}.
     *
     * @param string the value to be converted (can be null or empty)
     * @return a new {@link DateTime} instance representing the specified String (or null)
     */
    public static DateTime parseDateTime(final String string) {
        return StringUtils.isBlank(string) ? null : INSTANCE.unmarshal(string);
    }

    /**
     * Exposes the {@link #marshal(DateTime)} instance method as a static helper method to convert a
     * {@link DateTime} instance to a suitable String representation.
     *
     * @param dateTime the DateTime instance to be converted to a String (can be null or empty)
     * @return a String representation of the specified <code>dateTime</code> (or null)
     */
    public static String printDateTime(final DateTime dateTime) {
        return dateTime == null ? null : INSTANCE.marshal(dateTime);
    }
}
