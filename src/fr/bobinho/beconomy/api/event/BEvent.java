package fr.bobinho.beconomy.api.event;

import fr.bobinho.beconomy.BEconomyCore;
import fr.bobinho.beconomy.api.scheduler.BScheduler;
import fr.bobinho.beconomy.api.validate.BValidate;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.EventExecutor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Bobinho event library
 */
public final class BEvent<T extends Event> implements Listener, EventExecutor {

    /**
     * Event enum filters.=
     */
    public enum Filter {
        IGNORE_CANCELLED,

        IGNORE_DISALLOWED_LOGIN,
        IGNORE_DISALLOWED_PRE_LOGIN,

        IGNORE_FILTERS_FOR_LIMIT
    }

    //Root event
    private final Class<T> eventClass;
    private EventPriority priority = EventPriority.NORMAL;

    //Filters
    private final Collection<Filter> filters = new ArrayList<>();
    private final Collection<Function<T, Boolean>> functionFilters = new ArrayList<>();

    //Limit
    private int usageLimit;
    private int usage;

    //Expire
    private int expire;
    private TimeUnit expireUnit;
    private int expireTaskId = -1;

    //Consumer
    private Consumer<T> consumer;
    private boolean async;

    //Checks
    private boolean unregistered;

    /**
     * Creates an event
     *
     * @param eventClass the event class
     */
    public BEvent(@Nonnull Class<T> eventClass) {
        BValidate.notNull(eventClass);

        this.eventClass = eventClass;
    }

    /**
     * Creates an event.
     *
     * @param eventClass the event class
     * @param priority   the event priority
     */
    public BEvent(@Nonnull Class<T> eventClass, @Nonnull EventPriority priority) {
        BValidate.notNull(eventClass);
        BValidate.notNull(priority);

        this.eventClass = eventClass;
        this.priority = priority;
    }

    /**
     * Registers a event
     *
     * @param eventClass the bukkit event class
     * @param <T>        the bukkit event
     * @return the event builder
     */
    @Nonnull
    public static <T extends Event> BEvent<T> registerEvent(@Nonnull Class<T> eventClass) {
        BValidate.notNull(eventClass);

        return new BEvent<>(eventClass);
    }

    /**
     * Registers a new event
     *
     * @param eventClass the bukkit event class
     * @param priority   the event priority
     * @param <T>        the bukkit event
     * @return the event builder
     */
    @Nonnull
    public static <T extends Event> BEvent<T> registerEvent(@Nonnull Class<T> eventClass, @Nonnull EventPriority priority) {
        BValidate.notNull(eventClass);
        BValidate.notNull(priority);

        return new BEvent<>(eventClass, priority);
    }

    /**
     * Sets the bukkit event priority
     *
     * @param priority the event priority
     * @return the event builder
     */
    @Nonnull
    public BEvent<T> priority(@Nonnull EventPriority priority) {
        BValidate.notNull(priority);

        this.priority = priority;

        return this;
    }

    /**
     * Adds a new event filter to the list
     *
     * @param filter the event filter
     * @return the event builder
     */
    @Nonnull
    public BEvent<T> filter(@Nonnull Filter... filter) {
        BValidate.notNull(filter);

        for (Filter loop_filter : filter) {
            if (!this.filters.contains(loop_filter)) {
                this.filters.add(loop_filter);
            }
        }

        return this;
    }

    /**
     * Adds a new functional event filter to the list
     *
     * @param functionFilter the functional event filter
     * @return the event builder
     */
    @Nonnull
    public BEvent<T> filter(@Nonnull Function<T, Boolean> functionFilter) {
        BValidate.notNull(functionFilter);

        if (this.functionFilters.contains(functionFilter))
            return this;
        this.functionFilters.add(functionFilter);

        return this;
    }

    /**
     * Sets the usage limit
     *
     * @param usageLimit the sage limit
     * @return the event builder
     */
    @Nonnull
    public BEvent<T> limit(int usageLimit) {
        this.usageLimit = usageLimit;

        return this;
    }

