package ru.eqour.imageparsingrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
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
import ru.eqour.imageparsingrest.model.ImageRequest;
import ru.eqour.imageparsingrest.model.ImageResponse;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ImageTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Autowired
    public MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void NullImageTest() throws Exception {
        mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new ImageRequest(null))))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Некорректный запрос"));
    }

    @Test
    public void BrokenImageTest() throws Exception {
        mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"image\": \"broken\"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Не удалось конвертировать изображение"));
    }

    @Test
    public void ValidImageTest() throws Exception {
        MvcResult result = mvc.perform(post(TestHelper.REST_CONTROLLER_MAPPING + "/image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new ImageRequest(
                                        ImageHelper.convertToBase64(
                                                new BufferedImage(5, 5, BufferedImage.TYPE_4BYTE_ABGR)
                                        )
                                ))))
                .andExpect(status().is(200))
                .andReturn();
        try {
            ImageResponse r = mapper.readValue(result.getResponse().getContentAsString(), ImageResponse.class);
            assertThat(r).isNotNull();
            assertThat(r.getImageId()).isNotNull();
            assertThat(r.getImageId()).isGreaterThan(-1);
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
    }
}
