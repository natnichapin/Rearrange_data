package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@AllArgsConstructor
@Getter
@Setter
public class MapWithAccountNumber {
    private HashMap<String, MapWithProductName> accountNumberMap;

}

