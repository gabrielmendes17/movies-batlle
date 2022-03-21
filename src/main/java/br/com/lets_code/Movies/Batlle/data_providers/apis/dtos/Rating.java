package br.com.lets_code.Movies.Batlle.data_providers.apis.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {
    @JsonProperty("Source") 
    public String source;
    @JsonProperty("Value") 
    public String value;
}