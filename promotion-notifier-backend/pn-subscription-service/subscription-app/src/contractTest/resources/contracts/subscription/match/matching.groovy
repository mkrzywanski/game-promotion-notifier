package contracts.subscription.match

import org.springframework.cloud.contract.spec.Contract

def postId = "d02efa49-0d6f-4f17-9aaa-806ceb648477"
def offerId = "6c9bfd13-d071-4bd3-a028-53d44496e03b"
def userId = "7b3a0fb4-d94f-4cb1-85e0-c0219111afea"

Contract.make {
    request {
        method POST()
        url "/v1/subscriptions/match"
        headers {
            header 'Content-Type': 'application/json'
        }
        body([
                postsToMatch: [
                        [
                                postId: postId,
                                offers: [[
                                                 id  : offerId,
                                                 text: "Rainbow Six"
                                         ]]
                        ]
                ]
        ])
    }
    response {
        status(200)
        headers {
            header 'Content-Type': 'application/json'
        }
        body([
                matches: [
                        [
                                userId : userId,
                                postId : postId,
                                offerId: offerId
                        ]
                ]
        ])
    }
}
