package at.fhtw.client.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetailedTour {
    private int id;
    private String name;
    private String description;
    private float distance;
    private String image;
}
