package io.renren.modules.asset.controller;

import java.util.Arrays;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.asset.vo.AssetChangeVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.asset.entity.AssetChangeEntity;
import io.renren.modules.asset.service.AssetChangeService;
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
@RequestMapping("asset/assetchange")
public class AssetChangeController extends AbstractController {
    @Autowired
    private AssetChangeService assetChangeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetchange:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetChangeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetchange:info")
    public R info(@PathVariable("id") Integer id){
        AssetChangeEntity assetChange = assetChangeService.getById(id);

        return R.ok().put("assetChange", assetChange);
    }

    /**
     * 信息
     */
    @RequestMapping("/infoByRecordNo/{recordNo}")
    public R infoByRecordNo(@PathVariable("recordNo") String recordNo){

        AssetChangeVo assetChange = assetChangeService.getByRecordNo(recordNo);

        return R.ok().put("assetChange", assetChange);
    }

    /**
     * 资产变更 驳回
     */
    @RequestMapping("/rebut")
    public R rebut(String recordNo){
        //
        return assetChangeService.assetRebut(recordNo);
    }
    /**
     * 资产变更 同意
     */
    @RequestMapping("/agree")
    public R agree(String recordNo){
        //
        return assetChangeService.assetAgree(recordNo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetchange:save")
    public R save(@RequestBody AssetChangeEntity assetChange){
        //设置申请人， 申请人名
        assetChange.setCreatedUsername(getUser().getUsername());
        assetChange.setCreatedUserid(getUserId().intValue());
        assetChangeService.insert(assetChange);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetchange:update")
    public R update(@RequestBody AssetChangeEntity assetChange){
        ValidatorUtils.validateEntity(assetChange);
        assetChangeService.updateById(assetChange);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetchange:delete")
    public R delete(@RequestBody Integer[] ids){
        assetChangeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
