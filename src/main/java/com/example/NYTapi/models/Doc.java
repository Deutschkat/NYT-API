package com.example.NYTapi.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Doc {

    @JsonProperty("web_url")
    private String webUrl;

    private Headline headline;
    private Byline byline;
    private List<Multimedia> multimedia;

    @JsonProperty("pub_date")
    private String pubDate;
    @JsonProperty("section_name")
    private String sectionName;

    private String imageUrl;


    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
