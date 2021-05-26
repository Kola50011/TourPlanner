package at.fhtw.events;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class LogChangeEvent extends Event {
    public static final EventType<Event> TOUR_CHANGE_EVENT = new EventType<>("LogChangeEvent");

    @Getter
    private final int logId;

    public LogChangeEvent(int tourId) {
        super(TOUR_CHANGE_EVENT);
        this.logId = tourId;
    }
}
