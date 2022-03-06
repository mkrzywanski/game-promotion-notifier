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
                userId: "db3ca7f3-7c8b-48ab-8245-4126a1389daf",
                firstName: "Michal",
                lastName: "K",
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
                userId: $(producer('db3ca7f3-7c8b-48ab-8245-4126a1389daf'), consumer(anyUuid())),
        ])
    }
}
