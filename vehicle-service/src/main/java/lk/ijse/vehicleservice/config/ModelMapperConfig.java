package lk.ijse.vehicleservice.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Marks this class as a source of bean definitions
public class ModelMapperConfig {

    @Bean // Marks this method as a bean producer
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Configure ModelMapper to use strict matching strategy
        // This helps ensure that properties are mapped only if their names and types match precisely
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(false); // Ensure null values are mapped (important for updates)
        return modelMapper;
    }
}