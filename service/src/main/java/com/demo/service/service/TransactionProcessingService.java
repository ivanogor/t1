package com.demo.service.service;

import com.demo.service.dto.TransactionAcceptedMessageDto;
import com.demo.service.dto.TransactionResultMessageDto;

public interface TransactionProcessingService {
    TransactionResultMessageDto processTransaction(TransactionAcceptedMessageDto message);
}
