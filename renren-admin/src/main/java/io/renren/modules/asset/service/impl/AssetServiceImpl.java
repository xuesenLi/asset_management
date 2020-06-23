package io.renren.modules.asset.service.impl;

import io.renren.modules.asset.dao.AssetOperRecordDao;
import io.renren.modules.asset.dao.RecordDetailDao;
import io.renren.modules.asset.entity.AssetOperRecordEntity;
import io.renren.modules.asset.entity.RecordDetailEntity;
import io.renren.modules.asset.enums.AssetOperTypeEnum;
import io.renren.modules.asset.enums.AssetStatusEnum;
import io.renren.modules.asset.enums.RecordStatusEnum;
import io.renren.modules.asset.enums.RecordTypeEnum;
import io.renren.modules.asset.form.AssetForm;
import io.renren.modules.asset.utils.GeneratorRecordNo;
import io.renren.modules.asset.vo.ResponseVo;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetDao;
import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.service.AssetService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("assetService")
public class AssetServiceImpl extends ServiceImpl<AssetDao, AssetEntity> implements AssetService {

    @Autowired
    private RecordDetailDao recordDetailDao;

    @Autowired
    private GeneratorRecordNo generatorRecordNo;

    @Autowired
    private AssetOperRecordDao assetOperRecordDao;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().orderByDesc("create_time")
        );

        return new PageUtils(page);
    }

    /**
     * 获取资产状态为 闲置的
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByTypeXZ(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().eq("asset_status", AssetStatusEnum.IDLE.getCode())
        );

        return new PageUtils(page);
    }

    /**
     * 获取资产状态为 在用的
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByTypeZY(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().eq("asset_status", AssetStatusEnum.IN_USE.getCode())
        );

        return new PageUtils(page);
    }

    /**
     * 获取资产状态为 借用的
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByTypeJY(Map<String, Object> params) {
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().eq("asset_status", AssetStatusEnum.BORROWING.getCode())
        );

        return new PageUtils(page);
    }

    /**
     * 获取资产状态为 闲置、在用、借用
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByTypeXZJ(Map<String, Object> params) {

        //构建 资产状态为 闲置、在用、借用 的集合
        List<Integer> assetStatusList = new ArrayList<>();
        assetStatusList.add(AssetStatusEnum.IDLE.getCode());
        assetStatusList.add(AssetStatusEnum.IN_USE.getCode());
        assetStatusList.add(AssetStatusEnum.BORROWING.getCode());

        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>()
                .in("asset_status", assetStatusList)
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByIn(Map<String, Object> params, String recordNo) {
        //1. 从单号详情表中查找 单号对应的资产id
        QueryWrapper<RecordDetailEntity> recordDetailQueryWrapper = new QueryWrapper<>();
        recordDetailQueryWrapper.eq("record_no", recordNo);
        List<RecordDetailEntity> recordDetailEntities = recordDetailDao.selectList(recordDetailQueryWrapper);

        List<Integer> assetIds = recordDetailEntities.stream().map(RecordDetailEntity::getAssetId).collect(Collectors.toList());

        //2. 通过资产id 返回资产信息集合
        IPage<AssetEntity> page = this.page(
                new Query<AssetEntity>().getPage(params),
                new QueryWrapper<AssetEntity>().in("id", assetIds)
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public ResponseVo batchInsertAsset(AssetForm form, SysUserEntity user) {

        List<AssetEntity> assetEntities = new ArrayList<>();
        for(int i = 0; i < form.getAssetNumber(); i++){
            AssetEntity assetEntity = new AssetEntity();
            BeanUtils.copyProperties(form, assetEntity);
            assetEntity.setAdminUserid(user.getUserId().intValue());
            assetEntity.setAdminUsername(user.getUsername());
            assetEntity.setAssetCode(generatorRecordNo.generateAssetNO(RecordTypeEnum.ASSET_NO));
            if(StringUtils.isEmpty(assetEntity.getUseOrgName())){
                //无新增使用组织， 入库为闲置
                assetEntity.setAssetStatus(AssetStatusEnum.IDLE.getCode());
            }else{
                //否则 在用
                assetEntity.setAssetStatus(AssetStatusEnum.IN_USE.getCode());
            }
            assetEntities.add(assetEntity);
        }

        //批量插入资产信息
        int count = baseMapper.batchInsertAsset(assetEntities);

        List<Integer> assetIds = assetEntities.stream()
                .map(AssetEntity::getId)
                .collect(Collectors.toList());

        //批量插入资产操作记录
        //资产操作记录 批量入库
        List<AssetOperRecordEntity> assetOperRecordEntities = new ArrayList<>();
        for (Integer assetId : assetIds) {
            AssetOperRecordEntity assetOperRecordEntity = new AssetOperRecordEntity();
            assetOperRecordEntity.setAssetId(assetId);
            //assetOperRecordEntity.setRecordNo(recordNo);
            assetOperRecordEntity.setOperType(AssetOperTypeEnum.IN_STORAGE.getCode());
            assetOperRecordEntity.setCreatedUserid(user.getUserId().intValue());
            assetOperRecordEntity.setCreatedUsername(user.getUsername());
            //报废 处理内容 ： 资产“已报废”
            assetOperRecordEntity.setOperContent("资产'入库'");

            assetOperRecordEntities.add(assetOperRecordEntity);
        }

        assetOperRecordDao.batchInsert(assetOperRecordEntities);

        return ResponseVo.success();
    }

    @Override
    public ResponseVo updateByOtherInfo(AssetEntity asset) {
        int i = baseMapper.updateByOtherInfo(asset);
        if(i < 1){
            return ResponseVo.success("修改失败");
        }
        return ResponseVo.success();
    }

}
