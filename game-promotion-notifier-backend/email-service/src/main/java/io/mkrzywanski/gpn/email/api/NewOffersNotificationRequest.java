package io.mkrzywanski.gpn.email.api;

import lombok.Value;

import java.util.List;

@Value
public class NewOffersNotificationRequest {
    UserData userData;
    String email;
    List<PostData> postDataList;
}
