package org.coderinfo.pf.admin.dao;

import org.coderinfo.pf.core.domain.cms.CmsSubjectProductRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义商品和专题关系操作Dao
 * Created by macro on 2018/4/26.
 */
public interface CmsSubjectProductRelationDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list") List<CmsSubjectProductRelation> subjectProductRelationList);
}