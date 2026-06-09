package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.converter.FavoriteTransferModelAndDtoConverter;
import org.example.converter.TransferModelAndDtoConverter;
import org.example.dto.*;
import org.example.model.FavoriteTransfer;
import org.example.model.Transfer;
import org.example.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Transfers management", description = "API for transfers management")
@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferModelAndDtoConverter transferConverter;
    private final FavoriteTransferModelAndDtoConverter favoriteTransferConverter;
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

    @GetMapping("/statement/{iban}")
    public List<TransferResponseDto> getStatement(
            @PathVariable("iban") String iban,
            @RequestParam("from") LocalDate from,
            @RequestParam("to") LocalDate to) {
        List<Transfer> statement = transferService.getStatement(iban, from, to);
        return transferConverter.toDtos(statement);
    }

    @PostMapping("/favorite")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteTransferResponseDto createFavorite(
            @Valid @RequestBody CreateFavoriteTransferRequestDto createFavoriteTransferRequestDto) {
        FavoriteTransfer favoriteTransfer = favoriteTransferConverter.toModel(createFavoriteTransferRequestDto);
        FavoriteTransfer newFavoriteTransfer = transferService.createFavorite(favoriteTransfer);
        return favoriteTransferConverter.toDto(newFavoriteTransfer);
    }

    @DeleteMapping("/favorite/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavorite(@PathVariable("id") Long id) {
        transferService.deleteFavoriteById(id);
    }

    @GetMapping("/favorite")
    public List<FavoriteTransferResponseDto> getAllFavorite() {
        List<FavoriteTransfer> models = transferService.getAllFavorite();
        return favoriteTransferConverter.toDtos(models);
    }
}