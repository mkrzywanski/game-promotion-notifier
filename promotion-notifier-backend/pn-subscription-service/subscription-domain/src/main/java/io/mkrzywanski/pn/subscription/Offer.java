package io.mkrzywanski.pn.subscription;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Offer {

    String text;
    int id;

    private Offer() {
        this("", 0);
    }

}
