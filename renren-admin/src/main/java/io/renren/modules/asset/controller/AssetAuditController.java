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

import io.renren.modules.asset.entity.AssetAuditEntity;
import io.renren.modules.asset.service.AssetAuditService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 17:31:52
 */
@RestController
@RequestMapping("asset/assetaudit")
public class AssetAuditController {
    @Autowired
    private AssetAuditService assetAuditService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetaudit:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetAuditService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetaudit:info")
    public R info(@PathVariable("id") Integer id){
        AssetAuditEntity assetAudit = assetAuditService.getById(id);

        return R.ok().put("assetAudit", assetAudit);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetaudit:save")
    public R save(@RequestBody AssetAuditEntity assetAudit){
        assetAuditService.save(assetAudit);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetaudit:update")
    public R update(@RequestBody AssetAuditEntity assetAudit){
        ValidatorUtils.validateEntity(assetAudit);
        assetAuditService.updateById(assetAudit);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetaudit:delete")
    public R delete(@RequestBody Integer[] ids){
        assetAuditService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
