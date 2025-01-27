package guru.qa.country.service.soap;

import guru.qa.country.config.AppConfig;
import guru.qa.country.domain.graphql.CountryGql;
import guru.qa.country.domain.graphql.CountryInputGql;
import guru.qa.country.service.CountryService;
import guru.qa.xml.country.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.UUID;
import java.util.stream.Collectors;

@Endpoint
public class CountryEndpoint {

    private final CountryService countryService;

    public CountryEndpoint(CountryService countryService) {
        this.countryService = countryService;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "GetAllRequest")
    @ResponsePayload
    public GetAllResponse getAllCountries() {
        GetAllResponse response = new GetAllResponse();
        response.getCountry().addAll(
                countryService.getAllCountriesGql().stream().map(countryGql -> {
                    CountryResponse countryResponse = new CountryResponse();
                    countryResponse.setCountryName(countryGql.countryName());
                    countryResponse.setCountryCode(countryGql.countryCode());
                    return countryResponse;
                }).collect(Collectors.toList())
        );
        return response;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "GetCountryByIdRequest")
    @ResponsePayload
    public GetCountryByIdResponse getCountryById(@RequestPayload GetCountryByIdRequest request) {
        CountryGql countryGql = countryService.getCountryGqlById(UUID.fromString(request.getId()));
        GetCountryByIdResponse response = new GetCountryByIdResponse();
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountryName(countryGql.countryName());
        countryResponse.setCountryCode(countryGql.countryCode());
        response.setCountry(countryResponse);
        return response;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "AddCountryRequest")
    @ResponsePayload
    public AddCountryResponse addCountry(@RequestPayload AddCountryRequest request) {
        CountryInputGql countryInput = new CountryInputGql(
                request.getCountryName(),
                request.getCountryCode()
        );
        CountryGql addedCountry = countryService.addCountryGql(countryInput);
        AddCountryResponse response = new AddCountryResponse();
        response.setSuccess(addedCountry != null);
        return response;
    }

    @PayloadRoot(namespace = AppConfig.SOAP_NAMESPACE, localPart = "UpdateCountryRequest")
    @ResponsePayload
    public UpdateCountryResponse updateCountry(@RequestPayload UpdateCountryRequest request) {
        CountryGql updatedCountry = countryService.updateCountryNameGql(
                request.getCountryCode(),
                request.getNewCountryName()
        );
        UpdateCountryResponse response = new UpdateCountryResponse();
        CountryResponse countryResponse = new CountryResponse();
        countryResponse.setCountryName(updatedCountry.countryName());
        countryResponse.setCountryCode(updatedCountry.countryCode());
        response.setCountry(countryResponse);
        return response;
    }
}