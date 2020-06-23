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
public class AssetController extends AbstractController {
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
     * 获取资产状态为 闲置的
     */
    @RequestMapping("/listByTypeXZ")
    public R listByTypeXZ(@RequestParam Map<String, Object> params){
        PageUtils page = assetService.queryPageByTypeXZ(params);

        return R.ok().put("page", page);
    }
    /**
     * 获取资产状态为 在用的
     */
    @RequestMapping("/listByTypeZY")
    public R listByTypeZY(@RequestParam Map<String, Object> params){
        PageUtils page = assetService.queryPageByTypeZY(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取资产状态为 借用的
     */
    @RequestMapping("/listByTypeJY")
    public R listByTypeJY(@RequestParam Map<String, Object> params){
        PageUtils page = assetService.queryPageByTypeJY(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取资产状态为 闲置、在用、借用
     */
    @RequestMapping("/listByTypeXZJ")
    public R listByTypeXZJ(@RequestParam Map<String, Object> params){
        PageUtils page = assetService.queryPageByTypeXZJ(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取指定  单号  的列表
     */
    @RequestMapping("/listByRecordNo/{recordNo}")
    public R listByRecordNo(@RequestParam Map<String, Object> params,
                            @PathVariable("recordNo") String recordNo){
        PageUtils page = assetService.queryPageByIn(params, recordNo);

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

        return assetService.batchInsertAsset(form, getUser());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:asset:update")
    public ResponseVo update(@RequestBody AssetEntity asset){
        //assetService.updateById(asset);
        //修改其他信息。
        return assetService.updateByOtherInfo(asset);
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
