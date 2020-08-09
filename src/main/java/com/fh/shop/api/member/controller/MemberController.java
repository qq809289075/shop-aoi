package com.fh.shop.api.member.controller;


import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.api.member.po.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/members")
@Api(tags = "会员接口")
public class MemberController {
    @Resource
    private IMemberService  memberService;

    @PostMapping
    private ServerResponse addMember(Member member){
        return memberService.addMember(member);
    }
    @GetMapping ("/validateMember")
    private ServerResponse validateMember(String memberName){
        return memberService.validateMember(memberName);
    }
    @GetMapping ("/validateMail")
    private ServerResponse validateMail(String mail){
        return memberService.validateMail(mail);
    }
    @GetMapping ("/validatePhone")
    private ServerResponse validatePhone(String phone){
        return memberService.validatePhone(phone);
    }

    @PostMapping("/login")
    @ApiOperation("会员登录接口")
    @ApiImplicitParams({

           @ApiImplicitParam(name="memberName", value = "会员名",type="string",required = true, paramType = "query"),
            @ApiImplicitParam(name="password", value = "密码",type="string",required = true, paramType = "query")
    })
    public ServerResponse login(String memberName, String password){

        return memberService.login(memberName,password);
    }



}
