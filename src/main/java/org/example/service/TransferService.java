package org.example.service;

import org.example.model.Transfer;

public interface TransferService {

    /**
     * Performs specified transfer
     * @param transfer object that defines the amount of the transfer, the sender's and the receiver's account
     * @return object that describes performed transfer
     */
    Transfer performTransfer(Transfer transfer);
}