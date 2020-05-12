package io.renren.modules.asset.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.asset.entity.AssetRefundEntity;
import io.renren.modules.asset.service.AssetRefundService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-24 10:07:31
 */
@RestController
@RequestMapping("asset/assetrefund")
public class AssetRefundController extends AbstractController {
    @Autowired
    private AssetRefundService assetRefundService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetrefund:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetRefundService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetrefund:info")
    public R info(@PathVariable("id") Integer id){
        AssetRefundEntity assetRefund = assetRefundService.getById(id);

        return R.ok().put("assetRefund", assetRefund);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetrefund:save")
    public R save(@RequestBody AssetRefundEntity assetRefund){
        //设置申请人， 申请人名
        assetRefund.setCreatedUsername(getUser().getUsername());
        assetRefund.setCreatedUserid(getUserId().intValue());
        assetRefundService.insert(assetRefund);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetrefund:update")
    public R update(@RequestBody AssetRefundEntity assetRefund){
        ValidatorUtils.validateEntity(assetRefund);
        assetRefundService.updateById(assetRefund);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetrefund:delete")
    public R delete(@RequestBody Integer[] ids){
        assetRefundService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
