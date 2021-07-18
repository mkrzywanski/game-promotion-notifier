package io.mkrzywanski.gpn.email.api;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserData {
    String name;
    String username;
}

