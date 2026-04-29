package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.converter.TransferConverter;
import org.example.dto.TransferRequestDto;
import org.example.dto.TransferResponseDto;
import org.example.model.Transfer;
import org.example.service.TransferService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transfers management", description = "API for transfers management")
@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferConverter transferConverter;
    private final TransferService transferService;

    @Operation(
            summary = "Perform the transfer",
            description = "Performs the transfer between specified accounts with specified amount",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "Transfer was successfully performed",
                            content = @Content(schema = @Schema(implementation = TransferResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description =
                            "Specified resource didn't found (example: non-existent IBAN of the account)")
            })
    @PostMapping
    public TransferResponseDto performTransfer(@Valid @RequestBody TransferRequestDto transferRequestDto) {
        Transfer transfer = transferConverter.toModel(transferRequestDto);
        Transfer performedTransfer = transferService.performTransfer(transfer);
        return transferConverter.toDto(performedTransfer);
    }
}