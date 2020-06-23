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

import io.renren.modules.asset.entity.AssetScrapEntity;
import io.renren.modules.asset.service.AssetScrapService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-20 10:57:50
 */
@RestController
@RequestMapping("asset/assetscrap")
public class AssetScrapController extends AbstractController {
    @Autowired
    private AssetScrapService assetScrapService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetscrap:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetScrapService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetscrap:info")
    public R info(@PathVariable("id") Integer id){
        AssetScrapEntity assetScrap = assetScrapService.getById(id);

        return R.ok().put("assetScrap", assetScrap);
    }

    /**
     * 信息
     */
    @RequestMapping("/infoByRecordNo/{recordNo}")
    public R infoByRecordNo(@PathVariable("recordNo") String recordNo){

        AssetScrapEntity assetScrap = assetScrapService.getByRecordNo(recordNo);

        return R.ok().put("assetScrap", assetScrap);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetscrap:save")
    public R save(@RequestBody AssetScrapEntity assetScrap){
        //设置申请人， 申请人名
        assetScrap.setCreatedUsername(getUser().getUsername());
        assetScrap.setCreatedUserid(getUserId().intValue());
        assetScrapService.insert(assetScrap);

        return R.ok();
    }

    /**
     * 资产报废 驳回
     */
    @RequestMapping("/rebut")
    public R rebut(String recordNo){
        //
        return assetScrapService.assetRebut(recordNo);
    }
    /**
     * 资产报废 同意
     */
    @RequestMapping("/agree")
    public R agree(String recordNo){
        //
        return assetScrapService.assetAgree(recordNo, getUser());
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetscrap:update")
    public R update(@RequestBody AssetScrapEntity assetScrap){
        ValidatorUtils.validateEntity(assetScrap);
        assetScrapService.updateById(assetScrap);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetscrap:delete")
    public R delete(@RequestBody Integer[] ids){
        assetScrapService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
