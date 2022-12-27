package io.mkrzywanski.pn.email.api;

import io.mkrzywanski.pn.email.domain.Price;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class OfferData {
    @NotEmpty
    private String name;

    @NotEmpty
    @URL
    private String url;

    @NotEmpty
    @Valid
    private Set<Price> price;
}
