package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBalanceRequestDto {

    @Schema(
            description = "IBAN of the account",
            example = "IBANQEDESSDGPEB3FR9H8QZ2MOT1S58A",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "IBAN is required to get balance")
    String iban;
}
