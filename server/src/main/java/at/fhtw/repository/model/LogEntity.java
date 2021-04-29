package at.fhtw.repository.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@ToString
public class LogEntity {

    private int id;
    private int tourId;
    private Timestamp startTime;
    private Timestamp endTime;
    private int rating;
    private String meansOfTransport;
}
