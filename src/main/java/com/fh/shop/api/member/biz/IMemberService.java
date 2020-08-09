package com.fh.shop.api.member.biz;


import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.po.Member;

public interface IMemberService {

    ServerResponse addMember(Member member);

    ServerResponse validateMember(String memberName);

    ServerResponse validateMail(String mail);

    ServerResponse validatePhone(String phone);

    ServerResponse login(String memberName, String password);
}
