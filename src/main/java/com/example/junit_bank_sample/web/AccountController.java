package com.example.junit_bank_sample.web;

import com.example.junit_bank_sample.config.auth.LoginUser;
import com.example.junit_bank_sample.dto.ResponseDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountDepositReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountSaveRespDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountTransferReqDto;
import com.example.junit_bank_sample.dto.account.AccountReqDto.AccountWithdrawReqDto;
import com.example.junit_bank_sample.dto.account.AccountRespDto;
import com.example.junit_bank_sample.dto.account.AccountRespDto.*;
import com.example.junit_bank_sample.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/s/account")
    public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto, BindingResult bindingResult ,
                                         @AuthenticationPrincipal LoginUser loginUser) {

        AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1,"계좌등록성공", accountSaveRespDto) , HttpStatus.CREATED);

    }

    // 인증이 필요하고 , account 테이블에 1번 row 를 주세요

    // cos 로 로그인을 했는데 , cos 의 id 가 2번일때 : 2번 유저가 1번 유저에 데이터를 달라고 할수 있는 상황이다.
    /*
             if(id == loginUser.getUser().getId()) {
                    throw new CustomFobiddenException("권한이 없습니다.");
             }  이렇게도 처리를 할수 있지만 너무 귀찮은 일이다.

             /s/account/{id} 이렇게 처리 하면 권한처리를 해야 한다.
     */

    @GetMapping("/s/account/login-user")
    public ResponseEntity<?> findUserAccount(@AuthenticationPrincipal LoginUser loginUser) {
        AccountListRespDto accountListDto = accountService.계좌목록보기_유저별(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌목록보기_유저별 성공", accountListDto) , HttpStatus.OK);
    }


    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long number, @AuthenticationPrincipal LoginUser loginUser) {
        accountService.계좌삭제(number,loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌삭제완료", null), HttpStatus.OK);
    }


    // 인증이 필요없다. 왜냐면 ATM 기기에서 입금을 한다는 설정이다.
    @PostMapping("/account/deposit")
    public ResponseEntity<?> depositAccount(@RequestBody @Valid AccountDepositReqDto accountDepositReqDto, BindingResult bindingResult) {

        AccountDepositRespDto respDto = accountService.계좌입금(accountDepositReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌입금완료", respDto), HttpStatus.CREATED);

    }


    // 인증이 필요하다
    @PostMapping("/s/account/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody @Valid AccountWithdrawReqDto accountWithdrawReqDto, BindingResult bindingResult,
                                             @AuthenticationPrincipal LoginUser loginUser) {

        AccountWithdrawRespDto accountWithdrawRespDto = accountService.계좌출금(accountWithdrawReqDto , loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌출금완료", accountWithdrawRespDto), HttpStatus.CREATED);

    }


    // 인증이 필요하다
    @PostMapping("/s/account/transfer")
    public ResponseEntity<?> transferAccount(@RequestBody @Valid AccountTransferReqDto accountTransferReqDto, BindingResult bindingResult,
                                             @AuthenticationPrincipal LoginUser loginUser) {

        AccountTransferRespDto accountTransferRespDto = accountService.계좌이체(accountTransferReqDto, loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1,"계좌이체완료", accountTransferRespDto), HttpStatus.CREATED);

    }

}
