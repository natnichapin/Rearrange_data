package org.example.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class AccountOutput {

    private Integer accountTotal ;
    private List<ProductOutput> accounts;
}
