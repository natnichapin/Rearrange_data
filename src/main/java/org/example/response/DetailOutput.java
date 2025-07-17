package org.example.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
@Setter
public class DetailOutput {
    private String cardNumber;
    private String balance;
    @JsonProperty("expire-date")
    private String expireDate;

}
