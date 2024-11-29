package com.ivanogor.service3.controller;

import com.ivanogor.service3.dto.AccountDto;
import com.ivanogor.service3.dto.ClientDto;
import com.ivanogor.service3.dto.UnblockDto;
import com.ivanogor.service3.service.UnlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unlock")
@RequiredArgsConstructor
public class UnlockController {
    private final UnlockService unlockService;

    @PostMapping("/client")
    public ResponseEntity<UnblockDto> unlockClient(@RequestBody ClientDto clientDto){
        UnblockDto response = unlockService.unlockClient(clientDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/account")
    public ResponseEntity<UnblockDto> unlockAccount(@RequestBody AccountDto accountDto){
        UnblockDto response = unlockService.unlockAccount(accountDto);
        return ResponseEntity.ok(response);
    }
}
