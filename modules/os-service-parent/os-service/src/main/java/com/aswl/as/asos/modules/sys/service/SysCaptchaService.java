/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.modules.sys.entity.SysCaptchaEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.awt.image.BufferedImage;

/**
 * 验证码
 *
 * @author admin@gzaswl.net
 */
public interface SysCaptchaService extends IService<SysCaptchaEntity> {

    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
