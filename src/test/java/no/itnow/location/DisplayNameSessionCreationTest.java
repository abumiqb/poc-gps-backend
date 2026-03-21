package no.itnow.location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DisplayNameSessionCreationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Test01 DisplayName gjøres om til stor forbokstav når session opprettes")
    void test01DisplayNameSessionCreation() throws Exception {
        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testDataHelper.readFixture("fixtures/session-create-request.json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").isNotEmpty())
                .andExpect(jsonPath("$.displayName").value("Ola"))
                .andExpect(jsonPath("$.deviceId").value("pixel-8"))
                .andExpect(jsonPath("$.active").value(true));
    }
}
