package de.schoenfeld.chess.events;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus {
    private final Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> listeners;
    private final Queue<GameEvent> eventQueue;
    private boolean isProcessing;

    public EventBus(Map<Class<? extends GameEvent>, List<Consumer<? extends GameEvent>>> listeners) {
        this.listeners = listeners;
        eventQueue = new ConcurrentLinkedQueue<>();
        isProcessing = false;
    }

    public EventBus() {
        this(new HashMap<>());
    }

    public <T extends GameEvent> void subscribe(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(listener);
    }

    public void publish(GameEvent event) {
        eventQueue.add(event);
        if (!isProcessing) processEventQueue();
    }

    private void processEventQueue() {
        isProcessing = true;
        while (!eventQueue.isEmpty()) {
            GameEvent event = eventQueue.poll();
            if (event == null) return;

            List<Consumer<? extends GameEvent>> eventListeners = listeners.get(event.getClass());
            if (eventListeners == null || eventListeners.isEmpty()) {
                continue;
            }

            for (Consumer<? extends GameEvent> rawListener : eventListeners) {
                @SuppressWarnings("unchecked")
                Consumer<GameEvent> listener = (Consumer<GameEvent>) rawListener;

                listener.accept(event);
            }
        }
        isProcessing = false;
    }
}
