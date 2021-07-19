package io.mkrzywanski.gpn.email.api;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewOffersNotificationData {
    @NotNull
    @Valid
    private UserData userData;

    @NotNull
    @Email
    private String email;

    @NotEmpty
    private List<PostData> postDataList;
}
