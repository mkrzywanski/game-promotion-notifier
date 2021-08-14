package contracts

import org.springframework.cloud.contract.spec.Contract

def contractDsl = Contract.make {
    label("trigger")
    input {
        triggeredBy("trigger()")
    }
    outputMessage {
        sentTo("posts")
        body([
                id: $(anyUuid()),
                offers: [[
                             name: 'Rainbow Six',
                             gamePrice: [
                                     PLN: 1
                             ],
                             link: 'www.test.pl'

                ]]
        ])
    }
}