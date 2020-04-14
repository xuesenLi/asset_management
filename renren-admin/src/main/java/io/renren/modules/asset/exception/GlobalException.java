package com.lxs.mall.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mr.Li
 * @date 2019/12/28 - 18:08
 */
public class GlobalException extends RuntimeException {

    @Getter
    @Setter
    private String msg;

    public GlobalException(String msg) {
        this.msg = msg;
    }
}
