package org.coderinfo.pf.admin.dao;

import org.coderinfo.pf.admin.dto.OmsOrderDeliveryParam;
import org.coderinfo.pf.admin.dto.OmsOrderDetail;
import org.coderinfo.pf.admin.dto.OmsOrderQueryParam;
import org.coderinfo.pf.core.domain.oms.OmsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单自定义查询Dao
 * Created by macro on 2018/10/12.
 */
public interface OmsOrderDao {
    /**
     * 条件查询订单
     */
    List<OmsOrder> getList(@Param("queryParam") OmsOrderQueryParam queryParam);

    /**
     * 批量发货
     */
    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);
}
