package io.mkrzywanski.pn.email.domain;

import lombok.Value;

@Value(staticConstructor = "from")
public class EmailContent {
    String content;
}
