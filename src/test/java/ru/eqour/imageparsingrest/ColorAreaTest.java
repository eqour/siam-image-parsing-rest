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
public class ColorAreaTest {

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
        private final Integer[] selectedArea;

        public NegativeParameterizedTests(int imageId, Color color, Double colorDifference, Integer[] selectedArea) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) {
                this.image = null;
            } else {
                this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            }
            this.color = color;
            this.colorDifference = colorDifference;
            this.selectedArea = selectedArea;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 0, Color.BLACK, 10.0, new Integer[] { -1, 2, 0, 2 } },
                    { 0, Color.BLACK, 10.0, new Integer[] { 0, 2, 0, 9 } },
                    { -1, Color.BLACK, 10.0, new Integer[] { 0, 0, 2, 2 } },
                    { 0, null, 10.0, new Integer[] { 0, 0, 2, 2 } },
                    { 0, Color.BLACK, null, new Integer[] { 0, 0, 2, 2 } },
                    { 0, Color.BLACK, -1.0, new Integer[] { 0, 0, 2, 2 } },
                    { 0, Color.BLACK, 10.0, new Integer[] { 0, null, 2, null } },
            };
        }

        @Test
        public void colorAreaTest() {
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
                    "/color/area",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ColorAreaRequest(
                                    image == null ? Long.MAX_VALUE
                                            : Objects.requireNonNull(imageResponse.getBody()).getImageId(),
                                    colorDifference,
                                    color,
                                    selectedArea[0], selectedArea[1], selectedArea[2], selectedArea[3]
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
        private final Color color;
        private final Double colorDifference;
        private final int[][] pixels;

        private final Integer[] selectedArea;

        public PositiveParameterizedTests(int imageId, Color color, Double colorDifference,
                                          Integer[] selectedArea, int[][] pixels) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) this.image = null;
            else this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            this.color = color;
            this.colorDifference = colorDifference;
            this.pixels = pixels;
            this.selectedArea = selectedArea;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 0, Color.RED, 0.0, new Integer[] { 0, 0, 2, 2 }, new int[][] {} },
                    { 0, Color.RED, 500.0, new Integer[] { 1, 1, 1, 1 }, new int[][] { { 1, 1 } } },
                    { 1, Color.RED, 0.0, new Integer[] { 1, 1, 3, 3 }, new int[][] { { 2, 2 } } },
                    { 3, Color.RED, 2.0, new Integer[] { 0, 2, 0, 2 }, new int[][] { { 0, 2 } } },
            };
        }

        @Test
        public void colorAreaTest() {
            ResponseEntity<ImageResponse> imageResponse = restTemplate.exchange(
                    "/image",
                    HttpMethod.POST,
                    new HttpEntity<>(new ImageRequest(image)),
                    ImageResponse.class
            );
            ResponseEntity<String> response = restTemplate.exchange(
                    "/color/area",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ColorAreaRequest(
                                    Objects.requireNonNull(imageResponse.getBody()).getImageId(),
                                    colorDifference,
                                    color,
                                    selectedArea[0], selectedArea[1], selectedArea[2], selectedArea[3]
                            )
                    ),
                    String.class
            );
            try {
                assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
                ColorResponse r = mapper.readValue(response.getBody(), ColorResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.sortMinXMinY(r.getPixels())).isDeepEqualTo(pixels);
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