    /**
     * Sets the expire duration and time unit
     *
     * @param expire     the expire duration
     * @param expireUnit the expire time unit
     * @return the event builder
     */
    @Nonnull
    public BEvent<T> expire(int expire, @Nonnull TimeUnit expireUnit) {
        BValidate.notNull(expire);

        this.expire = expire;
        this.expireUnit = expireUnit;

        return this;
    }

    /**
     * Events the consume action
     *
     * @param consumer the root function
     * @return the event builder
     */
    @Nonnull
    public BEvent<T> consume(@Nonnull Consumer<T> consumer) {
        BValidate.notNull(consumer);

        this.consumer = consumer;

        //Register events
        this.register();

        return this;
    }

    /**
     * Events the consume action
     *
     * @param consumer the root function
     * @return the event builder
     */
    @Nonnull
    public BEvent<T> consumeAsync(@Nonnull Consumer<T> consumer) {
        BValidate.notNull(consumer);

        this.consumer = consumer;
        this.async = true;

        //Registers event
        this.register();

        return this;
    }

    /**
     * Registers the vent as a bukkit listener and event executor
     */
    private void register() {
        //Registers bukkit listener and executor
        Bukkit.getPluginManager().registerEvent(this.eventClass, this, this.priority, this, BEconomyCore.getInstance(), false);

        //Expire handler
        if (this.expireUnit != null)
            this.expireTaskId = BScheduler.syncScheduler().after(this.expire, this.expireUnit).run(this::unregister);
    }

    /**
     * Registers the event as a bukkit listener and event executor
     */
    public void unregister() {
        //If it is already unregistered, we do not want to use it
        if (this.unregistered)
            return;

        //Registers bukkit listener and executor
        HandlerList.unregisterAll(this);

        //Sets check
        this.unregistered = true;

        //Task control
        if (this.expireTaskId != 1)
            Bukkit.getScheduler().cancelTask(this.expireTaskId);
    }

    /**
     * Bukkit event executor execute override
     *
     * @param listener the event listener
     * @param event    the bukkit event
     */
    @SuppressWarnings("unchecked")
    @Override
    public void execute(@Nonnull Listener listener, @Nonnull Event event) {
        BValidate.notNull(listener);
        BValidate.notNull(event);

        //If it is already unregistered, we do not want to use it
        if (this.unregistered)
            return;

        //If both event class is not same, no need to continue
        if (!event.getClass().isAssignableFrom(this.eventClass) && !this.eventClass.isAssignableFrom(event.getClass()))
            return;

        //Early check
        if (this.filters.contains(Filter.IGNORE_FILTERS_FOR_LIMIT) && this.usageLimit != 0 && this.usage++ >= this.usageLimit) {
            this.unregister();
            return;
        }

        //Checks cancelled
        if (this.filters.contains(Filter.IGNORE_CANCELLED)
                && event instanceof Cancellable
                && ((Cancellable) event).isCancelled())
            return;

        //Checks disallowed login
        if (this.filters.contains(Filter.IGNORE_DISALLOWED_LOGIN)
                && event instanceof PlayerLoginEvent
                && ((PlayerLoginEvent) event).getResult() != PlayerLoginEvent.Result.ALLOWED)
            return;

        //Checks disallowed pre login
        if (this.filters.contains(Filter.IGNORE_DISALLOWED_PRE_LOGIN)
                && event instanceof AsyncPlayerPreLoginEvent
                && ((AsyncPlayerPreLoginEvent) event).getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        //Checks functional filters
        boolean should_continue = true;
        for (Function<T, Boolean> function_filter : this.functionFilters) {
            if (function_filter.apply((T) event))
                continue;
            should_continue = false;
            break;
        }

        //If any of the functional filters false, no need to continue
        if (!should_continue)
            return;

        //Consume event
        if (this.async)
            BScheduler.asyncScheduler().run(() -> this.consumer.accept((T) event));
        else
            this.consumer.accept((T) event);

        //Usage limit check
        if (!this.filters.contains(Filter.IGNORE_FILTERS_FOR_LIMIT) && this.usageLimit != 0 && this.usage++ >= this.usageLimit)
            this.unregister();
    }

}
