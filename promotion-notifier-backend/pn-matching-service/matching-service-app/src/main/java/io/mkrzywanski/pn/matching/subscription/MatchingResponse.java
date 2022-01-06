package io.mkrzywanski.pn.matching.subscription;

import lombok.*;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MatchingResponse {
    Set<Match> matches;
}
