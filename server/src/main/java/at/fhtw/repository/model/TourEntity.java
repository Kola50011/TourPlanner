package at.fhtw.repository.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TourEntity {

    private int id;
    private String name;
    private String description;
}
