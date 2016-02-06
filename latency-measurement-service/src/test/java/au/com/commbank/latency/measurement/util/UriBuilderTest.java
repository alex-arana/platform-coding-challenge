package au.com.commbank.latency.measurement.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.net.URI;
import org.junit.Test;


/**
 * Unit test for {@link UriBuilder}.
 */
public class UriBuilderTest {

    /**
     * Unit test case for {@link UriBuilder#create(String)}.
     */
    @Test
    public void testCreateFromSchemalessSpec() {
        URI uri = UriBuilder.create("www.commbank.com.au");
        assertThat(uri, notNullValue());
        assertThat(uri.toString(), equalTo("http://www.commbank.com.au"));

        uri = UriBuilder.create("http://www.google.com");
        assertThat(uri, notNullValue());
        assertThat(uri.toString(), equalTo("https://www.google.com"));

        uri = UriBuilder.create("http://projects.spring.io/spring-xd/");
        assertThat(uri, notNullValue());
        assertThat(uri.toString(), equalTo("http://projects.spring.io/spring-xd/"));

        uri = UriBuilder.create("www.xkcd.com/443?foo=bar");
        assertThat(uri, notNullValue());
        assertThat(uri.toString(), equalTo("http://www.xkcd.com/443?foo=bar"));

        uri = UriBuilder.create("www.facebook.com:443");
        assertThat(uri, notNullValue());
        assertThat(uri.toString(), equalTo("https://www.facebook.com:443"));
    }

    /**
     * Unit test case for {@link UriBuilder#create(String)}, negative scenario.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateUriWithBlankSpec() {
        UriBuilder.create("   ");
    }
}
