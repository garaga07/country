package guru.qa.country.service;

import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import guru.qa.country.grpc.*;
import guru.qa.country.model.Country;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class GrpcCountryService extends CountryServiceGrpc.CountryServiceImplBase {

    private final CountryService countryService;

    public GrpcCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public void getAll(Empty request, StreamObserver<CountriesResponse> responseObserver) {
        List<Country> countries = countryService.getAllCountries();
        List<CountryResponse> countryResponses = countries.stream()
                .map(this::convertToCountryResponse)
                .collect(Collectors.toList());

        CountriesResponse response = CountriesResponse.newBuilder()
                .addAllAllCountries(countryResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CountryRequest> addCountry(StreamObserver<CountryCountResponse> responseObserver) {
        AtomicInteger addedCount = new AtomicInteger();

        return new StreamObserver<>() {
            @Override
            public void onNext(CountryRequest countryRequest) {
                Country country = new Country(
                        countryRequest.getCountryName(),
                        countryRequest.getCountryCode(),
                        new Date(countryRequest.getIndependenceDay().getSeconds() * 1000)
                );

                countryService.addCountry(country);
                addedCount.incrementAndGet();
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                CountryCountResponse response = CountryCountResponse.newBuilder()
                        .setCalculatedResponse(addedCount.get())
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void updateCountry(UpdateCountryRequest request, StreamObserver<CountryResponse> responseObserver) {
        String countryCode = request.getCountryCode();
        String newCountryName = request.getNewCountryName();

        Country updatedCountry = countryService.updateCountryName(countryCode, newCountryName);

        CountryResponse response = convertToCountryResponse(updatedCountry);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private CountryResponse convertToCountryResponse(Country country) {
        Timestamp independenceDay = Timestamp.newBuilder()
                .setSeconds(country.independencyDate().getTime() / 1000)
                .build();

        return CountryResponse.newBuilder()
                .setCountryName(country.countryName())
                .setCountryCode(country.countryCode())
                .setIndependenceDay(independenceDay)
                .build();
    }
}