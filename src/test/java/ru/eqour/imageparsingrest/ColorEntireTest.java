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
public class ColorEntireTest {

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
        private final Color color;
        private final Double colorDifference;

        public NegativeParameterizedTests(int imageId, Color color, Double colorDifference) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) {
                this.image = null;
            } else {
                this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            }
            this.color = color;
            this.colorDifference = colorDifference;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { -1, Color.BLACK, 10.0 },
                    { 0, null, 10.0 },
                    { 0, Color.BLACK, -1.0 },
                    { 0, Color.BLACK, null }
            };
        }

        @Test
        public void colorEntireTest() throws Exception {
            Long imageId = null;
            if (image != null) {
                MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/image")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(
                                        new ImageRequest(ImageHelper.convertToBase64(image))
                                )))
                        .andReturn();
                imageId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.imageId", Long.class);
            }
            mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/color/entire")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ColorEntireRequest(
                                        image == null ? Long.MAX_VALUE : imageId,
                                        colorDifference,
                                        color == null ? null : ImageColor.fromColor(color)
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
        private final Color color;
        private final Double colorDifference;
        private final int[][] pixels;

        public PositiveParameterizedTests(int imageId, Color color, Double colorDifference, int[][] pixels) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) this.image = null;
            else this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            this.color = color;
            this.colorDifference = colorDifference;
            this.pixels = pixels;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 1, Color.RED, 0.0, new int[][] { { 0, 0 }, { 0, 4 }, { 2, 2 }, { 4, 0 }, { 4, 4 } } },
                    { 2, Color.RED, 0.0, new int[][] {} },
                    { 3, Color.RED, 2.0, new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 } } },
                    { 3, Color.RED, 4.0, new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 } } }
            };
        }

        @Test
        public void colorEntireTest() throws Exception {
            MvcResult iResult = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/image")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ImageRequest(ImageHelper.convertToBase64(image)))))
                    .andReturn();
            Long imageId = JsonPath.parse(iResult.getResponse().getContentAsString()).read("$.imageId", Long.class);
            MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/color/entire")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ColorEntireRequest(
                                    imageId, colorDifference,
                                    ImageColor.fromColor(color)
                            ))))
                    .andExpect(status().is(200))
                    .andReturn();
            try {
                ColorResponse r = mapper.readValue(result.getResponse().getContentAsString(), ColorResponse.class);
                assertThat(r).isNotNull();
                assertThat(TestHelper.sortMinXMinY(r.getPixels())).isDeepEqualTo(pixels);
            } catch (JsonProcessingException e) {
                fail(e.getMessage());
            }
        }
    }
}
