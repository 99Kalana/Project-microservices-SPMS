package lk.ijse.parkingspaceservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Marks this class for Spring configuration
public class ModelMapperConfig {

    @Bean // Marks this method's return value as a Spring Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}