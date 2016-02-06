package au.com.commbank.latency.measurement.util;

import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import com.google.common.collect.Iterables;


/**
 * Utility class containing helper methods that deal with Collections and Arrays.
 */
public final class CollectionHelpers {

    /**
     * Prevent public instantiation of this utility class.
     */
    private CollectionHelpers() {

    }

    /**
     * Converts the specified {@link List} of String elements into an array of {@link String}s
     * wrapped within an {@link Optional} reference.
     *
     * @param <T> type of element that the output array must contain
     * @param list A list of String elements. It can be <code>null</code> or empty
     * @param elementType type of element that the output array must contain. Can be an interface or
     *        superclass of the actual class
     * @return An array of Strings wrapped within an Optional reference which can be absent
     */
    public static <T> Optional<T[]> listToOptionalArray(final List<? extends T> list, final Class<T> elementType) {
        return CollectionUtils.isEmpty(list)
            ? Optional.<T[]>empty() : Optional.of(Iterables.toArray(list, elementType));
    }
}
