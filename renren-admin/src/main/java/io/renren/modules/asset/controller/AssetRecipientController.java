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

import io.renren.modules.asset.entity.AssetRecipientEntity;
import io.renren.modules.asset.service.AssetRecipientService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-24 10:07:32
 */
@RestController
@RequestMapping("asset/assetrecipient")
public class AssetRecipientController {
    @Autowired
    private AssetRecipientService assetRecipientService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetrecipient:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetRecipientService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetrecipient:info")
    public R info(@PathVariable("id") Integer id){
        AssetRecipientEntity assetRecipient = assetRecipientService.getById(id);

        return R.ok().put("assetRecipient", assetRecipient);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetrecipient:save")
    public R save(@RequestBody AssetRecipientEntity assetRecipient){
        assetRecipientService.save(assetRecipient);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetrecipient:update")
    public R update(@RequestBody AssetRecipientEntity assetRecipient){
        ValidatorUtils.validateEntity(assetRecipient);
        assetRecipientService.updateById(assetRecipient);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetrecipient:delete")
    public R delete(@RequestBody Integer[] ids){
        assetRecipientService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
