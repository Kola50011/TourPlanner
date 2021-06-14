package at.fhtw.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Log {

    private int id;
    private int tourId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String startLocation;
    private String endLocation;
    private int rating;
    private String meansOfTransport;
    private float distance;
    private String notes;
    private String moneySpent;

    public static Log defaultLog() {
        return Log.builder()
                .startTime(Timestamp.from(Instant.now()))
                .endTime(Timestamp.from(Instant.now()))
                .startLocation("")
                .endLocation("")
                .rating(0)
                .meansOfTransport("")
                .notes("")
                .moneySpent("")
                .build();
    }
}
