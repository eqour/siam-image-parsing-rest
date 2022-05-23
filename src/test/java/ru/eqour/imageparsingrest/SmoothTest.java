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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.*;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.eqour.imageparsingrest.model.SmoothRequest;
import ru.eqour.imageparsingrest.model.SmoothResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Enclosed.class)
public class SmoothTest {

	@RunWith(Parameterized.class)
	@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
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
		public void smoothTest() throws Exception {
			mvc.perform(post("/smooth")
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(new SmoothRequest(maxIteration, points))))
					.andExpect(status().is4xxClientError());
		}
	}

	@RunWith(Parameterized.class)
	@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
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
					{ new int[0][0], new int[0][0], 5, 1 },
					{ new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } }, new int[][] { { 1, 1 } }, 1, 1 },
					{ new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } }, new int[][] { { 1, 1 } }, 2, 2 },
					{ new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 1, 0 }, { 1, 1 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } }, new int[][] { { 1, 1 } }, 3, 2 }
			};
		}

		@Test
		public void smoothTest() throws Exception {
			MvcResult result = mvc.perform(post("/smooth")
							.contentType(MediaType.APPLICATION_JSON)
							.content(mapper.writeValueAsString(new SmoothRequest(requestMaxIteration, requestPoints))))
					.andExpect(status().is(200))
					.andReturn();
			try {
				SmoothResponse r = mapper.readValue(result.getResponse().getContentAsString(), SmoothResponse.class);
				assertThat(r).isNotNull();
				assertThat(r.getIteration()).isEqualTo(responseMaxIteration);
				assertThat(r.getPoints()).isDeepEqualTo(responsePoints);
			} catch (JsonProcessingException e) {
				fail(e.getMessage());
			}
		}
	}
}
