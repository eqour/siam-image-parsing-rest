package ru.eqour.imageparsingrest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import ru.eqour.imageparsingrest.model.SmoothRequest;
import ru.eqour.imageparsingrest.model.SmoothResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Enclosed.class)
public class SmoothTest {

	@RunWith(Parameterized.class)
	@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
	public static class NegativeParameterizedTests {

		@Rule
		public final SpringMethodRule springMethodRule = new SpringMethodRule();
		@Autowired
		private TestRestTemplate restTemplate;

		private final int[][] points;
		private final Integer maxIteration;

		public NegativeParameterizedTests(int[][] points, Integer maxIteration) {
			this.points = points;
			this.maxIteration = maxIteration;
		}

		@Parameterized.Parameters
		public static Object[][] getParameters() {
			return new Object[][] {
					{ null, 1 },
					{ new int[][] { null }, 1 },
					{ new int[][] { { 1, 5, 3 } }, 1 },
					{ new int[][] { { 1 } }, 1 },
					{ new int[5][0], 1 },
					{ new int[0][0], null },
					{ new int[0][0], 0 },
					{ new int[0][0], -1 }
			};
		}

		@Test
		public void smoothTest() {
			ResponseEntity<String> response = restTemplate.exchange(
					"/smooth",
					HttpMethod.POST,
					new HttpEntity<>(new SmoothRequest(maxIteration, points)),
					String.class
			);
			assertThat(response.getStatusCode().is4xxClientError()).isTrue();
		}
	}

	@RunWith(Parameterized.class)
	@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
	public static class PositiveParameterizedTests {

		@Rule
		public final SpringMethodRule springMethodRule = new SpringMethodRule();
		@Autowired
		private ObjectMapper mapper;
		@Autowired
		private TestRestTemplate restTemplate;

		private final int[][] requestPoints;
		private final int[][] responsePoints;
		private final Integer requestMaxIteration;
		private final Integer responseMaxIteration;

		public PositiveParameterizedTests(int[][] requestPoints, int[][] responsePoints,
										  Integer requestMaxIteration, Integer responseMaxIteration) {
			this.requestPoints = requestPoints;
			this.responsePoints = responsePoints;
			this.requestMaxIteration = requestMaxIteration;
			this.responseMaxIteration = responseMaxIteration;
		}

		@Parameterized.Parameters
		public static Object[][] getParameters() {
			return new Object[][] {
					{ new int[0][0], new int[0][0], 5, 1},
					{ new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } }, new int[][] { { 1, 1 } }, 1, 1 },
					{ new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } }, new int[][] { { 1, 1 } }, 2, 2 },
					{ new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } }, new int[][] { { 1, 1 } }, 3, 2 }
			};
		}

		@Test
		public void smoothTest() {
			ResponseEntity<String> response = restTemplate.exchange(
					"/smooth",
					HttpMethod.POST,
					new HttpEntity<>(new SmoothRequest(requestMaxIteration, requestPoints)),
					String.class
			);
			try {
				assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
				SmoothResponse r = mapper.readValue(response.getBody(), SmoothResponse.class);
				assertThat(r).isNotNull();
				assertThat(r.getIteration()).isEqualTo(responseMaxIteration);
				assertThat(r.getPoints()).isDeepEqualTo(responsePoints);
			} catch (JsonProcessingException e) {
				fail(e.getMessage());
			}
		}
	}
}
