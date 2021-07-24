package io.mkrzywanski.pn.email.domain;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Price {
    @NotNull
    Currency currency;

    @NotNull
    BigDecimal value;
}
