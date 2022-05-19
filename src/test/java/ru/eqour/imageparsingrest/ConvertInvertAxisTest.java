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
import ru.eqour.imageparsingrest.model.ConvertInvertAxisRequest;
import ru.eqour.imageparsingrest.model.ConvertResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Enclosed.class)
public class ConvertInvertAxisTest {

    private static final double DELTA = 0.005;

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    public static class NegativeParameterizedTests {

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        private TestRestTemplate restTemplate;

        private final double[][] inputPoints;
        private final Double invertedAxisPosition;

        public NegativeParameterizedTests(double[][] inputPoints, Double invertedAxisPosition) {
            this.inputPoints = inputPoints;
            this.invertedAxisPosition = invertedAxisPosition;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { null, 0.0 },
                    { new double[][] { { 1.34, 9.67, 3.45 } }, 0.0 },
                    { new double[][] { { -5.66 } }, 0.0 },
                    { new double[][] { { -5.66, 4.65 } }, null }
            };
        }

        @Test
        public void convertInvertAxisXTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/invertAxisX",
                    HttpMethod.POST,
                    new HttpEntity<>(new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition)),
                    String.class
            );
            assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        }

        @Test
        public void convertInvertAxisYTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/invertAxisY",
                    HttpMethod.POST,
                    new HttpEntity<>(new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition)),
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

        private final double[][] inputPoints;
        private final Double invertedAxisPosition;
        private final double[][] outputPointsInvertedX;
        private final double[][] outputPointsInvertedY;

        public PositiveParameterizedTests(double[][] inputPoints,
                                          Double invertedAxisPosition,
                                          double[][] outputPointsInvertedX,
                                          double[][] outputPointsInvertedY) {
            this.inputPoints = inputPoints;
            this.invertedAxisPosition = invertedAxisPosition;
            this.outputPointsInvertedX = outputPointsInvertedX;
            this.outputPointsInvertedY = outputPointsInvertedY;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { new double[][] { { 4, 7 } }, 10.0, new double[][] { { 6, 7 } }, new double[][] { { 4, 3 } } },
                    { new double[][] { { -5, 12 }, { 0, 10 } }, 10.0, new double[][] { { 15, 12 }, { 10, 10 } }, new double[][] { { -5, -2 }, { 0, 0 } } },
                    { new double[][] { { 4, 7 } }, 5.0, new double[][] { { 1, 7 } }, new double[][] { { 4, -2 } } },
                    { new double[][] { { 4, 7 } }, 5.34, new double[][] { { 1.34, 7 } }, new double[][] { { 4, -1.66 } } }
            };
        }

        @Test
        public void convertInvertAxisX() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/invertAxisX",
                    HttpMethod.POST,
                    new HttpEntity<>(new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition)),
                    String.class
            );
            try {
                assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
                ConvertResponse r = mapper.readValue(response.getBody(), ConvertResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.compareDouble2Array(r.getPoints(), outputPointsInvertedX, DELTA)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }

        @Test
        public void convertInvertAxisY() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/invertAxisY",
                    HttpMethod.POST,
                    new HttpEntity<>(new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition)),
                    String.class
            );
            try {
                assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
                ConvertResponse r = mapper.readValue(response.getBody(), ConvertResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.compareDouble2Array(r.getPoints(), outputPointsInvertedY, DELTA)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
