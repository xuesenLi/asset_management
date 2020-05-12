package io.renren.modules.asset.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.asset.entity.AssetReturnedEntity;
import io.renren.modules.asset.service.AssetReturnedService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-05-12 13:28:50
 */
@RestController
@RequestMapping("asset/assetreturned")
@Slf4j
public class AssetReturnedController extends AbstractController {
    @Autowired
    private AssetReturnedService assetReturnedService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetreturned:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetReturnedService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetreturned:info")
    public R info(@PathVariable("id") Integer id){
        AssetReturnedEntity assetReturned = assetReturnedService.getById(id);

        return R.ok().put("assetReturned", assetReturned);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetreturned:save")
    public R save(@RequestBody AssetReturnedEntity assetReturned){
        //设置申请人， 申请人名
        assetReturned.setCreatedUsername(getUser().getUsername());
        assetReturned.setCreatedUserid(getUserId().intValue());
        assetReturnedService.insert(assetReturned);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetreturned:update")
    public R update(@RequestBody AssetReturnedEntity assetReturned){
        ValidatorUtils.validateEntity(assetReturned);
        assetReturnedService.updateById(assetReturned);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetreturned:delete")
    public R delete(@RequestBody Integer[] ids){
        assetReturnedService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
