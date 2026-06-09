package org.example.service;

import org.example.model.FavoriteTransfer;
import org.example.model.Transfer;

import java.time.LocalDate;
import java.util.List;

public interface TransferService {

    /**
     * Performs specified transfer between accounts
     * @param transfer object that defines the amount of the transfer, the sender's and the receiver's account
     * @return object that describes performed transfer
     */
    Transfer performTransfer(Transfer transfer);

    List<Transfer> getStatement(String iban, LocalDate from, LocalDate to);

    FavoriteTransfer createFavorite(FavoriteTransfer favoriteTransfer);

    void deleteFavoriteById(Long id);

    List<FavoriteTransfer> getAllFavorite();
}