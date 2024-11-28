package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll()
                .stream()
                .map(Country::fromEntity)
                .toList();
    }

    public Country addCountry(Country country) {
        CountryEntity entity = new CountryEntity();
        entity.setCountryName(country.countryName());
        entity.setCountryCode(country.countryCode());
        entity = countryRepository.save(entity);
        return Country.fromEntity(entity);
    }

    public Country updateCountryName(String countryCode, String newCountryName) {
        CountryEntity entity = countryRepository.findByCountryCode(countryCode);
        if (entity == null) {
            throw new IllegalArgumentException("Country with code " + countryCode + " not found.");
        }
        entity.setCountryName(newCountryName);
        entity = countryRepository.save(entity);
        return Country.fromEntity(entity);
    }
}