package io.mkrzywanski.pn.subscription;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class Offer {

    String text;
    UUID id;

    private Offer() {
        this("", UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

}
