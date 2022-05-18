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
import ru.eqour.imageparsingrest.model.PerspectiveRequest;
import ru.eqour.imageparsingrest.model.PerspectiveResponse;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Enclosed.class)
public class PerspectiveTest {

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    public static class NegativeParameterizedTests {

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        private TestRestTemplate restTemplate;

        private final BufferedImage inputImage;
        private final int[][] points;
        private final Integer[] requiredSize;

        public NegativeParameterizedTests(int imageId, int[][] points, Integer[] requiredSize) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) this.inputImage = null;
            else this.inputImage = helper.loadBufferedImageFromResources("pct-input-" + imageId + ".png");
            this.points = points;
            this.requiredSize = requiredSize;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { -1, null, new Integer[] { 1, 1 } },
                    { -1, new int[0][0], new Integer[] { 1, 1 } },
                    { 0, null, new Integer[] { 1, 1 } },
                    { 0, new int[0][0], new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 }, { 2, 0 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 1, 0 }, { 1 }, { 0, 1 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 2, 1 }, { 0, 1 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 0, 1 }, null, { 1, 1 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 1, 1 }, { 1, 0 }, { 1, 1 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }, new Integer[] { 65537, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }, new Integer[] { 1, -1 } },
                    { 0, new int[][] { { 3, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }, new Integer[] { 1, 1 } },
                    { 0, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } }, new Integer[] { null, 3 } },
                    { 0, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } }, new Integer[] { 3, null } },
                    { 0, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } }, new Integer[] { null, null } }
            };
        }

        @Test
        public void perspectiveTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/perspective",
                    HttpMethod.POST,
                    new HttpEntity<>(new PerspectiveRequest(inputImage, points, requiredSize[0], requiredSize[1])),
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

        private final BufferedImage inputImage;
        private final BufferedImage outputImage;
        private final int[][] points;
        private final Integer[] requiredSize;

        public PositiveParameterizedTests(int imageId, int[][] points, Integer[] requiredSize) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) {
                this.inputImage = this.outputImage = null;
            } else {
                this.inputImage = helper.loadBufferedImageFromResources("pct-input-" + imageId + ".png");
                this.outputImage = helper.loadBufferedImageFromResources("pct-output-" + imageId + ".png");
            }
            this.points = points;
            this.requiredSize = requiredSize;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 0, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } }, new Integer[] { 3, 3 } },
                    { 1, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } }, new Integer[] { 2, 7 } },
                    { 2, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } }, new Integer[] { 8, 4 } },
                    { 3, new int[][] { { 0, 4 }, { 9, 0 }, { 9, 9 }, { 0, 5 } }, new Integer[] { 10, 10 } },
                    { 4, new int[][] { { 0, 4 }, { 9, 0 }, { 9, 9 }, { 0, 5 } }, new Integer[] { 10, 10 } },
                    { 5, new int[][] { { 0, 0 }, { 15, 0 }, { 10, 13 }, { 5, 13 } }, new Integer[] { 5, 5 } },
                    { 6, new int[][] { { 0, 7 }, { 7, 0 }, { 7, 7 }, { 0, 0 } }, new Integer[] { 8, 8 } }
            };
        }

        @Test
        public void perspectiveTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/perspective",
                    HttpMethod.POST,
                    new HttpEntity<>(new PerspectiveRequest(inputImage, points, requiredSize[0], requiredSize[1])),
                    String.class
            );
            try {
                assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
                PerspectiveResponse r = mapper.readValue(response.getBody(), PerspectiveResponse.class);
                assertThat(r).isNotNull();
                assertThat(r.getImageId()).isNotNull();
                assertThat(TestHelper.compareBufferedImages(r.getImage(), outputImage)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
