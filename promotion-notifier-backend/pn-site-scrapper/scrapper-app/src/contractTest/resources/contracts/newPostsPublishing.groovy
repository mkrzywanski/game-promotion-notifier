package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label("trigger")
    input {
        triggeredBy("trigger()")
    }
    outputMessage {
        sentTo("posts")
        body([
                id    : $(anyUuid()),
                offers: [[
                                 id       : '856e68ac-2ae9-4164-bbda-374663b91cdd',
                                 name     : 'Rainbow Six',
                                 gamePrice: [
                                         PLN: 1
                                 ],
                                 link     : 'www.test.pl'

                         ]]
        ])
    }
}