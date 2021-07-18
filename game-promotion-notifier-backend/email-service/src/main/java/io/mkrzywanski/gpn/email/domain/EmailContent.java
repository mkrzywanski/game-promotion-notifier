package io.mkrzywanski.gpn.email.domain;

import lombok.Value;

@Value(staticConstructor = "from")
public class EmailContent {
    String content;
}
