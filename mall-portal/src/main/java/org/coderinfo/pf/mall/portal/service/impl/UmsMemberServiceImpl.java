package org.coderinfo.pf.mall.portal.service.impl;

import org.coderinfo.pf.common.exception.Asserts;
import org.coderinfo.pf.core.mapper.UacMemberLevelMapper;
import org.coderinfo.pf.core.mapper.UacMemberMapper;
import org.coderinfo.pf.core.domain.uac.UacMember;
import org.coderinfo.pf.core.domain.uac.UacMemberLevel;
import org.coderinfo.pf.mall.portal.domain.MemberDetails;
import org.coderinfo.pf.mall.portal.service.UacMemberCacheService;
import org.coderinfo.pf.mall.portal.service.UacMemberService;
import org.coderinfo.pf.mall.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 会员管理Service实现类
 * Created by macro on 2018/8/3.
 */
@Service
public class UacMemberServiceImpl implements UacMemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UacMemberServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UacMemberMapper memberMapper;
    @Autowired
    private UacMemberLevelMapper memberLevelMapper;
    @Autowired
    private UacMemberCacheService memberCacheService;
    @Value("${redis.key.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public UacMember getByUsername(String username) {
        UacMember member = memberCacheService.getMember(username);
        if(member!=null) return member;
        UacMemberExample example = new UacMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UacMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            member = memberList.get(0);
            memberCacheService.setMember(member);
            return member;
        }
        return null;
    }

    @Override
    public UacMember getById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public void register(String username, String password, String telephone, String authCode) {
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            Asserts.fail("验证码错误");
        }
        //查询是否已有该用户
        UacMemberExample example = new UacMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        example.or(example.createCriteria().andPhoneEqualTo(telephone));
        List<UacMember> UacMembers = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(UacMembers)) {
            Asserts.fail("该用户已经存在");
        }
        //没有该用户进行添加操作
        UacMember UacMember = new UacMember();
        UacMember.setUsername(username);
        UacMember.setPhone(telephone);
        UacMember.setPassword(passwordEncoder.encode(password));
        UacMember.setCreateTime(new Date());
        UacMember.setStatus(1);
        //获取默认会员等级并设置
        UacMemberLevelExample levelExample = new UacMemberLevelExample();
        levelExample.createCriteria().andDefaultStatusEqualTo(1);
        List<UacMemberLevel> memberLevelList = memberLevelMapper.selectByExample(levelExample);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            UacMember.setMemberLevelId(memberLevelList.get(0).getId());
        }
        memberMapper.insert(UacMember);
        UacMember.setPassword(null);
    }

    @Override
    public String generateAuthCode(String telephone) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<6;i++){
            sb.append(random.nextInt(10));
        }
        memberCacheService.setAuthCode(telephone,sb.toString());
        return sb.toString();
    }

    @Override
    public void updatePassword(String telephone, String password, String authCode) {
        UacMemberExample example = new UacMemberExample();
        example.createCriteria().andPhoneEqualTo(telephone);
        List<UacMember> memberList = memberMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(memberList)){
            Asserts.fail("该账号不存在");
        }
        //验证验证码
        if(!verifyAuthCode(authCode,telephone)){
            Asserts.fail("验证码错误");
        }
        UacMember UacMember = memberList.get(0);
        UacMember.setPassword(passwordEncoder.encode(password));
        memberMapper.updateByPrimaryKeySelective(UacMember);
        memberCacheService.delMember(UacMember.getId());
    }

    @Override
    public UacMember getCurrentMember() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        MemberDetails memberDetails = (MemberDetails) auth.getPrincipal();
        return memberDetails.getUacMember();
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        UacMember record=new UacMember();
        record.setId(id);
        record.setIntegration(integration);
        memberMapper.updateByPrimaryKeySelective(record);
        memberCacheService.delMember(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UacMember member = getByUsername(username);
        if(member!=null){
            return new MemberDetails(member);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密后传递
        try {
            UserDetails userDetails = loadUserByUsername(username);
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public String refreshToken(String token) {
        return jwtTokenUtil.refreshHeadToken(token);
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String telephone){
        if(StringUtils.isEmpty(authCode)){
            return false;
        }
        String realAuthCode = memberCacheService.getAuthCode(telephone);
        return authCode.equals(realAuthCode);
    }

}
