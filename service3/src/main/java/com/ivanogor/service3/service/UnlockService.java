package com.ivanogor.service3.service;

import com.ivanogor.service3.dto.AccountDto;
import com.ivanogor.service3.dto.ClientDto;
import com.ivanogor.service3.dto.UnblockDto;

public interface UnlockService {
    UnblockDto unlockClient(ClientDto clientDto);

    UnblockDto unlockAccount(AccountDto accountDto);
}
