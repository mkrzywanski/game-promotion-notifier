package io.mkrzywanski.gpn.email.domain;

import io.mkrzywanski.gpn.email.api.PostData;
import io.mkrzywanski.gpn.email.api.UserData;
import lombok.Value;

import java.util.List;

@Value
public class NewOffersEmailData {
    UserData userData;
    List<PostData> postDataList;
}
