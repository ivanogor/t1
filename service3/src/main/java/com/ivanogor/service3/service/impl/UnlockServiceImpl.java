package com.ivanogor.service3.service.impl;

import com.ivanogor.service3.dto.AccountDto;
import com.ivanogor.service3.dto.ClientDto;
import com.ivanogor.service3.dto.UnblockDto;
import com.ivanogor.service3.service.UnlockService;
import org.springframework.stereotype.Service;

@Service
public class UnlockServiceImpl implements UnlockService {
    @Override
    public UnblockDto unlockClient(ClientDto clientDto) {
        return UnblockDto.builder()
                .id(clientDto.getClientId())
                .isUnblocked(Math.random() < 0.5)
                .build();
    }

    @Override
    public UnblockDto unlockAccount(AccountDto accountDto) {
        return UnblockDto.builder()
                .id(accountDto.getAccountId().toString())
                .isUnblocked(Math.random() < 0.5)
                .build();
    }
}
