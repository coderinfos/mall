package org.coderinfo.pf.admin.dto;

import org.coderinfo.pf.core.domain.uac.UacPermission;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台权限节点封装
 * Created by macro on 2018/9/30.
 */
public class UacPermissionNode extends UacPermission {
    @Getter
    @Setter
    @ApiModelProperty(value = "子级权限")
    private List<UacPermissionNode> children;
}
