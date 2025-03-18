package com.techy.microservices.order.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class InventoryClientStud {

    public static void stubInventoryCall(String skuCode,Integer quantity){

        stubFor(get(urlPathEqualTo("/api/inventory/check"))
                .withQueryParam("skuCode", equalTo(skuCode))
                .withQueryParam("quantity", equalTo(quantity.toString()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true"))); //

    }
}
