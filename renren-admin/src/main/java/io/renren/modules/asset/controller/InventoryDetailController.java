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

import io.renren.modules.asset.entity.InventoryDetailEntity;
import io.renren.modules.asset.service.InventoryDetailService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-06-23 09:37:46
 */
@RestController
@RequestMapping("asset/inventorydetail")
public class InventoryDetailController {
    @Autowired
    private InventoryDetailService inventoryDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:inventorydetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inventoryDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:inventorydetail:info")
    public R info(@PathVariable("id") Integer id){
        InventoryDetailEntity inventoryDetail = inventoryDetailService.getById(id);

        return R.ok().put("inventoryDetail", inventoryDetail);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:inventorydetail:save")
    public R save(@RequestBody InventoryDetailEntity inventoryDetail){
        inventoryDetailService.save(inventoryDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:inventorydetail:update")
    public R update(@RequestBody InventoryDetailEntity inventoryDetail){
        ValidatorUtils.validateEntity(inventoryDetail);
        inventoryDetailService.updateById(inventoryDetail);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:inventorydetail:delete")
    public R delete(@RequestBody Integer[] ids){
        inventoryDetailService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
