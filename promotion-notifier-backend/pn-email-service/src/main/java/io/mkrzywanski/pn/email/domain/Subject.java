package io.mkrzywanski.pn.email.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
public class Subject {
    String subject;
}
