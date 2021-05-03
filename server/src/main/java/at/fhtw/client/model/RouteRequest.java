package at.fhtw.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@Builder
@ToString
public class RouteRequest {

    List<String> locations;
}
