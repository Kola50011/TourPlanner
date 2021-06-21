package at.fhtw.fixtures;

import at.fhtw.repository.model.LogEntity;
import at.fhtw.service.model.Log;

import java.sql.Timestamp;

public class LogFixtures {
    public static Log carLog() {
        return Log.builder()
                .id(1)
                .tourId(1)
                .startLocation("A")
                .endLocation("B")
                .startTime(new Timestamp(0))
                .endTime(new Timestamp(1))
                .notes("Some notes")
                .rating(1)
                .meansOfTransport("Car")
                .distance(1)
                .moneySpent("0 €")
                .build();
    }

    public static Log bikeLog() {
        return Log.builder()
                .id(2)
                .tourId(2)
                .startLocation("C")
                .endLocation("D")
                .startTime(new Timestamp(0))
                .endTime(new Timestamp(1))
                .notes("Some other notes")
                .rating(5)
                .meansOfTransport("Bike")
                .distance(2)
                .moneySpent("0 €")
                .build();
    }

    public static LogEntity carLogEntity() {
        return LogEntity.builder()
                .id(1)
                .tourId(1)
                .startLocation("A")
                .endLocation("B")
                .startTime(new Timestamp(0))
                .endTime(new Timestamp(1))
                .notes("Some notes")
                .rating(1)
                .meansOfTransport("Car")
                .distance(1)
                .moneySpent("0 €")
                .build();
    }

    public static LogEntity bikeLogEntity() {
        return LogEntity.builder()
                .id(2)
                .tourId(2)
                .startLocation("C")
                .endLocation("D")
                .startTime(new Timestamp(0))
                .endTime(new Timestamp(1))
                .notes("Some other notes")
                .rating(5)
                .meansOfTransport("Bike")
                .distance(2)
                .moneySpent("0 €")
                .build();
    }
}
