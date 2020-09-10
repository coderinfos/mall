package org.coderinfo.pf.mall.portal.controller;

import org.coderinfo.pf.common.api.CommonResult;
import org.coderinfo.pf.core.domain.uac.UacMemberReceiveAddress;
import org.coderinfo.pf.mall.portal.service.UacMemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会员收货地址管理Controller
 * Created by macro on 2018/8/28.
 */
@Controller
@Api(tags = "UacMemberReceiveAddressController", description = "会员收货地址管理")
@RequestMapping("/member/address")
public class UacMemberReceiveAddressController {
    @Autowired
    private UacMemberReceiveAddressService memberReceiveAddressService;

    @ApiOperation("添加收货地址")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@RequestBody UacMemberReceiveAddress address) {
        int count = memberReceiveAddressService.add(address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id) {
        int count = memberReceiveAddressService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改收货地址")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UacMemberReceiveAddress address) {
        int count = memberReceiveAddressService.update(id, address);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UacMemberReceiveAddress>> list() {
        List<UacMemberReceiveAddress> addressList = memberReceiveAddressService.list();
        return CommonResult.success(addressList);
    }

    @ApiOperation("获取收货地址详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UacMemberReceiveAddress> getItem(@PathVariable Long id) {
        UacMemberReceiveAddress address = memberReceiveAddressService.getItem(id);
        return CommonResult.success(address);
    }
}
