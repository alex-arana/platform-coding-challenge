package au.com.commbank.latency.measurement.util;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;


/**
 * The PerformanceStopwatch class is used to time code blocks in application code.
 * <p>
 * The general usage pattern is to create a PerformanceStopwatch before a section of code that is to be timed and
 * then stop it before it is stopped and its elapsed time fetched.
 * <pre>
 * PerformanceStopwatch stopWatch = new PerformanceStopwatch(...);
 * try {
 *     ...code being timed...
 * } finally {
 *     stopwatch.stop();
 *     stopwatch.getElapsedTime(); // do something with this value..
 * }
 * </pre>
 */
public class PerformanceStopwatch implements Serializable {
    private static final long serialVersionUID = 1L;

    private long startTime;
    private long nanoStartTime;
    private long elapsedTime;
    private State state = State.UNSTARTED;

    public PerformanceStopwatch(final boolean autostart) {
        if (autostart) {
            start();
        }
    }

    /**
     * Gets the time when this instance was created, or when one of the <tt>start()</tt> messages was last called.
     *
     * @return The start time in milliseconds since the epoch.
     */
    public long getStartTime() {
        return state == State.UNSTARTED ? NumberUtils.LONG_MINUS_ONE : startTime;
    }

    /**
     * Gets the time in milliseconds between when this PerformanceStopwatch was last started and stopped.
     * <p>
     * If <tt>stop()</tt> was not called, then the time returned is the time since the PerformanceStopwatch was
     * started.
     *
     * @return The elapsed time in milliseconds.
     */
    public long getElapsedTime() {
        final long result;
        switch (state) {
            case UNSTARTED:
                result = NumberUtils.LONG_MINUS_ONE;
                break;
            case STOPPED:
                result = elapsedTime;
                break;
            default:
                result = getCurrentElapsedTime();
                break;
        }
        return result;
    }

    /**
     * Starts this PerformanceStopwatch, which sets its startTime property to the current time and resets the
     * elapsedTime property.
     * <p>
     * For single-use PerformanceStopwatch instance you should not need to call this method as a
     * PerformanceStopwatch is automatically started when it is created.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        nanoStartTime = System.nanoTime();
        state = State.RUNNING;
    }

    /**
     * Stops this PerformanceStopwatch, which "freezes" its elapsed time. You should normally call this method (or
     * the stop method) before passing this instance to the performance statistics collection service.
     */
    public void stop() {
        elapsedTime = getCurrentElapsedTime();
        state = State.STOPPED;
    }

    private long getCurrentElapsedTime() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - nanoStartTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
            .append("startTime", new DateTime(startTime))
            .append("elapsedTime", elapsedTime)
            .toString();
    }

    enum State {
        UNSTARTED, RUNNING, STOPPED;
    }
}
