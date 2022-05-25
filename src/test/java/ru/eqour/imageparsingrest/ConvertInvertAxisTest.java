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
import ru.eqour.imageparsingrest.model.ConvertInvertAxisRequest;
import ru.eqour.imageparsingrest.model.ConvertResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class ConvertInvertAxisTest {

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
        public void convertInvertAxisXTest() throws Exception {
            mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/convert/invertAxisX")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition))))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        public void convertInvertAxisYTest() throws Exception {
            mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/convert/invertAxisY")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition))))
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
        public void convertInvertAxisX() throws Exception {
            MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/convert/invertAxisX")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition))))
                    .andExpect(status().is(200))
                    .andReturn();
            try {
                ConvertResponse r = mapper.readValue(result.getResponse().getContentAsString(), ConvertResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.compareDouble2Array(r.getPoints(), outputPointsInvertedX, DELTA)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }

        @Test
        public void convertInvertAxisY() throws Exception {
            MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/convert/invertAxisY")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ConvertInvertAxisRequest(inputPoints, invertedAxisPosition))))
                    .andExpect(status().is(200))
                    .andReturn();
            try {
                ConvertResponse r = mapper.readValue(result.getResponse().getContentAsString(), ConvertResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.compareDouble2Array(r.getPoints(), outputPointsInvertedY, DELTA)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
