package io.mkrzywanski.gpn.email.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Price {
    Currency currency;
    BigDecimal value;
}
