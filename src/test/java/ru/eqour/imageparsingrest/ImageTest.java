package ru.eqour.imageparsingrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import ru.eqour.imageparsingrest.model.ImageRequest;
import ru.eqour.imageparsingrest.model.ImageResponse;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ImageTest {

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void NullImageTest() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/image",
                HttpMethod.POST,
                new HttpEntity<>(new ImageRequest(null)),
                String.class
        );
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    public void BrokenImageTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"image\": {\"base64\": \"broken\"}}", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/image",
                HttpMethod.POST,
                entity,
                String.class
        );
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    public void ValidImageTest() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/image",
                HttpMethod.POST,
                new HttpEntity<>(new ImageRequest(new BufferedImage(5, 5, BufferedImage.TYPE_4BYTE_ABGR))),
                String.class
        );
        try {
            assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
            ImageResponse r = mapper.readValue(response.getBody(), ImageResponse.class);
            assertThat(r).isNotNull();
            assertThat(r.getImageId()).isNotNull();
            assertThat(r.getImageId()).isGreaterThan(-1);
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        }
    }
}
