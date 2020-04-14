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

import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.service.AssetService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 14:32:22
 */
@RestController
@RequestMapping("asset/asset")
public class AssetController {
    @Autowired
    private AssetService assetService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:asset:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:asset:info")
    public R info(@PathVariable("id") Integer id){
        AssetEntity asset = assetService.getById(id);

        return R.ok().put("asset", asset);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:asset:save")
    public R save(@RequestBody AssetEntity asset){
        assetService.save(asset);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:asset:update")
    public R update(@RequestBody AssetEntity asset){
        ValidatorUtils.validateEntity(asset);
        assetService.updateById(asset);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:asset:delete")
    public R delete(@RequestBody Integer[] ids){
        assetService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
