package org.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    @JsonProperty("ingredients")
    private List<String> ingredients = new ArrayList<>();;

    public Order withIngredients(String ingredient) {
        ingredients.add(ingredient);
        return this;
    }
}
