package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private CountryService countryService;

    @BeforeEach
    void beforeEach(){
        Country country = Country.builder()
                .name("Example")
                .languages(Map.of("", "Language"))
                .borders(List.of("", "", "", ""))
                .code("EXA")
                .region("Continent")
                .build();
        Country country2 = Country.builder()
                .name("Exam")
                .languages(Map.of("1", "Language", "2", "AnotherLanguage"))
                .borders(List.of("", ""))
                .code("EXM")
                .region("Continent2")
                .build();
        Country country3 = Country.builder()
                .name("Test")
                .languages(Map.of("", "AnotherLanguage"))
                .borders(List.of(""))
                .code("PXA")
                .region("AnotherContinent")
                .build();
        Country country4 = Country.builder()
                .name("Testing")
                .borders(List.of())
                .code("EZA")
                .region("Continent")
                .build();
        Country country5 = Country.builder()
                .name("Sample")
                .languages(Map.of("", "Language2"))
                .borders(List.of("", "", "", "", ""))
                .code("POS")
                .region("Continent")
                .build();
        when(countryService.getAllCountries()).thenReturn(List.of(country, country2, country3, country4, country5));
    }
    @Test
    void getAll() throws Exception {
        this.mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].code").value("EXA"))
                .andExpect(jsonPath("$[4].code").value("POS"));
        this.mockMvc.perform(get("/api/countries?name=example"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("EXA"));
        this.mockMvc.perform(get("/api/countries?name=EXA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("EXA"))
                .andExpect(jsonPath("$[1].code").value("EXM"));
        this.mockMvc.perform(get("/api/countries?code=POS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("POS"));
        this.mockMvc.perform(get("/api/countries?code=e"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].code").value("EXA"))
                .andExpect(jsonPath("$[2].code").value("EZA"));
        this.mockMvc.perform(get("/api/countries?name=ex&code=ex"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("EXA"))
                .andExpect(jsonPath("$[1].code").value("EXM"));
    }

    @Test
    void getByContinent() throws Exception{
        this.mockMvc.perform(get("/api/countries/Continent/continent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].code").value("EXA"))
                .andExpect(jsonPath("$[2].code").value("POS"));
    }

    @Test
    void getByLanguage() throws Exception{
        this.mockMvc.perform(get("/api/countries/Language/language"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].code").value("EXA"))
                .andExpect(jsonPath("$[1].code").value("EXM"));
    }

    @Test
    void getByMostBorders() throws Exception{
        this.mockMvc.perform(get("/api/countries/most-borders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("POS"));
    }
}