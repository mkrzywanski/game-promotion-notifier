package io.mkrzywanski.gpn.email.api;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserData {
    @NotEmpty
    String name;
    @NotEmpty
    String username;
}

