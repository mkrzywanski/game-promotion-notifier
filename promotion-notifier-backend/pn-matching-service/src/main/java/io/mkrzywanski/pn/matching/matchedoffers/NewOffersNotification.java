package io.mkrzywanski.pn.matching.matchedoffers;

import io.mkrzywanski.pn.matching.user.UserDetails;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class NewOffersNotification {
    private UserDetails userDetails;
    private List<PostNotificationData> postNotificationData;
}
