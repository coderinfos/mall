package org.coderinfo.pf.mall.demo.service;

import org.coderinfo.pf.mall.demo.dto.PmsBrandDto;
import org.coderinfo.pf.core.domain.pms.PmsBrand;

import java.util.List;

/**
 * DemoService接口
 */
public interface DemoService {
    List<PmsBrand> listAllBrand();

    int createBrand(PmsBrandDto pmsBrandDto);

    int updateBrand(Long id, PmsBrandDto pmsBrandDto);

    int deleteBrand(Long id);

    List<PmsBrand> listBrand(int pageNum, int pageSize);

    PmsBrand getBrand(Long id);
}
