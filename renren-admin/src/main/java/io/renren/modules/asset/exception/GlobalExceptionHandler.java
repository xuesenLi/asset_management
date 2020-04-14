package io.renren.modules.asset.exception;

import io.renren.modules.asset.enums.ResponseEnum;
import io.renren.modules.asset.vo.ResponseVo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Mr.Li
 * @date 2019/12/28 - 18:05
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseVo GlobalException(GlobalException e) {
        return ResponseVo.error(ResponseEnum.ERROR, e.getMsg());
    }


    /**
     * 异常处理 @valid
     * 这样就不用在controller层去获取  BindingResult 对象了
     *
     * @param e
     * @return
     */
 /*   @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return ResponseVo.error(ResponseEnum.PARAM_ERROR, bindingResult);
    }*/

}
