package guru.qa.country.service;

import guru.qa.country.data.CountryRepository;
import guru.qa.country.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
}
