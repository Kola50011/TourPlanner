package at.fhtw.client.model;

import lombok.*;

import java.sql.Timestamp;

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
}
