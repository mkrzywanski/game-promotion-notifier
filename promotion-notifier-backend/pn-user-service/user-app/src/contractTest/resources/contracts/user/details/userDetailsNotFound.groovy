package contracts.user.details

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.RegexPatterns

Contract.make {
    request {
        method(GET())
        url("/v1/users/f20848bf-5500-4002-8222-e9fc2dcab6e6")
    }
    response {
        headers {
            header 'Content-Type': 'application/json'
        }
        status(404)
        body([
                timestamp  : $(producer(regex(RegexPatterns.iso8601WithOffset())), consumer('2021-08-17T16:30:30.501884049Z')),
                message    : "User with id ${fromRequest().path(2)} not found",
                status     : 404,
                serviceName: "user-service",
                path       : "/v1/users/${fromRequest().path(2)}"
        ])
    }
}