package io.renren.modules.asset.service.impl;

import io.renren.common.utils.R;
import io.renren.modules.asset.dao.*;
import io.renren.modules.asset.entity.AssetAuditEntity;
import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.entity.RecordDetailEntity;
import io.renren.modules.asset.enums.AssetStatusEnum;
import io.renren.modules.asset.enums.RecordStatusEnum;
import io.renren.modules.asset.enums.RecordTypeEnum;
import io.renren.modules.asset.utils.GeneratorRecordNo;
import io.renren.modules.asset.vo.AssetChangeVo;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.dao.SysUserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.entity.AssetChangeEntity;
import io.renren.modules.asset.service.AssetChangeService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("assetChangeService")
@Slf4j
public class AssetChangeServiceImpl extends ServiceImpl<AssetChangeDao, AssetChangeEntity> implements AssetChangeService {

    @Autowired
    private GeneratorRecordNo generatorRecordNo;

    @Autowired
    private RecordDetailDao recordDetailDao;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private AssetAuditDao assetAuditDao;

    @Autowired
    private AssetCategoryDao assetCategoryDao;

    @Autowired
    private SysDeptDao sysDeptDao;

    @Autowired
    private AssetAreaDao assetAreaDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetChangeEntity> page = this.page(
                new Query<AssetChangeEntity>().getPage(params),
                new QueryWrapper<AssetChangeEntity>().orderByDesc("created_time")
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void insert(AssetChangeEntity assetChange) {
        //1. 生成单号
        String recordNo = generatorRecordNo.generateRecordNo(RecordTypeEnum.ASSET_CHANGE);
        assetChange.setRecordNo(recordNo);
        assetChange.setAssetNum(assetChange.getAssets().size());
        //设置单号状态   待审批
        assetChange.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());
        //设置 变更内容：
        StringBuilder stringBuilder = new StringBuilder();
        if(!StringUtils.isEmpty(assetChange.getAssetName())){
            stringBuilder.append(" [资产名称]变更为：").append(assetChange.getAssetName());
        }
        if(!StringUtils.isEmpty(assetChange.getCategoryName())){
            stringBuilder.append(" [资产分类]变更为：").append(assetChange.getCategoryName());
        }
        if(!StringUtils.isEmpty(assetChange.getOrgName())){
            stringBuilder.append(" [所属部门]变更为：").append(assetChange.getOrgName());
        }
        if(!StringUtils.isEmpty(assetChange.getUseOrgName())){
            stringBuilder.append(" [使用部门]变更为：").append(assetChange.getUseOrgName());
        }
        if(!StringUtils.isEmpty(assetChange.getEmpName())){
            stringBuilder.append(" [使用人]变更为：").append(assetChange.getEmpName());
        }
        if(!StringUtils.isEmpty(assetChange.getAreaName())){
            stringBuilder.append(" [区域名称]变更为：").append(assetChange.getAreaName());
        }
        assetChange.setChangeContent(stringBuilder.toString());

        log.info("assetChange = {}", assetChange);
        //2. 入库
        //变更单号 入库
        baseMapper.insert(assetChange);

        List<RecordDetailEntity> recordDetailEntitys = new ArrayList<>();
        assetChange.getAssets().forEach(assetId ->{
            RecordDetailEntity recordDetailEntity = new RecordDetailEntity();
            recordDetailEntity.setAssetId(assetId);
            recordDetailEntity.setRecordNo(recordNo);
            recordDetailEntitys.add(recordDetailEntity);
        });

        //单号详情 批量入库
        recordDetailDao.batchInsert(recordDetailEntitys);

        //批量修改  将当前操作的资产状态改为 待审批， 由于要恢复为原来的资产状态，所以将状态的值 +10 后面恢复在 -10
        //assetDao.batchUpdateByIds(assetChange.getAssets(), AssetStatusEnum.PENDING_APPROVAL.getCode());
        assetDao.batchAssetStatusAdd10(assetChange.getAssets());

        //向审批表中 插入 待审批 的 记录
        AssetAuditEntity assetAuditEntity = new AssetAuditEntity();
        assetAuditEntity.setRecordNo(recordNo);
        assetAuditEntity.setRecordStatus(RecordStatusEnum.PENDING_APPROVAL.getCode());
        assetAuditEntity.setRecordType(RecordTypeEnum.ASSET_CHANGE.getCode());
        assetAuditEntity.setCreatedUsername(assetChange.getCreatedUsername());

        assetAuditDao.insert(assetAuditEntity);
    }

