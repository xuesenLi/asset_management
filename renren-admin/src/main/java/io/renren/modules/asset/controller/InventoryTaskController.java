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

import io.renren.modules.asset.entity.InventoryTaskEntity;
import io.renren.modules.asset.service.InventoryTaskService;
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
@RequestMapping("asset/inventorytask")
public class InventoryTaskController {
    @Autowired
    private InventoryTaskService inventoryTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:inventorytask:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inventoryTaskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("asset:inventorytask:info")
    public R info(@PathVariable("id") Integer id){
        InventoryTaskEntity inventoryTask = inventoryTaskService.getById(id);

        return R.ok().put("inventoryTask", inventoryTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:inventorytask:save")
    public R save(@RequestBody InventoryTaskEntity inventoryTask){
        inventoryTaskService.save(inventoryTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:inventorytask:update")
    public R update(@RequestBody InventoryTaskEntity inventoryTask){
        ValidatorUtils.validateEntity(inventoryTask);
        inventoryTaskService.updateById(inventoryTask);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:inventorytask:delete")
    public R delete(@RequestBody Integer[] ids){
        inventoryTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
