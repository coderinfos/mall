package org.coderinfo.pf.mall.portal.service;

import org.coderinfo.pf.common.api.CommonPage;
import org.coderinfo.pf.core.domain.pms.PmsBrand;
import org.coderinfo.pf.core.domain.pms.PmsProduct;

import java.util.List;

/**
 * 前台品牌管理Service
 * Created by macro on 2020/5/15.
 */
public interface PortalBrandService {
    /**
     * 分页获取推荐品牌
     */
    List<PmsBrand> recommendList(Integer pageNum, Integer pageSize);

    /**
     * 获取品牌详情
     */
    PmsBrand detail(Long brandId);

    /**
     * 分页获取品牌关联商品
     */
    CommonPage<PmsProduct> productList(Long brandId, Integer pageNum, Integer pageSize);
}
