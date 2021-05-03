package at.fhtw.service.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetailedTour {
    private String name;
    private int id;
    private float distance;
}
