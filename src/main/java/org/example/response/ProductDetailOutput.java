package org.example.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ProductDetailOutput {
    private String name;
    private String totalBalance;
    private List<DetailOutput> details;

}

