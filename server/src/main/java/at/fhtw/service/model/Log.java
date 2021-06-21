package at.fhtw.service.model;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
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
}
