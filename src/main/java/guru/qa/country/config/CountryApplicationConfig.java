package guru.qa.country.config;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import guru.qa.country.service.DateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class CountryApplicationConfig {

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        final SimpleModule module = new SimpleModule("", Version.unknownVersion());
        module.addSerializer(Date.class, new DateSerializer("dd.MM.yyyy"));
        mapper.registerModule(module);
        return mapper;
    }
}
