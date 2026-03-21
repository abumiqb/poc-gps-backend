package no.itnow.location;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
class TestDataHelper {

    private final ObjectMapper objectMapper;

    TestDataHelper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    String createSessionAndGetSessionId(MockMvc mockMvc) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(readFixture("fixtures/session-create-request.json")))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        String sessionId = body.get("sessionId").asText();
        assertThat(sessionId).isNotBlank();
        return sessionId;
    }

    String withSessionId(String fixturePath, String sessionId) throws IOException {
        return readFixture(fixturePath).replace("__SESSION_ID__", sessionId);
    }

    String readFixture(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
