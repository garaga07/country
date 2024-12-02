package guru.qa.country.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateCountryNameRequest {
    @JsonProperty("country_name") // Соответствие полю в JSON
    private String newCountryName;

    public String getNewCountryName() {
        return newCountryName;
    }
}