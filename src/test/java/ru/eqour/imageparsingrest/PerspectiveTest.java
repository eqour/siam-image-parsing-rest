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
import ru.eqour.imageparsingrest.model.PerspectiveRequest;
import ru.eqour.imageparsingrest.model.PerspectiveResponse;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class PerspectiveTest {

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
        public void perspectiveTest() throws Exception {
            mvc.perform(post("/perspective")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new PerspectiveRequest(inputImage, points, requiredSize[0], requiredSize[1])
                            )))
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
        public void perspectiveTest() throws Exception {
            MvcResult result = mvc.perform(post("/perspective")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new PerspectiveRequest(inputImage, points, requiredSize[0], requiredSize[1])
                            )))
                    .andExpect(status().is(200))
                    .andReturn();
            try {
                PerspectiveResponse r = mapper.readValue(result.getResponse().getContentAsString(), PerspectiveResponse.class);
                assertThat(r).isNotNull();
                assertThat(r.getImageId()).isNotNull();
                assertThat(TestHelper.compareBufferedImages(r.getImage(), outputImage)).isTrue();
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
