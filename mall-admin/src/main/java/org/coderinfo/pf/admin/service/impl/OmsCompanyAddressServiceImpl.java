package org.coderinfo.pf.admin.service.impl;

import org.coderinfo.pf.core.mapper.OmsCompanyAddressMapper;
import org.coderinfo.pf.core.domain.oms.OmsCompanyAddress;
import org.coderinfo.pf.admin.service.OmsCompanyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址管理Service实现类
 * Created by macro on 2018/10/18.
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
