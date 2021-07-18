package io.mkrzywanski.gpn.email.api;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NewOffersNotificationData {
    private UserData userData;
    private String email;
    private List<PostData> postDataList;
}
