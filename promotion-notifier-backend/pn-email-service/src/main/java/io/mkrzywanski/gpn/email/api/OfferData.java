package io.mkrzywanski.gpn.email.api;

import io.mkrzywanski.gpn.email.domain.Price;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferData {
    @NotEmpty
    private String name;
    @NotEmpty
    @Valid
    private Set<Price> price;
}
