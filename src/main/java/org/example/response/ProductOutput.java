package org.example.response;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ProductOutput {
    private String accountNumber;
    private List<ProductDetailOutput> products;
}
