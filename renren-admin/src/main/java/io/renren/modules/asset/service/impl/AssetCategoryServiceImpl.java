package io.renren.modules.asset.service.impl;

import io.renren.common.annotation.DataFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetCategoryDao;
import io.renren.modules.asset.entity.AssetCategoryEntity;
import io.renren.modules.asset.service.AssetCategoryService;


@Service("assetCategoryService")
public class AssetCategoryServiceImpl extends ServiceImpl<AssetCategoryDao, AssetCategoryEntity> implements AssetCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetCategoryEntity> page = this.page(
                new Query<AssetCategoryEntity>().getPage(params),
                new QueryWrapper<AssetCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<AssetCategoryEntity> queryList(Map<String, Object> params) {
        return baseMapper.queryList(params);
    }


    @Override
    public List<Integer> queryCategoryList(Integer parentId) {
        return baseMapper.queryCategoryIdList(parentId);
    }

    @Override
    public List<Integer> getSubCategoryIdList(Integer categoryId) {
        //部门及子部门ID列表
        List<Integer> categoryIdList = new ArrayList<>();

        //获取子部门ID
        List<Integer> subIdList = queryCategoryList(categoryId);
        getDeptTreeList(subIdList, categoryIdList);

        return categoryIdList;
    }

    /**
     * 递归
     */
    private void getDeptTreeList(List<Integer> subIdList, List<Integer> deptIdList){
        for(Integer deptId : subIdList){
            List<Integer> list = queryCategoryList(deptId);
            if(list.size() > 0){
                getDeptTreeList(list, deptIdList);
            }

            deptIdList.add(deptId);
        }
    }
}
