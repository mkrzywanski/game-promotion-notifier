package io.mkrzywanski.gpn.email.domain;

import lombok.Value;

@Value(staticConstructor = "of")
public class EmailAddress {
    String email;
}
