package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.CountryDto;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CountryController {
    @Autowired
    private final CountryService countryService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getAll(@RequestParam(required = false) String name, @RequestParam(required = false) String code) {
        List<CountryDto> countryDtos = new ArrayList<>();
        List<Country> countries = countryService.getAllCountries();

        if (name != null)
            countries = countryService.filterCountriesByName(countries, name);
        if (code != null)
            countries = countryService.filterCountriesByCode(countries, code);

        countries.forEach(c -> countryDtos.add(modelMapper.map(c, CountryDto.class)));
        return ResponseEntity.ok(countryDtos);
    }
    @GetMapping("/countries/{continent}/continent")
    public ResponseEntity<List<CountryDto>> getByContinent(@PathVariable String continent) {
        List<CountryDto> countryDtos = new ArrayList<>();
        List<Country> countries = countryService.getAllCountries();
        countries = countryService.filterCountriesByContinent(countries, continent);
        countries.forEach(c -> countryDtos.add(modelMapper.map(c, CountryDto.class)));
        return ResponseEntity.ok(countryDtos);
    }
    @GetMapping("/countries/{language}/language")
    public ResponseEntity<List<CountryDto>> getByLanguage(@PathVariable String language) {
        List<CountryDto> countryDtos = new ArrayList<>();
        List<Country> countries = countryService.getAllCountries();
        countries = countryService.filterCountriesByLanguage(countries, language);
        countries.forEach(c -> countryDtos.add(modelMapper.map(c, CountryDto.class)));
        return ResponseEntity.ok(countryDtos);
    }
    @GetMapping("/countries/most-borders")
    public ResponseEntity<CountryDto> getByMostBorders() {
        List<Country> countries = countryService.getAllCountries();
        CountryDto countryDto = modelMapper.map(countryService.getCountryWithMostBorders(countries), CountryDto.class);
        return ResponseEntity.ok(countryDto);
    }
}