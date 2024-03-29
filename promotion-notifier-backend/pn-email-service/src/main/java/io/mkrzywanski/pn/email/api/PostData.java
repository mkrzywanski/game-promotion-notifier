package io.mkrzywanski.pn.email.api;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PostData {

    @NotEmpty
    @URL
    private String link;

    @NotEmpty
    @Valid
    private List<OfferData> offerNotificationData;
}
