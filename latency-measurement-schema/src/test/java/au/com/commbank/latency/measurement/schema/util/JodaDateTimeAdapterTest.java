package au.com.commbank.latency.measurement.schema.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for {@link JodaDateTimeAdapter}.
 */
public class JodaDateTimeAdapterTest {
    private JodaDateTimeAdapter adapter;

    @Before
    public void setUp() {
        adapter = new JodaDateTimeAdapter();
    }

    /**
     * Test method for {@link JodaDateTimeAdapter#unmarshal(String)}.
     */
    @Test
    public void testUnmarshalString() {
        final DateTime dateTime = adapter.unmarshal("1997-07-16T19:20:30.450+10:00");
        assertThat(dateTime, notNullValue());
        assertThat(dateTime.getYear(), is(1997));
        assertThat(dateTime.getMonthOfYear(), is(7));
        assertThat(dateTime.getDayOfMonth(), is(16));
        assertThat(dateTime.getHourOfDay(), is(19));
        assertThat(dateTime.getMinuteOfHour(), is(20));
        assertThat(dateTime.getSecondOfMinute(), is(30));
        assertThat(dateTime.getMillisOfSecond(), is(450));
    }

    /**
     * Test method for {@link JodaDateTimeAdapter#marshal(DateTime)}.
     */
    @Test
    public void testMarshalDateTime() {
        final DateTime dateTime = new DateTime(1997, 7, 16, 19, 20, 30, 450);
        final String dateString = adapter.marshal(dateTime);
        assertFalse("date String was empty or null", StringUtils.isBlank(dateString));
        assertThat(dateString, is("1997-07-16T19:20:30.450+10:00"));
    }

    /**
     * Test method for {@link JodaDateTimeAdapter#parseDateTime(String)}.
     */
    @Test
    public void testParseDate() {
        final DateTime dateTime = JodaDateTimeAdapter.parseDate("1997-07-16T19:20:30.450+10:00");
        assertThat(dateTime, notNullValue());
        assertThat(dateTime.getYear(), is(1997));
        assertThat(dateTime.getMonthOfYear(), is(7));
        assertThat(dateTime.getDayOfMonth(), is(16));
        assertThat(dateTime.getHourOfDay(), is(0));
        assertThat(dateTime.getMinuteOfHour(), is(0));
        assertThat(dateTime.getSecondOfMinute(), is(0));
        assertThat(dateTime.getMillisOfSecond(), is(0));

        for (final String string : new String[] {null, "", "  "}) {
            assertThat(JodaDateTimeAdapter.parseDate(string), nullValue());
        }
    }

    /**
     * Test method for {@link JodaDateTimeAdapter#parseDate(String)}.
     */
    @Test
    public void testParseDateTime() {
        final DateTime dateTime = JodaDateTimeAdapter.parseDateTime("1997-07-16T19:20:30.450+10:00");
        assertThat(dateTime, notNullValue());
        assertThat(dateTime.getYear(), is(1997));
        assertThat(dateTime.getMonthOfYear(), is(7));
        assertThat(dateTime.getDayOfMonth(), is(16));
        assertThat(dateTime.getHourOfDay(), is(19));
        assertThat(dateTime.getMinuteOfHour(), is(20));
        assertThat(dateTime.getSecondOfMinute(), is(30));
        assertThat(dateTime.getMillisOfSecond(), is(450));

        for (final String string : new String[] {null, "", "  "}) {
            assertThat(JodaDateTimeAdapter.parseDateTime(string), nullValue());
        }
    }

    /**
     * Test method for {@link JodaDateTimeAdapter#printDate(DateTime)}.
     */
    @Test
    public void testPrintDate() {
        final DateTime dateTime = new DateTime(1997, 7, 16, 19, 20, 30, 450);
        final String dateString = JodaDateTimeAdapter.printDate(dateTime);
        assertFalse("date String was empty or null", StringUtils.isBlank(dateString));
        assertThat(dateString, is("1997-07-16T00:00:00.000+10:00"));
        assertThat(JodaDateTimeAdapter.printDate(null), nullValue());
    }

    /**
     * Test method for {@link JodaDateTimeAdapter#printDateTime(DateTime)}.
     */
    @Test
    public void testPrintDateTime() {
        final DateTime dateTime = new DateTime(1997, 7, 16, 19, 20, 30, 450);
        final String dateString = JodaDateTimeAdapter.printDateTime(dateTime);
        assertFalse("date String was empty or null", StringUtils.isBlank(dateString));
        assertThat(dateString, is("1997-07-16T19:20:30.450+10:00"));
        assertThat(JodaDateTimeAdapter.printDateTime(null), nullValue());
    }
}
