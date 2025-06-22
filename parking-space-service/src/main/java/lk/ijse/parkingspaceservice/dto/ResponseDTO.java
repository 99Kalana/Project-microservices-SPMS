package lk.ijse.parkingspaceservice.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO {

    private int status;

    private String message;

    private Object data;
}
