package io.mkrzywanski.gpn.email.api;

import io.mkrzywanski.gpn.email.domain.Price;
import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferData {
    private String name;
    private Set<Price> price;
}
