package at.fhtw.service.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class DetailedTour {
    private int id;
    private String name;
    private String description;
    private float distance;
    private String image;
}
