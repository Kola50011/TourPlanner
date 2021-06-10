package at.fhtw.service;

import at.fhtw.repository.LogRepository;
import at.fhtw.repository.TourRepository;
import at.fhtw.repository.model.LogEntity;
import at.fhtw.repository.model.TourEntity;
import at.fhtw.service.model.SearchRequest;
import at.fhtw.service.model.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {
    private static final String ID_FIELD = "id";
    private static final String SEARCH_FIELD = "searchField";
    private static final int SEARCH_LIMIT = 10;

    private final LogRepository logRepository;
    private final TourRepository tourRepository;
    private final ObjectMapper objectMapper;

    public SearchResult search(SearchRequest searchRequest) {
        var resultTours = searchDocuments(searchRequest, getTourDocuments());
        var resultLogs = searchDocuments(searchRequest, getLogDocuments());
        resultTours.addAll(getToursOfLogs(resultLogs));

        addIfEmpty(resultTours);
        addIfEmpty(resultLogs);

        return SearchResult.builder()
                .tourIDs(new ArrayList<>(resultTours))
                .logIDs(new ArrayList<>(resultLogs))
                .build();
    }

    private void addIfEmpty(Set<String> set) {
        if (set.isEmpty()) {
            set.add("-1");
        }
    }

    private Set<String> getToursOfLogs(Set<String> logs) {
        var res = new HashSet<String>();
        for (var logId : logs) {
            var optionalLog = logRepository.getLog(Integer.parseInt(logId));
            optionalLog.ifPresent(log -> res.add(Integer.toString(log.getTourId())));
        }
        return res;
    }

    private Set<String> searchDocuments(SearchRequest searchRequest, List<Document> documents) {
        Set<String> ret = new HashSet<>();
        try (var analyzer = new StandardAnalyzer()) {
            var indexPath = Files.createTempDirectory(Long.toString(new Date().getTime()));
            try (var directory = FSDirectory.open(indexPath)) {
                var indexWriter = getIndexWriter(analyzer, directory);
                indexWriter.addDocuments(documents);
                indexWriter.close();

                var indexReader = DirectoryReader.open(directory);
                var indexSearcher = new IndexSearcher(indexReader);
                var queryParser = new SimpleQueryParser(analyzer, SEARCH_FIELD);
                var query = queryParser.parse(searchRequest.getSearchString());

                var topDocs = indexSearcher.search(query, SEARCH_LIMIT);

                for (var scoreDoc : topDocs.scoreDocs) {
                    var doc = indexSearcher.doc(scoreDoc.doc);
                    ret.add(doc.getField(ID_FIELD).stringValue());
                }
                IOUtils.rm(indexPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private IndexWriter getIndexWriter(Analyzer analyzer, Directory directory) throws IOException {
        var indexWriterConfig = new IndexWriterConfig(analyzer);
        return new IndexWriter(directory, indexWriterConfig);
    }

    private Document tourToDocument(TourEntity tourEntity) {
        var document = new Document();
        document.add(new Field(ID_FIELD, Integer.toString(tourEntity.getId()), TextField.TYPE_STORED));
        document.add(new Field(SEARCH_FIELD, objectToString(tourEntity), TextField.TYPE_STORED));
        return document;
    }

    private Document logToDocument(LogEntity logEntity) {
        var document = new Document();
        document.add(new Field(ID_FIELD, Integer.toString(logEntity.getId()), TextField.TYPE_STORED));
        document.add(new Field(SEARCH_FIELD, objectToString(logEntity), TextField.TYPE_STORED));
        return document;
    }

    private List<Document> getLogDocuments() {
        var logs = logRepository.getAllLogs();
        return logs.stream().map(this::logToDocument).collect(Collectors.toList());
    }

    private List<Document> getTourDocuments() {
        try {
            var tours = tourRepository.getAllTours();
            return tours.stream().map(this::tourToDocument).collect(Collectors.toList());
        } catch (SQLException e) {
            log.error("Unable to get tours!", e);
        }
        return new ArrayList<>();
    }

    private String objectToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert object to json!", e);
        }
        return "";
    }
}
