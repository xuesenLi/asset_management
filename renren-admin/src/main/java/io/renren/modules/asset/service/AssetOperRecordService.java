package io.renren.modules.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.asset.entity.AssetOperRecordEntity;
import io.renren.modules.asset.form.WxFinishForm;
import io.renren.modules.asset.vo.ResponseVo;
import io.renren.modules.sys.entity.SysUserEntity;

import java.util.Map;

/**
 *
 *
 * @author lxs
 * @email xuesenli@gmail.com
 * @date 2020-05-21 13:41:23
 */
public interface AssetOperRecordService extends IService<AssetOperRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 插入维修完成 记录
     * @param form
     * @return
     */
    ResponseVo insertWXFinish(WxFinishForm form, SysUserEntity user);
}

