package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.converter.TransferConverter;
import org.example.dto.TransferRequestDto;
import org.example.dto.TransferResponseDto;
import org.example.model.Transfer;
import org.example.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferConverter transferConverter;
    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponseDto performTransfer(@Valid @RequestBody TransferRequestDto transferRequestDto) {
        Transfer transfer = transferConverter.toModel(transferRequestDto);
        Transfer performedTransfer = transferService.performTransfer(transfer);
        return transferConverter.toDto(performedTransfer);
    }
}