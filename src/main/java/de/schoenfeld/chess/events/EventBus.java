package de.schoenfeld.chess.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus {
    private static final Logger LOGGER = Logger.getLogger(EventBus.class.getName());

    private final Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> listeners;

    public EventBus(Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> listeners) {
        this.listeners = listeners;
    }

    public EventBus() {
        this(new HashMap<>());
    }

    public <T extends GameEvent> void subscribe(Class<T> eventType, Consumer<T> listener) {
        final String eventName = eventType.getName();
        LOGGER.log(Level.FINE, "Registering {0} for {1} events", new Object[] {listener, eventName});

        listeners.computeIfAbsent(eventType, k -> {
            LOGGER.log(Level.FINER, "Creating new listener list for {0}", eventName);
            return new ArrayList<>();
        }).add(listener);

        LOGGER.log(Level.INFO, "Subscription added: {0} now has {1} listeners",
                new Object[] {eventName, listeners.get(eventType).size()});
    }

    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void publish(T event) {
        final String eventType = event.getClass().getName();
        LOGGER.log(Level.FINE, "Publishing {0} event (ID: {1})",
                new Object[] {eventType, event.hashCode()});

        List<Consumer<? extends GameEvent>> eventListeners = listeners.get(event.getClass());

        if (eventListeners == null || eventListeners.isEmpty()) {
            LOGGER.log(Level.WARNING, "No listeners registered for {0} event", eventType);
            return;
        }

        LOGGER.log(Level.FINER, "Dispatching to {0} listener(s) for {1}",
                new Object[] {eventListeners.size(), eventType});

        eventListeners.forEach(listener -> {
            final String listenerName = listener.getClass().getSimpleName();
            try {
                LOGGER.log(Level.FINEST, "Notifying {0} about {1} event",
                        new Object[] {listenerName, eventType});

                ((Consumer<T>) listener).accept(event);

                LOGGER.log(Level.FINEST, "Successfully notified {0}", listenerName);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE,
                        String.format("Error in listener %s handling %s event",
                                listenerName, eventType), e);
            }
        });

        LOGGER.log(Level.FINE, "Completed processing of {0} event", eventType);
    }
}
