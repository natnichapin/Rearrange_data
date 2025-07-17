package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardsInput {

    private Integer cardTotal;
    private List<CardDetailInput> cards;

   public CardsInput() {
       this.cardTotal = 0;
       this.cards = new ArrayList<>();
   }


    // standard getters setters

}
