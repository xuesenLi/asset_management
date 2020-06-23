package io.renren.modules.asset.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.common.utils.Constant;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.sys.controller.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.asset.entity.AssetCategoryEntity;
import io.renren.modules.asset.service.AssetCategoryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author lxs
 * @email sunlightcs@gmail.com
 * @date 2020-04-09 17:16:23
 */
@RestController
@RequestMapping("asset/assetcategory")
@Slf4j
public class AssetCategoryController extends AbstractController {
    @Autowired
    private AssetCategoryService assetCategoryService;


    /**
     * 选择分类(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("asset:assetcategory:select")
    public R select(){
        List<AssetCategoryEntity> deptList = assetCategoryService.queryList(new HashMap<String, Object>());
        AssetCategoryEntity root = new AssetCategoryEntity();
        root.setCategoryId(0);
        root.setName("一级分类");
        root.setParentId(-1);
        root.setOpen(true);
        deptList.add(root);

        //添加一级部门
        /*if(getUserId() == Constant.SUPER_ADMIN){
            AssetCategoryEntity root = new AssetCategoryEntity();
            root.setCategoryId(0);
            root.setName("一级分类");
            root.setParentId(-1);
            root.setOpen(true);
            deptList.add(root);
        }*/

        return R.ok().put("categoryList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    @RequiresPermissions("asset:assetcategory:list")
    public R info(){
        Integer categoryId = 0;
        List<AssetCategoryEntity> categoryList = assetCategoryService.queryList(new HashMap<String, Object>());
        Integer parentId = null;
        for(AssetCategoryEntity assetCategoryEntity : categoryList){
            if(parentId == null){
                parentId = assetCategoryEntity.getParentId();
                continue;
            }

            if(parentId > assetCategoryEntity.getParentId().longValue()){
                parentId = assetCategoryEntity.getParentId();
            }
        }
        categoryId = parentId;
        /*if(getUserId() != Constant.SUPER_ADMIN){
            List<AssetCategoryEntity> categoryList = assetCategoryService.queryList(new HashMap<String, Object>());
            Integer parentId = null;
            for(AssetCategoryEntity assetCategoryEntity : categoryList){
                if(parentId == null){
                    parentId = assetCategoryEntity.getParentId();
                    continue;
                }

                if(parentId > assetCategoryEntity.getParentId().longValue()){
                    parentId = assetCategoryEntity.getParentId();
                }
            }
            categoryId = parentId;
        }*/

        return R.ok().put("categoryId", categoryId);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{categoryId}")
    @RequiresPermissions("asset:assetcategory:info")
    public R info(@PathVariable("categoryId") Integer categoryId){
        AssetCategoryEntity assetCategory = assetCategoryService.getById(categoryId);

        return R.ok().put("assetCategory", assetCategory);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("asset:assetcategory:list")
    public List<AssetCategoryEntity> list(){
        List<AssetCategoryEntity> categoryList = assetCategoryService.queryList(new HashMap<String, Object>());

        return categoryList;
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("asset:assetcategory:save")
    public R save(@RequestBody AssetCategoryEntity assetCategory){
        assetCategoryService.save(assetCategory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("asset:assetcategory:update")
    public R update(@RequestBody AssetCategoryEntity assetCategory){

        assetCategoryService.updateById(assetCategory);

        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("asset:assetcategory:delete")
    public R delete(Integer categoryId){
        //判断是否有子部门
        List<Integer> categoryList = assetCategoryService.queryCategoryList(categoryId);
        if(categoryList.size() > 0){
            return R.error("请先删除子部门");
        }

        assetCategoryService.removeById(categoryId);

        return R.ok();
    }

}
