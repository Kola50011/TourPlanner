package at.fhtw.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Filter {
    private Set<String> logIDs = new HashSet<>();
    private Set<String> tourIDs = new HashSet<>();
}
