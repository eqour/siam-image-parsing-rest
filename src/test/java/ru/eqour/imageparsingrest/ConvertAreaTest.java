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
import ru.eqour.imageparsingrest.model.ConvertAreaRequest;
import ru.eqour.imageparsingrest.model.ConvertResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class ConvertAreaTest {

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
        public void convertAreaTest() throws Exception {
            mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/convert/area")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ConvertAreaRequest(
                                    inputPoints,
                                    parameters[0], parameters[1], parameters[2], parameters[3],
                                    parameters[4], parameters[5], parameters[6], parameters[7]
                            ))))
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
        public void convertAreaTest() throws Exception {
            MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/convert/area")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ConvertAreaRequest(
                                    inputPoints,
                                    parameters[0], parameters[1], parameters[2], parameters[3],
                                    parameters[4], parameters[5], parameters[6], parameters[7]
                            ))))
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
