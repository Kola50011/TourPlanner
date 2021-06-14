package at.fhtw.repository.model;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LogEntity {

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
