package org.coderinfo.pf.admin.dao;

import org.coderinfo.pf.core.domain.pms.PmsProductVertifyRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义商品审核日志管理Dao
 * Created by macro on 2018/4/27.
 */
public interface PmsProductVertifyRecordDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<PmsProductVertifyRecord> list);
}