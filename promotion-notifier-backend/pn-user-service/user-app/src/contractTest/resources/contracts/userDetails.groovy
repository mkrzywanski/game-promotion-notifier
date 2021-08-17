import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method(GET())
        url("/v1/users/e083123c-eac4-463d-bc59-7f2e3fa3cbe1")
    }
    response {
        headers {
            header 'Content-Type': 'application/json'
        }
        status(200)
        body([
                userId   : "${fromRequest().path(2)}",
                username : "test",
                firstName: "test",
                email    : "test@test.pl"
        ])
    }
}