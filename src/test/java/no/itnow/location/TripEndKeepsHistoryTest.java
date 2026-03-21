package no.itnow.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TripEndKeepsHistoryTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Test03 Når turen avsluttes fjernes live-posisjon men historikken beholdes")
    void test03TripEndKeepsHistory() throws Exception {
        String sessionId = testDataHelper.createSessionAndGetSessionId(mockMvc);

        mockMvc.perform(post("/api/location/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testDataHelper.withSessionId("fixtures/location-update-request-1.json", sessionId)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/sessions/{sessionId}/end", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(get("/api/location/latest/{sessionId}", sessionId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/location/history/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sessionId").value(sessionId));
    }
}
