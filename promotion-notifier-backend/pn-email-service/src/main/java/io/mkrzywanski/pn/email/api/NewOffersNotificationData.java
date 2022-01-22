package io.mkrzywanski.pn.email.api;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
