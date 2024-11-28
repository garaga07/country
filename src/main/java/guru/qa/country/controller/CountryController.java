package guru.qa.country.controller;

import guru.qa.country.dto.UpdateCountryNameRequest;
import guru.qa.country.model.Country;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Country> getAll() {
        return countryService.getAllCountries();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Country addCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @PatchMapping("/update/{countryCode}")
    @ResponseStatus(HttpStatus.OK)
    public Country updateCountryName(
            @PathVariable String countryCode,
            @RequestBody UpdateCountryNameRequest request) {
        return countryService.updateCountryName(countryCode, request.getNewCountryName());
    }
}