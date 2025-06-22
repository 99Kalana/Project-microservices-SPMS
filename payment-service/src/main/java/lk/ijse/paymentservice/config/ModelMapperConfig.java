package lk.ijse.paymentservice.config;

import lk.ijse.paymentservice.dto.PaymentRequestDTO;
import lk.ijse.paymentservice.dto.PaymentResponseDTO;
import lk.ijse.paymentservice.entity.Payment;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Configure strict matching to avoid unexpected mappings
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Explicitly define mapping for PaymentRequestDTO to Payment entity
        // This is crucial to prevent ModelMapper from trying to auto-map 'expiryDate'
        // or other String fields to LocalDateTime if there's any ambiguity.
        modelMapper.createTypeMap(PaymentRequestDTO.class, Payment.class)
                .addMappings(mapper -> {
                    // Ensure card holder name is mapped
                    mapper.map(PaymentRequestDTO::getCardHolderName, Payment::setCardHolderName);
                    // Ensure expiry date is mapped as string
                    mapper.map(PaymentRequestDTO::getExpiryDate, Payment::setExpiryDate);
                    // Ensure other string fields are mapped if needed
                    mapper.map(PaymentRequestDTO::getPaymentMethod, Payment::setPaymentMethod);
                    // If you don't map cardNumber explicitly, ModelMapper should handle it by name,
                    // but if you want to explicitly say 'mask it during mapping', you'd do it here.
                    // For now, it's masked in the service *after* initial mapping.
                });

        // Also define mapping for Payment entity to PaymentResponseDTO
        modelMapper.createTypeMap(Payment.class, PaymentResponseDTO.class)
                .addMappings(mapper -> {
                    // For example, if you want to populate maskedCardNumber from the entity
                    // mapper.map(Payment::getMaskedCardNumber, PaymentResponseDTO::setMaskedCardNumber);
                });


        return modelMapper;
    }
}