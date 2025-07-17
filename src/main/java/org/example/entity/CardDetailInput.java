package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardDetailInput {

        private String productName;
        private String cardNumber;
        private String accountNumber;
        private String balance;
        @JsonProperty("expire-date")
        private String expireDate;



        // standard getters setters

}

