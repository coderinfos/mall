package org.coderinfo.pf.mall.portal.service;

import org.coderinfo.pf.core.domain.uac.UacMemberReceiveAddress;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户地址管理Service
 * Created by macro on 2018/8/28.
 */
public interface UacMemberReceiveAddressService {
    /**
     * 添加收货地址
     */
    int add(UacMemberReceiveAddress address);

    /**
     * 删除收货地址
     * @param id 地址表的id
     */
    int delete(Long id);

    /**
     * 修改收货地址
     * @param id 地址表的id
     * @param address 修改的收货地址信息
     */
    @Transactional
    int update(Long id, UacMemberReceiveAddress address);

    /**
     * 返回当前用户的收货地址
     */
    List<UacMemberReceiveAddress> list();

    /**
     * 获取地址详情
     * @param id 地址id
     */
    UacMemberReceiveAddress getItem(Long id);
}
