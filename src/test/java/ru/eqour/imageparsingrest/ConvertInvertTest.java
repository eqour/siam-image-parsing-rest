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
import ru.eqour.imageparsingrest.model.ConvertInvertRequest;
import ru.eqour.imageparsingrest.model.ConvertResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Enclosed.class)
public class ConvertInvertTest {

    private static final double DELTA = 0.005;

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
    public static class NegativeParameterizedTests {

        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        private TestRestTemplate restTemplate;

        private final double[][] inputPoints;
        private final Boolean invertByX;
        private final Boolean invertByY;

        public NegativeParameterizedTests(double[][] inputPoints, Boolean invertByX, Boolean invertByY) {
            this.inputPoints = inputPoints;
            this.invertByX = invertByX;
            this.invertByY = invertByY;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { null, false, false },
                    { new double[][] { { 2.56, 8.05, 4.55 } }, false, false },
                    { new double[][] { { 2.56 } }, false, false },
                    { new double[][] { { 2.56, 5.10 } }, null, false },
                    { new double[][] { { 2.56, 5.10 } }, false, null },
                    { new double[][] { { 2.56, 5.10 }, { 1.43 } }, false, true }
            };
        }

        @Test
        public void convertAreaTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/invert",
                    HttpMethod.POST,
                    new HttpEntity<>(new ConvertInvertRequest(inputPoints, invertByX, invertByY)),
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
        private final Boolean invertByX;
        private final Boolean invertByY;

        public PositiveParameterizedTests(double[][] inputPoints, double[][] outputPoints, Boolean invertByX, Boolean invertByY) {
            this.inputPoints = inputPoints;
            this.outputPoints = outputPoints;
            this.invertByX = invertByX;
            this.invertByY = invertByY;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { new double[][] { { 2.56, 8.05 } }, new double[][] { { 2.56, 8.05 } }, false, false },
                    { new double[][] { { 2.56, 8.05 } }, new double[][] { { -2.56, 8.05 } }, true, false },
                    { new double[][] { { 2.56, 8.05 } }, new double[][] { { 2.56, -8.05 } }, false, true },
                    { new double[][] { { 2.56, 8.05 } }, new double[][] { { -2.56, -8.05 } }, true, true }
            };
        }

        @Test
        public void convertAreaTest() {
            ResponseEntity<String> response = restTemplate.exchange(
                    "/convert/invert",
                    HttpMethod.POST,
                    new HttpEntity<>(new ConvertInvertRequest(inputPoints, invertByX, invertByY)),
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
