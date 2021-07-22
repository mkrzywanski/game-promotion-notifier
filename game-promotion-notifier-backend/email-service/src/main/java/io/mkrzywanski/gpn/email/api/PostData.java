package io.mkrzywanski.gpn.email.api;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostData {
    @NotEmpty
    @Valid
    private List<OfferData> offerDataList;
}
