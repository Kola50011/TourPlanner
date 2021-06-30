package at.fhtw.service.interfaces;

import at.fhtw.service.model.SearchRequest;
import at.fhtw.service.model.SearchResult;

public interface SearchService {
    SearchResult search(SearchRequest searchRequest);
}
