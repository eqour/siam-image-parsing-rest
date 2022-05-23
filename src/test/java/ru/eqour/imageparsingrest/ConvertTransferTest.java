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
import ru.eqour.imageparsingrest.model.ConvertModifyRequest;
import ru.eqour.imageparsingrest.model.ConvertResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class ConvertTransferTest {

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
        private final Double dx;
        private final Double dy;

        public NegativeParameterizedTests(double[][] inputPoints, Double dx, Double dy) {
            this.inputPoints = inputPoints;
            this.dx = dx;
            this.dy = dy;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { null, 0.0, 0.0 },
                    { new double[][] { { 1.34, 9.67, 3.45 } }, 0.0, 0.0 },
                    { new double[][] { { -5.66 } }, 0.0, 0.0 },
                    { new double[][] { { 2.13, 3.45 } }, null, 0.0 },
                    { new double[][] { { 2.13, 3.45 } }, 0.0, null }
            };
        }

        @Test
        public void convertTransferTest() throws Exception {
            mvc.perform(post("/convert/transfer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ConvertModifyRequest(inputPoints, dx, dy))))
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
        private final Double dx;
        private final Double dy;

        public PositiveParameterizedTests(double[][] inputPoints, double[][] outputPoints, Double dx, Double dy) {
            this.inputPoints = inputPoints;
            this.outputPoints = outputPoints;
            this.dx = dx;
            this.dy = dy;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { new double[][] { { 4.30, 2.53 } }, new double[][] { { 10, 10 } }, 5.7, 7.47 },
                    { new double[][] { { 4.30, 2.53 } }, new double[][] { { 4.30, 2.53 } }, 0.0, 0.0 },
                    { new double[][] { { 4.30, 2.53 } }, new double[][] { { 0, 0 } }, -4.30, -2.53 },
                    { new double[][] { { 4.30, 2.53 }, { 2, -3 } }, new double[][] { { 0, 0 }, { -2.3, -5.53 } }, -4.30, -2.53 }
            };
        }

        @Test
        public void convertTransferTest() throws Exception {
            MvcResult result = mvc.perform(post("/convert/transfer")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ConvertModifyRequest(inputPoints, dx, dy))))
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
