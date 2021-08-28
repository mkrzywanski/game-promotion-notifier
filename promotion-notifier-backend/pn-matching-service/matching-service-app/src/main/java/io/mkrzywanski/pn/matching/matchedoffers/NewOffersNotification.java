package io.mkrzywanski.pn.matching.matchedoffers;

import io.mkrzywanski.pn.matching.user.UserDetails;
import lombok.Value;

import java.util.List;

@Value
public class NewOffersNotification {
    UserDetails userDetails;
    List<PostNotificationData> postNotificationData;
}
