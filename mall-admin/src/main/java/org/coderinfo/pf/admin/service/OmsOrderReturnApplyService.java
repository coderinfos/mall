package org.coderinfo.pf.admin.service;

import org.coderinfo.pf.admin.dto.OmsOrderReturnApplyResult;
import org.coderinfo.pf.admin.dto.OmsReturnApplyQueryParam;
import org.coderinfo.pf.admin.dto.OmsUpdateStatusParam;
import org.coderinfo.pf.core.domain.oms.OmsOrderReturnApply;

import java.util.List;

/**
 * 退货申请管理Service
 * Created by macro on 2018/10/18.
 */
public interface OmsOrderReturnApplyService {
    /**
     * 分页查询申请
     */
    List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量删除申请
     */
    int delete(List<Long> ids);

    /**
     * 修改申请状态
     */
    int updateStatus(Long id, OmsUpdateStatusParam statusParam);

    /**
     * 获取指定申请详情
     */
    OmsOrderReturnApplyResult getItem(Long id);
}
