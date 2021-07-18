package io.mkrzywanski.gpn.email.api;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostData {
    private List<OfferData> offerDataList;
}
