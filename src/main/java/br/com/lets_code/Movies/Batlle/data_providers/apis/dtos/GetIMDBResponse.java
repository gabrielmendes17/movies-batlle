package br.com.lets_code.Movies.Batlle.data_providers.apis.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
GetIMDBResponse GetIMDBResponse = om.readValue(myJsonString, GetIMDBResponse.class); */
public class GetIMDBResponse{
    @JsonProperty("Search") 
    public List<Search> search;
    public String totalResults;
    @JsonProperty("Response") 
    public String response;
}

