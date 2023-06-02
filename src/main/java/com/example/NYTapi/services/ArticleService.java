package com.example.NYTapi.services;



import com.example.NYTapi.models.*;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Value("${api_key}")
    private String apikey;

    @Value("${mostPopularUrl}")
    private String mostPopularUrl;

    @Autowired
    RestTemplate restTemplate;
    Environment environment;


    public List<Article> getMostPopular() {
        NytResponse response = restTemplate.getForObject(mostPopularUrl + "api-key=" + apikey, NytResponse.class);
        List<Article> results = new ArrayList<>();
        if (response != null && response.getStatus().equals("OK")) {
            results = response.getResults();
            results = results.stream()
                    .filter(article -> article.getMedia() != null && !article.getMedia().isEmpty())
                    .peek(article -> {
                        List<Media> medias = article.getMedia();
                        if (!medias.isEmpty() && medias.get(0).getMediaMetadata() != null && !medias.get(0).getMediaMetadata().isEmpty()) {
                            article.setImageUrl(medias.get(0).getMediaMetadata().get(0).getUrl());
                        }
                    })
                    .collect(Collectors.toList());
        }
        return results;
    }

    public List<Doc> getSearchResults(String searchText) {
        String apiKey = environment.getProperty("nytapi.apikey");
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=" + searchText + "&api-key=" + apiKey;

        ResponseEntity<NytSearchResponse> responseEntity = restTemplate.getForEntity(url, NytSearchResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            NytSearchResponse response = responseEntity.getBody();
            if (response != null && response.getResponse() != null) {
                Response searchResponse = response.getResponse();
                List<Doc> docs = searchResponse.getDocs();

                for (Doc doc : docs) {
                    List<Multimedia> multimediaList = doc.getMultimedia();
                    if (multimediaList != null) {
                        for (Multimedia multimedia : multimediaList) {
                            if ("largeHorizontal375".equals(multimedia.getSubtype())) {
                                doc.setImageUrl(multimedia.getUrl());
                                break; // Break the loop once we find the desired media
                            }
                        }
                    }
                }

                return docs;
            }
        }

        return Collections.emptyList();
    }
}