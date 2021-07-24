package io.mkrzywanski.pn.email.domain;

import io.mkrzywanski.pn.email.api.PostData;
import io.mkrzywanski.pn.email.api.UserData;
import lombok.Value;

import java.util.List;

@Value
public class NewOffersEmailData {
    UserData userData;
    List<PostData> postDataList;
}
