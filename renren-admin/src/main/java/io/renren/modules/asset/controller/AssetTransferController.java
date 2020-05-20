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

import io.renren.modules.asset.entity.AssetTransferEntity;
import io.renren.modules.asset.service.AssetTransferService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-18 11:09:07
 */
@RestController
@RequestMapping("asset/assettransfer")
public class AssetTransferController extends AbstractController {
    @Autowired
    private AssetTransferService assetTransferService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assettransfer:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetTransferService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assettransfer:info")
    public R info(@PathVariable("id") Integer id){
        AssetTransferEntity assetTransfer = assetTransferService.getById(id);

        return R.ok().put("assetTransfer", assetTransfer);
    }

    /**
     * 信息
     */
    @RequestMapping("/infoByRecordNo/{recordNo}")
    public R infoByRecordNo(@PathVariable("recordNo") String recordNo){

        AssetTransferEntity assetTransfer = assetTransferService.getByRecordNo(recordNo);

        return R.ok().put("assetTransfer", assetTransfer);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assettransfer:save")
    public R save(@RequestBody AssetTransferEntity assetTransfer){

        //设置申请人， 申请人名
        assetTransfer.setCreatedUsername(getUser().getUsername());
        assetTransfer.setCreatedUserid(getUserId().intValue());
        assetTransferService.insert(assetTransfer);

        return R.ok();
    }

    /**
     * 资产变更 驳回
     */
    @RequestMapping("/rebut")
    public R rebut(String recordNo){
        //
        return assetTransferService.assetRebut(recordNo);
    }
    /**
     * 资产变更 同意
     */
    @RequestMapping("/agree")
    public R agree(String recordNo){
        //
        return assetTransferService.assetAgree(recordNo);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assettransfer:update")
    public R update(@RequestBody AssetTransferEntity assetTransfer){
        ValidatorUtils.validateEntity(assetTransfer);
        assetTransferService.updateById(assetTransfer);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assettransfer:delete")
    public R delete(@RequestBody Integer[] ids){
        assetTransferService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
