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

import io.renren.modules.asset.entity.AssetLendEntity;
import io.renren.modules.asset.service.AssetLendService;
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
@RequestMapping("asset/assetlend")
@Slf4j
public class AssetLendController extends AbstractController {
    @Autowired
    private AssetLendService assetLendService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetlend:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetLendService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetlend:info")
    public R info(@PathVariable("id") Integer id){
        AssetLendEntity assetLend = assetLendService.getById(id);

        return R.ok().put("assetLend", assetLend);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetlend:save")
    public R save(@RequestBody AssetLendEntity assetLend){
        //设置申请人， 申请人名
        assetLend.setCreatedUserid(getUserId().intValue());
        assetLend.setCreatedUsername(getUser().getUsername());
        assetLendService.insert(assetLend);
        log.info("assetLend = {}", assetLend);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetlend:update")
    public R update(@RequestBody AssetLendEntity assetLend){
        ValidatorUtils.validateEntity(assetLend);
        assetLendService.updateById(assetLend);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetlend:delete")
    public R delete(@RequestBody Integer[] ids){
        assetLendService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
