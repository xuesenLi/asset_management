package io.renren.modules.asset.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.asset.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
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
 * @date 2020-04-10 14:29:07
 */
@RestController
@RequestMapping("asset/assetarea")
@Slf4j
public class AssetAreaController extends AbstractController {
    @Autowired
    private AssetAreaService assetAreaService;

    /**
     * 分页列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetarea:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetAreaService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/all")
    //@RequiresPermissions("asset:assetarea:all")
    public ResponseVo<List<AssetAreaEntity>> list(){
        List<AssetAreaEntity> list = assetAreaService.list();

        return ResponseVo.success(list);
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
        assetArea.setUpdateName(getUser().getUsername());
        log.info("保存 : assetArea = {}", assetArea);
        assetAreaService.save(assetArea);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetarea:update")
    public R update(@RequestBody AssetAreaEntity assetArea){
        assetArea.setUpdateName(getUser().getUsername());
        assetArea.setUpdateTime(new Date());
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
