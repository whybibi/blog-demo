package cn.why.blog.config.exception;

import cn.why.blog.basic.entity.enums.SimpleError;
import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalException {
    /**
     * 自定义 REST 业务异常
     * @param e 异常类型
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public R<Object> handleBadRequest(Exception e) {
        /*
         * 业务逻辑异常
         */
        if (e instanceof ApiException) {
            IErrorCode errorCode = ((ApiException) e).getErrorCode();
            if (null != errorCode) {
                return R.failed(errorCode);
            }
            return R.failed(e.getMessage());
        }

        /*
         * 参数校验异常
         */
        if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            if (null != bindingResult && bindingResult.hasErrors()) {
                List<String> jsonList = new ArrayList<>();
                bindingResult.getFieldErrors().stream().forEach(fieldError -> {
                    jsonList.add(fieldError.getDefaultMessage());
                });
                return R.failed(jsonList.toString());
            }
        }
        if (e instanceof UnexpectedTypeException) {
            UnexpectedTypeException unexpectedTypeException = (UnexpectedTypeException) e;
            return R.failed(unexpectedTypeException.getMessage());
        }

        /**
         * shiro权限
         */
        if (e instanceof UnauthorizedException) {
            return R.failed(SimpleError.NO_PERMISSION);
        }
        if (e instanceof UnauthenticatedException) {
            return R.failed(SimpleError.NO_PERMISSION);
        }

        /**
         * 系统内部异常，打印异常栈
         */
        //    logger.error("Error: handleBadRequest StackTrace : {}", e);
        return R.failed(ApiErrorCode.FAILED);
    }

}
