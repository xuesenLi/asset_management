package io.renren.modules.asset.service.impl;

import io.renren.modules.asset.dao.AssetDao;
import io.renren.modules.asset.entity.AssetEntity;
import io.renren.modules.asset.enums.AssetOperTypeEnum;
import io.renren.modules.asset.enums.AssetStatusEnum;
import io.renren.modules.asset.form.WxFinishForm;
import io.renren.modules.asset.vo.ResponseVo;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.asset.dao.AssetOperRecordDao;
import io.renren.modules.asset.entity.AssetOperRecordEntity;
import io.renren.modules.asset.service.AssetOperRecordService;
import org.springframework.transaction.annotation.Transactional;


@Service("assetOperRecordService")
public class AssetOperRecordServiceImpl extends ServiceImpl<AssetOperRecordDao, AssetOperRecordEntity> implements AssetOperRecordService {

    @Autowired
    private AssetDao assetDao;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AssetOperRecordEntity> page = this.page(
                new Query<AssetOperRecordEntity>().getPage(params),
                new QueryWrapper<AssetOperRecordEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public ResponseVo insertWXFinish(WxFinishForm form, SysUserEntity user) {

        AssetEntity assetEntity = assetDao.selectById(form.getId());
        //判断资产状态 是否为 维修中
        if(!AssetStatusEnum.UNDER_REPAIR.getCode().equals(assetEntity.getAssetStatus())){
            return ResponseVo.error("维修中的资产才能进行维修完成");
        }

        //1. 通过资产id 获取资产记录中最新的一条维修记录的 维修单号
        String recordNo = baseMapper.findRecordNoByAssetId(form.getId());
        AssetOperRecordEntity assetOperRecordEntity = new AssetOperRecordEntity();

        //  **维修内容封装 ： 维修说明， 实际维修费用：99 元** 。
        assetOperRecordEntity.setOperContent(form.getExplain() + ", " + "实际维修费用: " + form.getRepairCost() + "元");
        assetOperRecordEntity.setCreatedUserid(user.getUserId().intValue());
        assetOperRecordEntity.setCreatedUsername(user.getUsername());
        assetOperRecordEntity.setAssetId(form.getId());
        assetOperRecordEntity.setRecordNo(recordNo);
        assetOperRecordEntity.setOperType(AssetOperTypeEnum.REPAIR_FINISH.getCode());
        //2. 插入资产操作详情。
        int insert = baseMapper.insert(assetOperRecordEntity);

        //3. 将当前资产状态 恢复为上一次资产状态
        int update = assetDao.updateAssetStatusByPreStatus(form.getId());

        return ResponseVo.success();

    }

    @Override
    public PageUtils queryPageByAssetId(Map<String, Object> params, String assetId) {

        IPage<AssetOperRecordEntity> page = this.page(
                new Query<AssetOperRecordEntity>().getPage(params),
                new QueryWrapper<AssetOperRecordEntity>()
                        .eq("asset_id", Integer.valueOf(assetId))
                        .orderByDesc("oper_time")
        );

        return new PageUtils(page);
    }
}
