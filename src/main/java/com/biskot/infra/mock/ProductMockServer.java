package com.biskot.infra.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class ProductMockServer {

    private WireMockServer wireMockServer = new WireMockServer(9001);

    @PostConstruct
    private void configureMockServer() {
        wireMockServer.start();

        IntStream.range(1, 5).forEach(productId -> {
            try {
                wireMockServer.stubFor(
                        get(urlPathEqualTo("/products/" + productId))
                                .willReturn(aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBody(Files.readString(Paths.get("src/main/resources/mocks/product_" + productId + ".json"))))
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
