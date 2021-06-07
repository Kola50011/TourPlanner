package at.fhtw.service.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImportExport {
    private List<DetailedTour> tours;
    private List<Log> logs;
}
