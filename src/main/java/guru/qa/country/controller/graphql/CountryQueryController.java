package guru.qa.country.controller.graphql;

import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
public class CountryQueryController {

    private final CountryService countryService;

    @Autowired
    public CountryQueryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @QueryMapping
    public List<CountryGql> countries() {
        return countryService.getAllCountriesGql();
    }

    @QueryMapping
    public CountryGql country(@Argument UUID id) {
        return countryService.getCountryGqlById(id);
    }
}