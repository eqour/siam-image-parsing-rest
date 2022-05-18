package ru.eqour.imageparsingrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import ru.eqour.imageparsingrest.helper.TestHelper;
import ru.eqour.imageparsingrest.model.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Enclosed.class)
public class ColorPointTest {

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    public static class NegativeParameterizedTests {

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        private TestRestTemplate restTemplate;

        private final BufferedImage image;
        private final Integer[] startPoint;
        private final Double colorDifference;
        private final Integer searchRadius;

        public NegativeParameterizedTests(int imageId, Integer[] startPoint,
                                          Double colorDifference, Integer searchRadius) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) this.image = null;
            else this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            this.startPoint = startPoint;
            this.colorDifference = colorDifference;
            this.searchRadius = searchRadius;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { -1, new Integer[] { 0, 0 }, 0.0, 1 },
                    { 0, new Integer[] { -5, 0 }, 0.0, 1 },
                    { 0, new Integer[] { 20, 0 }, 0.0, 1 },
                    { 0, new Integer[] { 0, 0 }, -1.0, 1 },
                    { 0, new Integer[] { null, 0 }, 0.0, 1 },
                    { 0, new Integer[] { 0, null }, 0.0, 1 },
                    { 0, new Integer[] { 0, 0 }, null, 1 },
                    { 0, new Integer[] { 0, 0 }, 0.0, null }
            };
        }

        @Test
        public void colorPointTest() {
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
                    "/color/point",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ColorPointRequest(
                                    image == null ? Long.MAX_VALUE
                                            : Objects.requireNonNull(imageResponse.getBody()).getImageId(),
                                    colorDifference,
                                    startPoint[0], startPoint[1],
                                    searchRadius
                            )
                    ),
                    String.class
            );
            assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        }
    }

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    public static class PositiveParameterizedTests {

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        private ObjectMapper mapper;
        @Autowired
        private TestRestTemplate restTemplate;

        private final BufferedImage image;
        private final Integer[] startPoint;
        private final Double colorDifference;
        private final Integer searchRadius;

        public PositiveParameterizedTests(int imageId, Integer[] startPoint, Double colorDifference,
                                          Integer searchRadius) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) {
                this.image = null;
            } else {
                this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            }
            this.startPoint = startPoint;
            this.colorDifference = colorDifference;
            this.searchRadius = searchRadius;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 1, new Integer[] { 2, 2 }, 0.0, 3 },
                    { 2, new Integer[] { 2, 2 }, 0.0, 3 },
                    { 2, new Integer[] { 2, 2 }, 1.0, 3 },
                    { 3, new Integer[] { 0, 0 }, 0.0, 1 },
                    { 3, new Integer[] { 0, 0 }, 1.0, 1 },
                    { 3, new Integer[] { 0, 0 }, 4.0, 1 },
                    { 4, new Integer[] { 40, 49 }, 0.0, 15 },
                    { 4, new Integer[] { 40, 49 }, 200.0, 10 }
            };
        }

        @Test
        public void colorPointTest() {
            ResponseEntity<ImageResponse> imageResponse = restTemplate.exchange(
                    "/image",
                    HttpMethod.POST,
                    new HttpEntity<>(new ImageRequest(image)),
                    ImageResponse.class
            );
            ResponseEntity<ColorResponse> entireResponse = restTemplate.exchange(
                    "/color/entire",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ColorEntireRequest(
                                    Objects.requireNonNull(imageResponse.getBody()).getImageId(),
                                    colorDifference,
                                    new Color(image.getRGB(startPoint[0], startPoint[1]))
                            )
                    ),
                    ColorResponse.class
            );
            ResponseEntity<String> response = restTemplate.exchange(
                    "/color/point",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ColorPointRequest(
                                    Objects.requireNonNull(imageResponse.getBody()).getImageId(),
                                    colorDifference,
                                    startPoint[0], startPoint[1],
                                    searchRadius
                            )
                    ),
                    String.class
            );
            try {
                assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
                ColorResponse r = mapper.readValue(response.getBody(), ColorResponse.class);
                assertThat(r).isNotNull();
                int[][] actual = TestHelper.sortMinXMinY(r.getPixels());
                int[][] expected = TestHelper.sortMinXMinY(Objects.requireNonNull(entireResponse.getBody()).getPixels());
                assertThat(actual).isDeepEqualTo(expected);
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
