package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label("trigger")
    input {
        triggeredBy("trigger()")
    }
    outputMessage {
        sentTo("notifications")
        body([
                userDetails         : [
                        userId   : '5a1a353b-ffc1-4346-827a-83de9bacd800',
                        username : 'Andrew123',
                        firstName: 'Andrew',
                        email    : 'andrew.golota@gmail.com'
                ],
                postNotificationData: [
                        [
                                link                 : 'http://test.link',
                                offerNotificationData: [
                                        [
                                                name  : 'Rainbow Six',
                                                url   : 'http://test.link',
                                                prices: [
                                                        PLN: 1
                                                ]
                                        ]
                                ]
                        ]
                ]
        ])
    }
}