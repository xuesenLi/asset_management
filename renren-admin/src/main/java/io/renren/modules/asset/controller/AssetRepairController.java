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

import io.renren.modules.asset.entity.AssetRepairEntity;
import io.renren.modules.asset.service.AssetRepairService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-21 13:41:23
 */
@RestController
@RequestMapping("asset/assetrepair")
public class AssetRepairController extends AbstractController {
    @Autowired
    private AssetRepairService assetRepairService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetrepair:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetRepairService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetrepair:info")
    public R info(@PathVariable("id") Integer id){
        AssetRepairEntity assetRepair = assetRepairService.getById(id);

        return R.ok().put("assetRepair", assetRepair);
    }

    /**
     * 信息
     */
    @RequestMapping("/infoByRecordNo/{recordNo}")
    public R infoByRecordNo(@PathVariable("recordNo") String recordNo){

        AssetRepairEntity assetRepair = assetRepairService.getByRecordNo(recordNo);

        return R.ok().put("assetRepair", assetRepair);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetrepair:save")
    public R save(@RequestBody AssetRepairEntity assetRepair){
        //设置申请人， 申请人名
        assetRepair.setCreatedUsername(getUser().getUsername());
        assetRepair.setCreatedUserid(getUserId().intValue());
        assetRepairService.insert(assetRepair);

        return R.ok();
    }

    /**
     * 资产维修 驳回
     */
    @RequestMapping("/rebut")
    public R rebut(String recordNo){
        //
        return assetRepairService.assetRebut(recordNo);
    }
    /**
     * 资产维修 同意
     */
    @RequestMapping("/agree")
    public R agree(String recordNo){
        //
        return assetRepairService.assetAgree(recordNo);
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetrepair:update")
    public R update(@RequestBody AssetRepairEntity assetRepair){
        ValidatorUtils.validateEntity(assetRepair);
        assetRepairService.updateById(assetRepair);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetrepair:delete")
    public R delete(@RequestBody Integer[] ids){
        assetRepairService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
