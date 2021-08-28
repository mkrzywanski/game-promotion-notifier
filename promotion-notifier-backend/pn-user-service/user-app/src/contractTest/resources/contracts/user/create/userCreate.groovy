package contracts.user.create

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    request {
        method POST()
        url "/v1/users"
        headers {
            header 'Content-Type': 'application/json'
        }
        body([
                firstName: "Michal",
                userName : "user1",
                email    : "test@test.pl"
        ])
    }
    response {
        headers {
            header 'Content-Type': 'application/json'
        }
        status 201
        body([
                userId: $(producer('e083123c-eac4-463d-bc59-7f2e3fa3cbe1'), consumer(anyUuid())),
        ])
    }
}
