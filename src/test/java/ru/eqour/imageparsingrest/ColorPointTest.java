package ru.eqour.imageparsingrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import ru.eqour.imageparsingrest.helper.ImageHelper;
import ru.eqour.imageparsingrest.helper.TestHelper;
import ru.eqour.imageparsingrest.model.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class ColorPointTest {

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

        private final BufferedImage image;
        private final Integer[] startPoint;
        private final Double colorDifference;
        private final Integer searchRadius;

        public NegativeParameterizedTests(int imageId, Integer[] startPoint,
                                          Double colorDifference, Integer searchRadius) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) this.image = null;
            else this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            this.startPoint = startPoint;
            this.colorDifference = colorDifference;
            this.searchRadius = searchRadius;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { -1, new Integer[] { 0, 0 }, 0.0, 1 },
                    { 0, new Integer[] { -5, 0 }, 0.0, 1 },
                    { 0, new Integer[] { 20, 0 }, 0.0, 1 },
                    { 0, new Integer[] { 0, 0 }, -1.0, 1 },
                    { 0, new Integer[] { null, 0 }, 0.0, 1 },
                    { 0, new Integer[] { 0, null }, 0.0, 1 },
                    { 0, new Integer[] { 0, 0 }, null, 1 },
                    { 0, new Integer[] { 0, 0 }, 0.0, null }
            };
        }

        @Test
        public void colorPointTest() throws Exception {
            Long imageId = null;
            if (image != null) {
                MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/image")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(
                                        new ImageRequest(ImageHelper.convertToBase64(image)))
                                ))
                        .andReturn();
                imageId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.imageId", Long.class);
            }
            mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/color/point")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ColorPointRequest(image == null ? Long.MAX_VALUE : imageId,
                                            colorDifference, startPoint[0], startPoint[1], searchRadius
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

        private final BufferedImage image;
        private final Integer[] startPoint;
        private final Double colorDifference;
        private final Integer searchRadius;

        public PositiveParameterizedTests(int imageId, Integer[] startPoint, Double colorDifference,
                                          Integer searchRadius) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) {
                this.image = null;
            } else {
                this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            }
            this.startPoint = startPoint;
            this.colorDifference = colorDifference;
            this.searchRadius = searchRadius;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 1, new Integer[] { 2, 2 }, 0.0, 3 },
                    { 2, new Integer[] { 2, 2 }, 0.0, 3 },
                    { 2, new Integer[] { 2, 2 }, 1.0, 3 },
                    { 3, new Integer[] { 0, 0 }, 0.0, 1 },
                    { 3, new Integer[] { 0, 0 }, 1.0, 1 },
                    { 3, new Integer[] { 0, 0 }, 4.0, 1 },
                    { 4, new Integer[] { 40, 49 }, 0.0, 15 },
                    { 4, new Integer[] { 40, 49 }, 200.0, 10 }
            };
        }

        @Test
        public void colorPointTest() throws Exception {
            MvcResult iResult = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/image")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ImageRequest(ImageHelper.convertToBase64(image)))))
                    .andReturn();
            Long imageId = JsonPath.parse(iResult.getResponse().getContentAsString()).read("$.imageId", Long.class);
            MvcResult eResult = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/color/entire")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ColorEntireRequest(imageId, colorDifference,
                                            ImageColor.fromColor(new Color(image.getRGB(startPoint[0], startPoint[1])))
                                    ))))
                    .andReturn();
            int[][] pixels = JsonPath.parse(eResult.getResponse().getContentAsString()).read("$.pixels", int[][].class);
            MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/color/point")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ColorPointRequest(imageId, colorDifference, startPoint[0], startPoint[1],
                                            searchRadius
                                    ))))
                    .andExpect(status().is(200))
                    .andReturn();
            try {
                ColorResponse r = mapper.readValue(result.getResponse().getContentAsString(), ColorResponse.class);
                assertThat(r).isNotNull();
                int[][] actual = TestHelper.sortMinXMinY(r.getPixels());
                int[][] expected = TestHelper.sortMinXMinY(pixels);
                assertThat(actual).isDeepEqualTo(expected);
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
