package at.fhtw.service.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchResult {
    private List<String> logIDs = new ArrayList<>();
    private List<String> tourIDs = new ArrayList<>();
}
