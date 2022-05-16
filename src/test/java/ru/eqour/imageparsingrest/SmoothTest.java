package ru.eqour.imageparsingrest;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.eqour.imageparsingrest.model.SmoothRequest;
import ru.eqour.imageparsingrest.model.SmoothResponse;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SmoothTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void givenInvalidRequestExpectedErrorResponse1() {
		ResponseEntity<String> response = restTemplate.exchange("/smooth", HttpMethod.POST,
				new HttpEntity<>(new SmoothRequest(null, null)), String.class);
		assertThat(response.getStatusCode().is4xxClientError()).isTrue();
	}

	@Test
	public void givenInvalidRequestExpectedErrorResponse2() {
		ResponseEntity<String> response = restTemplate.exchange("/smooth", HttpMethod.POST,
				new HttpEntity<>(""), String.class);
		assertThat(response.getStatusCode().is4xxClientError()).isTrue();
	}

	@Test
	public void givenValidRequestExpectedOkResponse1() {
		ResponseEntity<SmoothResponse> response = restTemplate.exchange("/smooth", HttpMethod.POST,
				new HttpEntity<>(new SmoothRequest(1,
						new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } })
				),
				SmoothResponse.class);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(Objects.requireNonNull(response.getBody()).getIteration()).isEqualTo(1);
		assertThat(response.getBody().getPoints()).isDeepEqualTo(new int[][] { { 1, 1, } });
	}

	@Test
	public void givenValidRequestExpectedOkResponse2() {
		ResponseEntity<SmoothResponse> response = restTemplate.exchange("/smooth", HttpMethod.POST,
				new HttpEntity<>(new SmoothRequest(2,
						new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } })
				),
				SmoothResponse.class);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(Objects.requireNonNull(response.getBody()).getIteration()).isEqualTo(2);
		assertThat(response.getBody().getPoints()).isDeepEqualTo(new int[][] { { 1, 1, } });
	}

	@Test
	public void givenValidRequestExpectedOkResponse3() {
		ResponseEntity<SmoothResponse> response = restTemplate.exchange("/smooth", HttpMethod.POST,
				new HttpEntity<>(new SmoothRequest(3,
						new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } })
				),
				SmoothResponse.class);
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(Objects.requireNonNull(response.getBody()).getIteration()).isEqualTo(2);
		assertThat(response.getBody().getPoints()).isDeepEqualTo(new int[][] { { 1, 1, } });
	}
}
