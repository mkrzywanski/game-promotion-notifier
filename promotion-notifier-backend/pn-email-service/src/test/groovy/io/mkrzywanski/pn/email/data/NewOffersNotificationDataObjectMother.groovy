package io.mkrzywanski.pn.email.data


import io.mkrzywanski.pn.email.api.NewOffersNotificationData
import io.mkrzywanski.pn.email.api.PostData
import io.mkrzywanski.pn.email.api.UserData

final class NewOffersNotificationDataObjectMother {

    private UserData userData = UserDataObjectMother.userData().build()
    private List<PostData> postDataList = List.of(PostDataObjectMother.postData().build())

    static NewOffersNotificationDataObjectMother newOffersNotificationData() {
        return new NewOffersNotificationDataObjectMother()
    }

    private NewOffersNotificationDataObjectMother() {
    }

    NewOffersNotificationDataObjectMother userData(final UserData userData) {
        this.userData = userData
        return this
    }

    NewOffersNotificationDataObjectMother postData(List<PostData> postDataList) {
        this.postDataList = postDataList
        return this
    }

    NewOffersNotificationData build() {
        return new NewOffersNotificationData(userData, postDataList)
    }
}
