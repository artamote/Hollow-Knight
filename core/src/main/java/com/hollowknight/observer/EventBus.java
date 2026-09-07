package com.hollowknight.observer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class EventBus {

    public interface Listener {
        void onEvent(GameEvent event, Object data);
    }

    private static final Map<GameEvent, List<Listener>> listeners =
            new EnumMap<>(GameEvent.class);

    public static void subscribe(GameEvent event, Listener listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    public static void unsubscribe(GameEvent event, Listener listener) {
        List<Listener> list = listeners.get(event);
        if (list != null) list.remove(listener);
    }

    public static void publish(GameEvent event, Object data) {
        List<Listener> list = listeners.get(event);
        if (list != null) {

            for (Listener l : new ArrayList<>(list)) {
                l.onEvent(event, data);
            }
        }
    }

    public static void publish(GameEvent event) {
        publish(event, null);
    }


    public static void clearAll() {
        listeners.clear();
    }
}
