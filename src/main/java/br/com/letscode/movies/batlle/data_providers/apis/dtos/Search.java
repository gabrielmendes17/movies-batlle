package br.com.letscode.movies.batlle.data_providers.apis.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.ToString;

@Data
public class Search {
    @JsonProperty("Title") 
    public String title;
    @JsonProperty("Year") 
    public String year;
    @ToString.Exclude
    public String imdbID;
    @JsonProperty("Type") 
    public String type;
    @JsonProperty("Poster") 
    @ToString.Exclude
    public String poster;
}
