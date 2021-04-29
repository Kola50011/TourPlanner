package at.fhtw.repository.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TourEntity {

    private String name;
    private int id;
}
