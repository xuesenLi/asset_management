package io.renren.modules.asset.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.asset.enums.AssetStatusEnum;
import io.renren.modules.asset.enums.ResponseEnum;
import io.renren.modules.asset.form.AssetForm;
import io.renren.modules.asset.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.service.AssetService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.validation.Valid;

/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 14:32:22
 */
@RestController
@RequestMapping("asset/asset")
@Slf4j
public class AssetController {
    @Autowired
    private AssetService assetService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:asset:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:asset:info")
    public R info(@PathVariable("id") Integer id){
        AssetEntity asset = assetService.getById(id);

        return R.ok().put("asset", asset);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:asset:save")
    public ResponseVo save(@Valid @RequestBody AssetForm form,
                           BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.info("注册提交的表单有误, {} {}",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(ResponseEnum.PARAM_ERROR, bindingResult);
        }

        AssetEntity assetEntity = new AssetEntity();
        BeanUtils.copyProperties(form, assetEntity);
        assetService.save(assetEntity);

        return ResponseVo.success();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:asset:update")
    public R update(@RequestBody AssetEntity asset){
        ValidatorUtils.validateEntity(asset);
        assetService.updateById(asset);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:asset:delete")
    public ResponseVo delete(@RequestBody Integer[] ids){
        //只能删除 资产状态为 闲置， 报废的。
        //1. 查询所有资产是否满足条件
        Collection<AssetEntity> assetEntities = assetService.listByIds(Arrays.asList(ids));
        for(AssetEntity asset : assetEntities){
            if(!AssetStatusEnum.IDLE.getCode().equals(asset.getAssetStatus()) &&
                    !AssetStatusEnum.SCRAP.getCode().equals(asset.getAssetStatus())){
                return ResponseVo.error(ResponseEnum.DELETE_ASSET_FAIL);
            }
        }

        assetService.removeByIds(Arrays.asList(ids));
        return ResponseVo.success();
        //assetService.removeByIds(Arrays.asList(ids));
    }

}
