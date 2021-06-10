package at.fhtw.events;

import at.fhtw.client.model.Filter;
import at.fhtw.client.model.SearchResult;
import javafx.event.Event;
import javafx.event.EventType;
import lombok.Getter;

import java.util.HashSet;

public class FilterEvent extends Event {
    public static final EventType<Event> FILTER_EVENT = new EventType<>("FilterEvent");

    @Getter
    private final Filter filter;

    public FilterEvent(SearchResult searchResult) {
        super(FILTER_EVENT);
        this.filter = Filter.builder()
                .tourIDs(new HashSet<>(searchResult.getTourIDs()))
                .logIDs(new HashSet<>(searchResult.getLogIDs()))
                .build();
    }
}
