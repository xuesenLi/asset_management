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

import io.renren.modules.asset.entity.AssetCategoryEntity;
import io.renren.modules.asset.service.AssetCategoryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 16:41:56
 */
@RestController
@RequestMapping("asset/assetcategory")
public class AssetCategoryController {
    @Autowired
    private AssetCategoryService assetCategoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetcategory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetCategoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetcategory:info")
    public R info(@PathVariable("id") Integer id){
        AssetCategoryEntity assetCategory = assetCategoryService.getById(id);

        return R.ok().put("assetCategory", assetCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetcategory:save")
    public R save(@RequestBody AssetCategoryEntity assetCategory){
        assetCategoryService.save(assetCategory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetcategory:update")
    public R update(@RequestBody AssetCategoryEntity assetCategory){
        ValidatorUtils.validateEntity(assetCategory);
        assetCategoryService.updateById(assetCategory);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetcategory:delete")
    public R delete(@RequestBody Integer[] ids){
        assetCategoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
