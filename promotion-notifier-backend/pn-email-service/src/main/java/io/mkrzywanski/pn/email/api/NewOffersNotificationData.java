package io.mkrzywanski.pn.email.api;

import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class NewOffersNotificationData {
    @NotNull
    @Valid
    private UserData userData;

    @NotEmpty
    @Valid
    private List<PostData> postNotificationData;
}
