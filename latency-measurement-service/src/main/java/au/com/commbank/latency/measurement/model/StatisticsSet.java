package au.com.commbank.latency.measurement.model;

import java.io.Serializable;
import java.util.Optional;
import org.apache.commons.lang3.math.NumberUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;


/**
 * Represents a set of recorded statistics data that can be queried for the aggregated totals.
 */
public class StatisticsSet implements Serializable {
    private static final long serialVersionUID = 6288497909848657922L;

    private Long runningTotal = NumberUtils.LONG_ZERO;

    /**
     * Use a a sorted collection which accepts duplicates to store all latency values.
     */
    private final SortedMultiset<Long> values = TreeMultiset.create();

    public boolean add(final Long value) {
        runningTotal += value;
        return values.add(value);
    }

    public int size() {
        return values.size();
    }

    public Optional<Long> getMin() {
        if (values.size() > 0) {
            return Optional.of(values.firstEntry().getElement());
        }
        return Optional.empty();
    }

    public Optional<Long> getMax() {
        if (values.size() > 0) {
            return Optional.of(values.lastEntry().getElement());
        }
        return Optional.empty();
    }

    public Optional<Double> getMedian() {
        final int size = values.size();
        if (size > 1) {
            final Long mid = Iterables.get(values, (size - 1) / 2);
            if ((size & 1) == 1) {  // odd number
                return Optional.of((double) mid);
            } else {  // even number of values will return the average of the two middle elements
                final Long mid2 = Iterables.get(values, (size + 1) / 2);
                return Optional.of((mid + mid2) / 2.0);
            }
        }
        return Optional.empty();
    }

    public Optional<Double> getAverage() {
        final int size = values.size();
        if (size > 0) {
            return Optional.of((double) runningTotal / (double) size);
        }
        return Optional.empty();
    }

    /**
     * Returns the difference between the largest number in the data set and smallest number in the data
     * set.
     * <p>
     * If the data set does not contain at least two values this method return Optional.empty().
     *
     * @return data range of the stored values within an optional envelope.
     */
    public Optional<Long> getRange() {
        if (values.size() > 1) {
            return Optional.of(values.lastEntry().getElement() - values.firstEntry().getElement());
        }
        return Optional.empty();
    }

    public Long getRunningTotal() {
        return runningTotal;
    }

    public Optional<Double> getStdDev() {
        // TODO implement this method
        return Optional.empty();
    }

    public SortedMultiset<Long> values() {
        return values;
    }
}
