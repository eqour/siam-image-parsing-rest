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
import ru.eqour.imageparsingrest.helper.TestHelper;
import ru.eqour.imageparsingrest.model.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class ColorAreaTest {

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
        private final Integer[] selectedArea;

        public NegativeParameterizedTests(int imageId, Color color, Double colorDifference, Integer[] selectedArea) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) {
                this.image = null;
            } else {
                this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            }
            this.color = color;
            this.colorDifference = colorDifference;
            this.selectedArea = selectedArea;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 0, Color.BLACK, 10.0, new Integer[] { -1, 2, 0, 2 } },
                    { 0, Color.BLACK, 10.0, new Integer[] { 0, 2, 0, 9 } },
                    { -1, Color.BLACK, 10.0, new Integer[] { 0, 0, 2, 2 } },
                    { 0, null, 10.0, new Integer[] { 0, 0, 2, 2 } },
                    { 0, Color.BLACK, null, new Integer[] { 0, 0, 2, 2 } },
                    { 0, Color.BLACK, -1.0, new Integer[] { 0, 0, 2, 2 } },
                    { 0, Color.BLACK, 10.0, new Integer[] { 0, null, 2, null } },
            };
        }

        @Test
        public void colorAreaTest() throws Exception {
            Long imageId = null;
            if (image != null) {
                MvcResult result = mvc.perform(post("/image")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(new ImageRequest(image))))
                        .andReturn();
                imageId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.imageId", Long.class);
            }
            mvc.perform(post("/color/area")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ColorAreaRequest(image == null ? Long.MAX_VALUE : imageId,
                                            colorDifference, color,
                                            selectedArea[0], selectedArea[1], selectedArea[2], selectedArea[3]
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

        private final Integer[] selectedArea;

        public PositiveParameterizedTests(int imageId, Color color, Double colorDifference,
                                          Integer[] selectedArea, int[][] pixels) {
            TestHelper helper = new TestHelper();
            if (imageId < 0) this.image = null;
            else this.image = helper.loadBufferedImageFromResources("ColorSelectorTest-input-" + imageId + ".png");
            this.color = color;
            this.colorDifference = colorDifference;
            this.pixels = pixels;
            this.selectedArea = selectedArea;
        }

        @Parameterized.Parameters
        public static Object[][] getParameters() {
            return new Object[][] {
                    { 0, Color.RED, 0.0, new Integer[] { 0, 0, 2, 2 }, new int[][] {} },
                    { 0, Color.RED, 500.0, new Integer[] { 1, 1, 1, 1 }, new int[][] { { 1, 1 } } },
                    { 1, Color.RED, 0.0, new Integer[] { 1, 1, 3, 3 }, new int[][] { { 2, 2 } } },
                    { 3, Color.RED, 2.0, new Integer[] { 0, 2, 0, 2 }, new int[][] { { 0, 2 } } },
            };
        }

        @Test
        public void colorAreaTest() throws Exception {
            MvcResult iResult = mvc.perform(post("/image")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(new ImageRequest(image))))
                    .andReturn();
            Long imageId = JsonPath.parse(iResult.getResponse().getContentAsString()).read("$.imageId", Long.class);
            MvcResult result = mvc.perform(post("/color/area")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(
                                    new ColorAreaRequest(imageId, colorDifference, color,
                                            selectedArea[0], selectedArea[1], selectedArea[2], selectedArea[3]
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
