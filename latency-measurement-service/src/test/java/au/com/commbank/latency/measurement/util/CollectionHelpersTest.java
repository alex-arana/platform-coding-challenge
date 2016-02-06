package au.com.commbank.latency.measurement.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;
import com.google.common.collect.ImmutableList;


/**
 * Unit test case for {@link CollectionHelpers}.
 */
public class CollectionHelpersTest {

    @Test
    public void testConstructor() throws Exception {
        final Constructor<CollectionHelpers> constructor = CollectionHelpers.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        ReflectionUtils.makeAccessible(constructor);
        assertThat(constructor.newInstance(), notNullValue());
    }

    /**
     * Unit test for {@link CollectionHelpers#listToOptionalArray(List, Class)}.
     */
    @Test
    public void testListToOptionalArray() {
        final List<String> list = ImmutableList.of("Foo", "Bar");
        Optional<String[]> optional = CollectionHelpers.listToOptionalArray(list, String.class);
        assertTrue(optional.isPresent());
        assertThat(optional.get(), is(new String[] {"Foo", "Bar"}));

        final ImmutableList<String> emptyList = ImmutableList.of();
        optional = CollectionHelpers.listToOptionalArray(emptyList, String.class);
        assertFalse(optional.isPresent());

        optional = CollectionHelpers.listToOptionalArray(null, String.class);
        assertFalse(optional.isPresent());
    }
}
