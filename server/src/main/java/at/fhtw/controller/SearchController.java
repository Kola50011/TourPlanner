package at.fhtw.controller;

import at.fhtw.service.interfaces.SearchService;
import at.fhtw.service.model.SearchRequest;
import at.fhtw.service.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @PostMapping(path = "/search")
    public SearchResult search(@RequestBody SearchRequest searchRequest) {
        return searchService.search(searchRequest);
    }
}
