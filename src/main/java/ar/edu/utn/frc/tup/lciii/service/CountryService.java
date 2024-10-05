package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.CountryDto;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
        private final CountryRepository countryRepository;
        @Autowired
        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        public List<Country> filterCountriesByName(Collection<Country> countries, String name) {
                return countries.stream().filter(c -> c.getName().toLowerCase().startsWith(name.toLowerCase())).toList();
        }
        public List<Country> filterCountriesByCode(Collection<Country> countries, String code) {
                return countries.stream().filter(c -> c.getCode().toLowerCase().startsWith(code.toLowerCase())).toList();
        }
        public List<Country> filterCountriesByContinent(Collection<Country> countries, String continent) {
                return countries.stream().filter(c -> c.getRegion().equalsIgnoreCase(continent)).toList();
        }
        public List<Country> filterCountriesByLanguage (Collection<Country> countries, String language) {
                return countries.stream().filter(c -> c.getLanguages() != null && c.getLanguages().containsValue(language)).toList();
        }
        public Country getCountryWithMostBorders(Collection<Country> countries){
                Country[] countriesArray = countries.toArray(new Country[0]);
                Country mostBorders = null;
                for (Country country : countriesArray) {
                        if (country.getBorders() == null)
                                continue;
                        if (mostBorders == null)
                                mostBorders = country;
                        if (country.getBorders().size() > mostBorders.getBorders().size())
                                mostBorders = country;
                }
                return mostBorders;
        }
        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .code((String) countryData.get("cca3"))
                        .borders((List<String>) countryData.get("borders"))
                        .build();
        }


        private CountryDto mapToDTO(Country country) {
                return new CountryDto(country.getCode(), country.getName());
        }
}