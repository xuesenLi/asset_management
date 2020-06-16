package io.renren.modules.asset.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.asset.enums.ResponseEnum;
import io.renren.modules.asset.form.WxFinishForm;
import io.renren.modules.asset.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.asset.entity.AssetOperRecordEntity;
import io.renren.modules.asset.service.AssetOperRecordService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.validation.Valid;


/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-21 13:41:23
 */
@RestController
@RequestMapping("asset/assetoperrecord")
@Slf4j
public class AssetOperRecordController extends AbstractController {
    @Autowired
    private AssetOperRecordService assetOperRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetoperrecord:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = assetOperRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:assetoperrecord:info")
    public R info(@PathVariable("id") Integer id){
        AssetOperRecordEntity assetOperRecord = assetOperRecordService.getById(id);

        return R.ok().put("assetOperRecord", assetOperRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetoperrecord:save")
    public R save(@RequestBody AssetOperRecordEntity assetOperRecord){
        assetOperRecordService.save(assetOperRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetoperrecord:update")
    public R update(@RequestBody AssetOperRecordEntity assetOperRecord){
        ValidatorUtils.validateEntity(assetOperRecord);
        assetOperRecordService.updateById(assetOperRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetoperrecord:delete")
    public R delete(@RequestBody Integer[] ids){
        assetOperRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/insert/wxfinish")
    public ResponseVo WXFinnish(@Valid @RequestBody WxFinishForm form,
                                BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.info("注册提交的表单有误, {} {}",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(ResponseEnum.PARAM_ERROR, bindingResult);
        }

        return assetOperRecordService.insertWXFinish(form, getUser());

    }

}
