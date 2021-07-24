package io.mkrzywanski.gpn.email.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
public class Subject {
    String subject;
}
