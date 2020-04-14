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

import io.renren.modules.asset.entity.AssetAreaEntity;
import io.renren.modules.asset.service.AssetAreaService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-10 13:59:02
 */
@RestController
@RequestMapping("asset/assetarea")
public class AssetAreaController {
    @Autowired
    private AssetAreaService assetAreaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetarea:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetAreaService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{areaId}")
    @RequiresPermissions("asset:assetarea:info")
    public R info(@PathVariable("areaId") Integer areaId){
        AssetAreaEntity assetArea = assetAreaService.getById(areaId);

        return R.ok().put("assetArea", assetArea);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetarea:save")
    public R save(@RequestBody AssetAreaEntity assetArea){
        assetAreaService.save(assetArea);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetarea:update")
    public R update(@RequestBody AssetAreaEntity assetArea){
        ValidatorUtils.validateEntity(assetArea);
        assetAreaService.updateById(assetArea);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetarea:delete")
    public R delete(@RequestBody Integer[] areaIds){
        assetAreaService.removeByIds(Arrays.asList(areaIds));

        return R.ok();
    }

}
