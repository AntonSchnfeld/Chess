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
        LOGGER.log(Level.INFO, listener + " subscribed to " + eventType.getName() + " events");
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T extends GameEvent> void publish(T event) {
        LOGGER.log(Level.INFO, event + " published to all subscribed listeners");
        List<Consumer<? extends GameEvent>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            eventListeners.forEach(listener -> {
                ((Consumer<T>) listener).accept(event);
            });
        }
    }
}
