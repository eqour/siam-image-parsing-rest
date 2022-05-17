package ru.eqour.imageparsingrest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import ru.eqour.imageparsingrest.helper.ImageTestHelper;
import ru.eqour.imageparsingrest.model.ColorEntireRequest;
import ru.eqour.imageparsingrest.model.ImageRequest;
import ru.eqour.imageparsingrest.model.ImageResponse;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Enclosed.class)
public class ColorEntireTest {

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    public static class NegativeParameterizedTests {

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        private TestRestTemplate restTemplate;

        private final BufferedImage image;
        private final Color color;
        private final Double colorDifference;

        public NegativeParameterizedTests(int imageId, Color color, Double colorDifference) {
            ImageTestHelper helper = new ImageTestHelper();
            if (imageId < 0) {
                this.image = null;
            } else {
                this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            }
            this.color = color;
            this.colorDifference = colorDifference;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { -1, Color.BLACK, 10.0 },
                    { 0, null, 10.0 },
                    { 0, Color.BLACK, -1.0 }
            };
        }

        @Test
        public void colorEntireTest() {
            ResponseEntity<ImageResponse> imageResponse = null;
            if (image != null) {
                imageResponse = restTemplate.exchange(
                        "/image",
                        HttpMethod.POST,
                        new HttpEntity<>(new ImageRequest(image)),
                        ImageResponse.class
                );
            }
            ResponseEntity<String> response = restTemplate.exchange(
                    "/color/entire",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ColorEntireRequest(
                                    imageResponse == null ? 10000
                                            : Objects.requireNonNull(imageResponse.getBody()).getImageId(),
                                    colorDifference,
                                    color
                            )
                    ),
                    String.class
            );
            assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        }
    }
}
