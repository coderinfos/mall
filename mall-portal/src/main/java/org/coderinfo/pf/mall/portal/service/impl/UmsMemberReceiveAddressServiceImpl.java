package org.coderinfo.pf.mall.portal.service.impl;

import org.coderinfo.pf.core.mapper.UacMemberReceiveAddressMapper;
import org.coderinfo.pf.core.domain.uac.UacMember;
import org.coderinfo.pf.core.domain.uac.UacMemberReceiveAddress;
import org.coderinfo.pf.mall.portal.service.UacMemberReceiveAddressService;
import org.coderinfo.pf.mall.portal.service.UacMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 用户地址管理Service实现类
 * Created by macro on 2018/8/28.
 */
@Service
public class UacMemberReceiveAddressServiceImpl implements UacMemberReceiveAddressService {
    @Autowired
    private UacMemberService memberService;
    @Autowired
    private UacMemberReceiveAddressMapper addressMapper;
    @Override
    public int add(UacMemberReceiveAddress address) {
        UacMember currentMember = memberService.getCurrentMember();
        address.setMemberId(currentMember.getId());
        return addressMapper.insert(address);
    }

    @Override
    public int delete(Long id) {
        UacMember currentMember = memberService.getCurrentMember();
        UacMemberReceiveAddressExample example = new UacMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        return addressMapper.deleteByExample(example);
    }

    @Override
    public int update(Long id, UacMemberReceiveAddress address) {
        address.setId(null);
        UacMember currentMember = memberService.getCurrentMember();
        UacMemberReceiveAddressExample example = new UacMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        if(address.getDefaultStatus()==1){
            //先将原来的默认地址去除
            UacMemberReceiveAddress record= new UacMemberReceiveAddress();
            record.setDefaultStatus(0);
            UacMemberReceiveAddressExample updateExample = new UacMemberReceiveAddressExample();
            updateExample.createCriteria()
                    .andMemberIdEqualTo(currentMember.getId())
                    .andDefaultStatusEqualTo(1);
            addressMapper.updateByExampleSelective(record,updateExample);
        }
        return addressMapper.updateByExampleSelective(address,example);
    }

    @Override
    public List<UacMemberReceiveAddress> list() {
        UacMember currentMember = memberService.getCurrentMember();
        UacMemberReceiveAddressExample example = new UacMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId());
        return addressMapper.selectByExample(example);
    }

    @Override
    public UacMemberReceiveAddress getItem(Long id) {
        UacMember currentMember = memberService.getCurrentMember();
        UacMemberReceiveAddressExample example = new UacMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(currentMember.getId()).andIdEqualTo(id);
        List<UacMemberReceiveAddress> addressList = addressMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(addressList)){
            return addressList.get(0);
        }
        return null;
    }
}
