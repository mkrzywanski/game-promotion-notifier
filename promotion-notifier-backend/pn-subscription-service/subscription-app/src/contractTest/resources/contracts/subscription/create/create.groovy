package contracts.subscription.create

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        url "/v1/subscriptions"
        headers {
            header 'Content-Type': 'application/json'
        }
        body([
                userId: "c6d5ea09-e52d-4a4d-a365-fd95142a99be",
                items : [
                        "Rainbow Six"
                ]
        ])
    }
    response {
        status 201
        headers {
            header 'Content-Type': 'application/json'
        }
        body([
                subscriptionId: anyUuid()
        ])
    }
}
