package br.com.letscode.movies.batlle.data_providers.apis.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {
    @JsonProperty("Source") 
    public String source;
    @JsonProperty("Value") 
    public String value;
}