    @Override
    public AssetChangeVo getByRecordNo(String recordNo) {
        QueryWrapper<AssetChangeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_no", recordNo);
        AssetChangeEntity assetChangeEntity = baseMapper.selectOne(queryWrapper);

        //构建 AssetChangeVo
        AssetChangeVo assetChangeVo = new AssetChangeVo();
        assetChangeVo.setRecordStatus(assetChangeEntity.getRecordStatus());
        assetChangeVo.setRecordRemarks(assetChangeEntity.getRecordRemarks());
        assetChangeVo.setAssetName(assetChangeEntity.getAssetName());
        if(!StringUtils.isEmpty(assetChangeEntity.getCategoryId()))
            assetChangeVo.setCategoryName(assetCategoryDao.selectById(assetChangeEntity.getCategoryId()).getName());
        if(!StringUtils.isEmpty(assetChangeEntity.getOrgId()))
            assetChangeVo.setOrgName(sysDeptDao.selectById(assetChangeEntity.getOrgId()).getName());
        if(!StringUtils.isEmpty(assetChangeEntity.getUseOrgId()))
            assetChangeVo.setUseOrgName(sysDeptDao.selectById(assetChangeEntity.getUseOrgId()).getName());
        if(!StringUtils.isEmpty(assetChangeEntity.getEmpId()))
            assetChangeVo.setEmpName(sysUserDao.selectById(assetChangeEntity.getEmpId()).getUsername());
        if(!StringUtils.isEmpty(assetChangeEntity.getAreaId()))
            assetChangeVo.setAreaName(assetAreaDao.selectById(assetChangeEntity.getAreaId()).getAreaName());
        return assetChangeVo;

    }

    /**
     * 资产变更 驳回  将资产状态 恢复为 原来的
     * @param recordNo
     * @return
     */
    @Override
    @Transactional
    public R assetRebut(String recordNo) {
        //1. 通过单号查找详情
        List<Integer> assetIds = recordDetailDao.selectByRecordNo(recordNo);
        if(assetIds.size() == 0){
            return R.error("当前资产不存在");
        }

        //批量修改  将当前操作的资产状态改为 待审批， 由于要恢复为原来的资产状态，所以将状态的值 +10 后面恢复在 -10
        assetDao.batchAssetStatusReduce10(assetIds);

        //修改单号状态为 被驳回
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        //修改审批表  被驳回
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.BE_DISMISSED.getCode());

        return R.ok();
    }

    @Override
    @Transactional
    public R assetAgree(String recordNo) {
        //1. 通过单号查找详情
        List<Integer> assetIds = recordDetailDao.selectByRecordNo(recordNo);
        if(assetIds.size() == 0){
            return R.error("当前资产不存在");
        }
        //2. 修改资产的相关属性
        // 获取修改类容 assetEntity
        AssetEntity assetEntity = new AssetEntity();
        QueryWrapper<AssetChangeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("record_no", recordNo);
        AssetChangeEntity assetChangeEntity = baseMapper.selectOne(queryWrapper);
        assetEntity.setAssetName(assetChangeEntity.getAssetName());
        //TODO  id 没修改问题
        if(!StringUtils.isEmpty(assetChangeEntity.getCategoryId()))
            assetEntity.setCategoryName(assetCategoryDao.selectById(assetChangeEntity.getCategoryId()).getName());
        if(!StringUtils.isEmpty(assetChangeEntity.getOrgId()))
            assetEntity.setOrgName(sysDeptDao.selectById(assetChangeEntity.getOrgId()).getName());
        if(!StringUtils.isEmpty(assetChangeEntity.getUseOrgId()))
            assetEntity.setUseOrgName(sysDeptDao.selectById(assetChangeEntity.getUseOrgId()).getName());
        if(!StringUtils.isEmpty(assetChangeEntity.getEmpId()))
            assetEntity.setEmpName(sysUserDao.selectById(assetChangeEntity.getEmpId()).getUsername());
        if(!StringUtils.isEmpty(assetChangeEntity.getAreaId()))
            assetEntity.setAreaName(assetAreaDao.selectById(assetChangeEntity.getAreaId()).getAreaName());

        //根据资产id 批量修改 资产内容
        for (Integer id : assetIds) {
            assetEntity.setId(id);
            assetDao.updateById(assetEntity);
        }

        //3. 批量修改  将当前操作的资产状态改为 待审批， 由于要恢复为原来的资产状态，所以将状态的值 +10 后面恢复在 -10
        assetDao.batchAssetStatusReduce10(assetIds);

        //4. 修改单号状态为 已同意
        baseMapper.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());

        //修改审批表  被驳回
        assetAuditDao.updateByRecordStatus(recordNo, RecordStatusEnum.AGREED.getCode());
        return R.ok();
    }

}
