package at.fhtw.client.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class Coordinate {
    private float lng;
    private float lat;
}
