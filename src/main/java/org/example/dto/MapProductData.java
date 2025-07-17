package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.response.DetailOutput;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MapProductData {
    private String productName;
    private Double totalBalance;
    private List<DetailOutput> details;


}
