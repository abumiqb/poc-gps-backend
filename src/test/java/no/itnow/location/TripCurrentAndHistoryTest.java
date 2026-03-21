package no.itnow.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TripCurrentAndHistoryTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Test02 Siste punkt overskrives mens historikken beholder alle punkter")
    void test02TripCurrentAndHistory() throws Exception {
        String sessionId = testDataHelper.createSessionAndGetSessionId(mockMvc);

        mockMvc.perform(post("/api/location/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testDataHelper.withSessionId("fixtures/location-update-request-1.json", sessionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId))
                .andExpect(jsonPath("$.displayName").value("Ola"))
                .andExpect(jsonPath("$.latitude").value(59.9139))
                .andExpect(jsonPath("$.longitude").value(10.7522));

        mockMvc.perform(post("/api/location/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testDataHelper.withSessionId("fixtures/location-update-request-2.json", sessionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId))
                .andExpect(jsonPath("$.latitude").value(59.915))
                .andExpect(jsonPath("$.longitude").value(10.755));

        mockMvc.perform(get("/api/location/latest/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(sessionId))
                .andExpect(jsonPath("$.latitude").value(59.915))
                .andExpect(jsonPath("$.longitude").value(10.755));

        mockMvc.perform(get("/api/location/history/{sessionId}", sessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].latitude").value(59.9139))
                .andExpect(jsonPath("$[0].longitude").value(10.7522))
                .andExpect(jsonPath("$[1].latitude").value(59.915))
                .andExpect(jsonPath("$[1].longitude").value(10.755));
    }
}
