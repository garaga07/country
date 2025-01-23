package guru.qa.country.service;

import guru.qa.country.data.CountryEntity;
import guru.qa.country.data.CountryRepository;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public List<CountryGql> getAllCountriesGql() {
        return countryRepository.findAll()
                .stream()
                .map(entity -> new CountryGql(
                        entity.getId(),
                        entity.getCountryName(),
                        entity.getCountryCode()
                ))
                .toList();
    }

    public Country getCountryById(UUID id) {
        CountryEntity entity = countryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Country with ID " + id + " not found."));
        return Country.fromEntity(entity);
    }

    public CountryGql getCountryGqlById(UUID id) {
        return countryRepository.findById(id)
                .map(entity -> new CountryGql(
                        entity.getId(),
                        entity.getCountryName(),
                        entity.getCountryCode()
                ))
                .orElseThrow(() -> new IllegalArgumentException("Country with ID " + id + " not found."));
    }

    public Country addCountry(Country country) {
        CountryEntity entity = new CountryEntity();
        entity.setCountryName(country.countryName());
        entity.setCountryCode(country.countryCode());
        entity = countryRepository.save(entity);
        return Country.fromEntity(entity);
    }

    public CountryGql addCountryGql(CountryInputGql input) {
        CountryEntity entity = new CountryEntity();
        entity.setCountryName(input.countryName());
        entity.setCountryCode(input.countryCode());
        entity = countryRepository.save(entity);
        return new CountryGql(
                entity.getId(),
                entity.getCountryName(),
                entity.getCountryCode()
        );
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

    public CountryGql updateCountryNameGql(String countryCode, String newCountryName) {
        CountryEntity entity = countryRepository.findByCountryCode(countryCode);
        if (entity == null) {
            throw new IllegalArgumentException("Country with code " + countryCode + " not found.");
        }
        entity.setCountryName(newCountryName);
        entity = countryRepository.save(entity);
        return new CountryGql(
                entity.getId(),
                entity.getCountryName(),
                entity.getCountryCode()
        );
    }
}