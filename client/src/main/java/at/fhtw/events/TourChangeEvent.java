package at.fhtw.events;

import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

public class TourChangeEvent extends Event {
    public static final EventType<Event> TOUR_CHANGE_EVENT = new EventType<>("TourChangeEvent");

    @Getter
    private int tourId;

    public TourChangeEvent(int tourId) {
        super(TOUR_CHANGE_EVENT);
        this.tourId = tourId;
    }
}
