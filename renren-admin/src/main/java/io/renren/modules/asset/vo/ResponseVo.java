package io.renren.modules.asset.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.renren.modules.asset.enums.ResponseEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * @author Mr.Li
 * @date 2020/4/10 - 11:01
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {

    private Integer code;

    private String msg;

    private T data;

    private ResponseVo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResponseVo(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> ResponseVo<T> success() {
        return new ResponseVo<T>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg());
    }

    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<T>(ResponseEnum.SUCCESS.getCode(), data);
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum) {
        return new ResponseVo<T>(responseEnum.getCode(), responseEnum.getMsg());
    }

    public static <T> ResponseVo<T> error(String msg) {
        return new ResponseVo<T>(ResponseEnum.fail.getCode(), msg);
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, String msg) {
        return new ResponseVo<T>(responseEnum.getCode(), msg);
    }

    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult) {
        return new ResponseVo<T>(responseEnum.getCode(), Objects.requireNonNull(bindingResult.getFieldError()).getField() + " " +
                bindingResult.getFieldError().getDefaultMessage());
    }
}
