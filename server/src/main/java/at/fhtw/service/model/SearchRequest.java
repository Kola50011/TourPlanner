package at.fhtw.service.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchRequest {
    private String searchString = "";
}
