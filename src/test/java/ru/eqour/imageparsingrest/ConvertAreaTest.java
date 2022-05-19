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
import ru.eqour.imageparsingrest.model.ConvertAreaRequest;
import ru.eqour.imageparsingrest.model.ConvertResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Enclosed.class)
public class ConvertAreaTest {

    private static final double DELTA = 0.005;

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    public static class NegativeParameterizedTests {

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        private TestRestTemplate restTemplate;

        private final double[][] inputPoints;
        private final Double[] parameters;

        public NegativeParameterizedTests(double[][] inputPoints, Double[] parameters) {
            this.parameters = parameters;
            this.inputPoints = inputPoints;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { null, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 1d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 }, { 5, 7 }, { 4, 9 } }, new Double[] { 0d, 1d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 1d, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 1d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, 1d, 1d, 1d, 0d, 1d, 1d } },
                    { new double[][] { { 1, 1, 1 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 1 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { null, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, null, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, null, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, 1d, null, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, 1d, 1d, null, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, null, 1d, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, null, 1d } },
                    { new double[][] { { 0, 0 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, 1d, null } }
            };
        }

        @Test
        public void convertAreaTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/area",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ConvertAreaRequest(
                                    inputPoints,
                                    parameters[0], parameters[1], parameters[2], parameters[3],
                                    parameters[4], parameters[5], parameters[6], parameters[7]
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

        private final double[][] inputPoints;
        private final double[][] outputPoints;
        private final Double[] parameters;

        public PositiveParameterizedTests(double[][] inputPoints, double[][] outputPoints, Double[] parameters) {
            this.inputPoints = inputPoints;
            this.outputPoints = outputPoints;
            this.parameters = parameters;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { new double[][] { { 1, 1 } }, new double[][] { { 1, 1 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 0, 0 }, { 5, 7 }, { 4, 9 } }, new double[][] { { 0, 0 }, { 5, 7 }, { 4, 9 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 6.45, -12.44 } }, new double[][] { { 6.45, -12.44 } }, new Double[] { 0d, 0d, 1d, 1d, 0d, 0d, 1d, 1d } },
                    { new double[][] { { 5, 10 } }, new double[][] { { 0, 10 } }, new Double[] { 5d, 5d, 10d, 10d, 0d, 0d, 10d, 10d } },
                    { new double[][] { { 3, 6 } }, new double[][] { { 3, 4 } }, new Double[] { 0d, 10d, 10d, 0d, 0d, 0d, 10d, 10d } },
                    { new double[][] { { 0, 0 }, { 5, 7 }, { 4, 9 } }, new double[][] { { 0, 10 }, { 5, 3 }, { 4, 1 } }, new Double[] { 0d, 10d, 10d, 0d, 0d, 0d, 10d, 10d } },
                    { new double[][] { { 3, 6 } }, new double[][] { { 0.86, 1.71 } }, new Double[] { 0d, 0d, 7d, 7d, 0d, 0d, 2d, 2d } },
            };
        }

        @Test
        public void convertAreaTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/area",
                    HttpMethod.POST,
                    new HttpEntity<>(
                            new ConvertAreaRequest(
                                    inputPoints,
                                    parameters[0], parameters[1], parameters[2], parameters[3],
                                    parameters[4], parameters[5], parameters[6], parameters[7]
                            )
                    ),
                    String.class
            );
            try {
                assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
                ConvertResponse r = mapper.readValue(response.getBody(), ConvertResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.compareDouble2Array(r.getPoints(), outputPoints, DELTA)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
