package no.itnow.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MissingSessionLocationUpdateTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Test04 Lokasjonsoppdatering feiler når sessionId ikke finnes")
    void test04MissingSessionLocationUpdate() throws Exception {
        mockMvc.perform(post("/api/location/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testDataHelper.withSessionId("fixtures/location-update-request-1.json", "missing-session")))
                .andExpect(status().isNotFound());
    }
}
