package guru.qa.country;

import guru.qa.country.data.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void shouldAddAndGetCountry() throws Exception {
        // Проверяем добавление страны
        mockMvc.perform(post("/countries/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "country_name": "TestCountry",
                                    "country_code": "RU"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.country_name").value("TestCountry"))
                .andExpect(jsonPath("$.country_code").value("RU"));

        mockMvc.perform(patch("/countries/update/RU")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "country_name": "Россия"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country_name").value("Россия"))
                .andExpect(jsonPath("$.country_code").value("RU"));


        // Проверяем получение стран
        mockMvc.perform(get("/countries/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country_name").value("Россия"))
                .andExpect(jsonPath("$[0].country_code").value("RU"));
    }
}