package ru.eqour.imageparsingrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.eqour.imageparsingrest.helper.TestHelper;
import ru.eqour.imageparsingrest.model.ConvertInvertRequest;
import ru.eqour.imageparsingrest.model.ConvertResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class ConvertInvertTest {

    private static final double DELTA = 0.005;

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
    @AutoConfigureMockMvc
    public static class NegativeParameterizedTests {

        @ClassRule
        public static final SpringClassRule springClassRule = new SpringClassRule();
        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        public MockMvc mvc;
        @Autowired
        private ObjectMapper mapper;

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
        public void convertInvertTest() throws Exception{
            mvc.perform(post("/convert/invert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ConvertInvertRequest(inputPoints, invertByX, invertByY))))
                    .andExpect(status().is4xxClientError());
        }
    }

    @RunWith(Parameterized.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
    @AutoConfigureMockMvc
    public static class PositiveParameterizedTests {

        @ClassRule
        public static final SpringClassRule springClassRule = new SpringClassRule();
        @Rule
        public final SpringMethodRule springMethodRule = new SpringMethodRule();
        @Autowired
        public MockMvc mvc;
        @Autowired
        private ObjectMapper mapper;

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
        public void convertInvertTest() throws Exception {
            MvcResult result = mvc.perform(post("/convert/invert")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ConvertInvertRequest(inputPoints, invertByX, invertByY))))
                    .andExpect(status().is(200))
                    .andReturn();
            try {
                ConvertResponse r = mapper.readValue(result.getResponse().getContentAsString(), ConvertResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.compareDouble2Array(r.getPoints(), outputPoints, DELTA)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
