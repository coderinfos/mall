package org.coderinfo.pf.admin.dto;

import org.coderinfo.pf.core.domain.uac.UacMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单节点封装
 * Created by macro on 2020/2/4.
 */
@Getter
@Setter
public class UacMenuNode extends UacMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<UacMenuNode> children;
}
