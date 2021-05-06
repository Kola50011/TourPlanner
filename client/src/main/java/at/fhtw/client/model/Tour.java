package at.fhtw.client.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Tour {
    private String name;
    private String description;
    private int id;
}
