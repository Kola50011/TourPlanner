package at.fhtw.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
