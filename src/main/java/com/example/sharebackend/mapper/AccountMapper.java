package com.example.sharebackend.mapper;

import com.example.sharebackend.domain.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {
    //회원 가입
    int insertOne (Account account);

    // 해당 아이디 정보 조회
    Account selectAll(String id);

    Account findById(String id);

    //코드 인증 성공
    int activeUpdate (String  account);

}
