package at.fhtw.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoundingBox {
    private Coordinate ul;
    private Coordinate lr;
}
