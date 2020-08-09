package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.area.mapper.IAreaMapper;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.mapper.IMemberMapper;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;

import com.fh.shop.api.mq.MQSender;
import com.fh.shop.api.mq.MailMessage;
import com.fh.shop.api.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


@Service("memberService")
public class IMemberServiceImpl implements IMemberService {

    @Autowired
    private IMemberMapper memberMapper;

   @Autowired
   private MailUtil mailUtil;

    @Autowired
    private IAreaMapper areaMapper;

    @Autowired
    private MQSender mqSender;

    @Override
    public ServerResponse addMember(Member member) {
        String memberName =member.getMemberName();
        String password =member.getPassword();
        String mail =member.getMail();
        String phone =member.getPhone();

        if(StringUtils.isEmpty(memberName)||StringUtils.isEmpty(password)
                                          ||StringUtils.isEmpty(mail)
                                          ||StringUtils.isEmpty(phone)
        ){
           return ServerResponse.error(ResponseEnum.MEMER_IS_NULL);
        }
        QueryWrapper<Member> queryWrapper=new QueryWrapper <>();
        queryWrapper.eq("memberName",memberName);
        Member memberDB = memberMapper.selectOne(queryWrapper);
        if(memberDB !=null){
            return ServerResponse.error(ResponseEnum.MEMER_MEMBER_ECZ);
        }
        QueryWrapper<Member> mailWrapper=new QueryWrapper <>();
        mailWrapper.eq("mail",mail);
        Member memberMail = memberMapper.selectOne(mailWrapper);
        if(memberMail !=null){
            return ServerResponse.error(ResponseEnum.MEMER_MAIL_ECZ);
        }
        QueryWrapper<Member> phoneWrapper=new QueryWrapper <>();
        phoneWrapper.eq("phone",phone);
        Member memberPhone = memberMapper.selectOne(phoneWrapper);
        if(memberPhone !=null){
            return ServerResponse.error(ResponseEnum.MEMER_PHONE_ECZ);
        }

        mailUtil.sendMail(mail,"飞狐","恭喜"+member.getRealName()+"注册成功");
          memberMapper.addMember(member);
          return ServerResponse.success();
    }

    @Override
    public ServerResponse validateMember(String memberName) {
        if(StringUtils.isEmpty(memberName)){
            return ServerResponse.error(ResponseEnum.MEMER_IS_NULL);
        }
        QueryWrapper<Member> queryWrapper=new QueryWrapper <>();
        queryWrapper.eq("memberName",memberName);
        Member memberDB = memberMapper.selectOne(queryWrapper);
        if(memberDB !=null){
            return ServerResponse.error(ResponseEnum.MEMER_MEMBER_ECZ);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse validateMail(String mail) {
        if(StringUtils.isEmpty(mail)){

            return ServerResponse.error(ResponseEnum.MEMER_MAIL_ECZ);
        }
        QueryWrapper<Member> mailWrapper=new QueryWrapper <>();
        mailWrapper.eq("mail",mail);
        Member memberMail = memberMapper.selectOne(mailWrapper);
        if(memberMail !=null){
            return ServerResponse.error(ResponseEnum.MEMER_MAIL_ECZ);
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse validatePhone(String phone) {
        if(StringUtils.isEmpty(phone)){

            return ServerResponse.error(ResponseEnum.MEMER_PHONE_ECZ);
        }
        QueryWrapper<Member> phoneWrapper=new QueryWrapper <>();
        phoneWrapper.eq("phone",phone);
        Member memberPhone = memberMapper.selectOne(phoneWrapper);
        if(memberPhone !=null){
            return ServerResponse.error(ResponseEnum.MEMER_PHONE_ECZ);
        }

        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(String memberName, String password) {

        if(StringUtils.isEmpty(memberName)||StringUtils.isEmpty(password)){
              return ServerResponse.error(ResponseEnum.LOGIN_MEMBER_POSSWROD_NULL);
        }
        QueryWrapper <Member> memberQueryWrapper = new QueryWrapper <>();
        memberQueryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(memberQueryWrapper);
        if(member==null){
           return ServerResponse.error(ResponseEnum.LOGIN_MEMBERNAME_NEXT);
        }
        if(!password.equals(member.getPassword())){
            return ServerResponse.error(ResponseEnum.LOGIN_POSSWROD_NEXT);
        }
        MemberVo memberVo = new MemberVo();
        Long memberId= member.getId();
        memberVo.setId(memberId);
        memberVo.setMemberName(member.getMemberName());
        memberVo.setPassword(member.getPassword());
        memberVo.setRealName(member.getRealName());
        String uuid = UUID.randomUUID().toString();
        memberVo.setUuid(uuid);
        String memberJson = JSONObject.toJSONString(memberVo);
        try {
            String memberJsonBase64 = Base64.getEncoder().encodeToString(memberJson.getBytes("utf-8"));

            String sign = MD5Util.sign(memberJsonBase64, MD5Util.SECRET);
            String signBase64 = Base64.getEncoder().encodeToString(sign.getBytes("utf-8"));

            RedisUitl.setEx(KeyUtil.buildMemberKey(uuid,memberId),"",KeyUtil.MEMBER_KEY_EXPIRE);
            //同步发送qq信箱
           // String mail = member.getMail();
           // mailUtil.sendMail(mail,"登陆成功",member.getRealName()+"在"+ DateUtil.date2str(new Date(),DateUtil.FULL_TIME));
          //1部发送qq信箱
            String mail = member.getMail();
            MailMessage mailMessage = new MailMessage();
            mailMessage.setMail(mail);
            mailMessage.setRealName(member.getRealName());
            mailMessage.setTitle("登陆成功");
            mailMessage.setContent(member.getRealName()+"在"+ DateUtil.date2str(new Date(),DateUtil.FULL_TIME)+"登陆了");
            mqSender.MailMessage(mailMessage);
            return ServerResponse.success(memberJsonBase64+"."+signBase64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          //  throw new RuntimeException(e);
            return ServerResponse.error();
        }

    }
}
