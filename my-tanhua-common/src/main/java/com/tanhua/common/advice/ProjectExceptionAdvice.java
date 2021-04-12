package com.tanhua.common.advice;

import com.tanhua.common.exception.BusinessException;
import com.tanhua.common.exception.SystemException;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/3/20 21:01
 */
@Component
@ControllerAdvice
public class ProjectExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Model> businessException(BusinessException ex, Model m){
        m.addAttribute("msg",ex.getMessage());
        return ResponseEntity.status(ex.getCode()).body(m);

    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Model> systemException(Exception ex, Model m){
        m.addAttribute("msg",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(m);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Model> exception(Exception ex, Model m){
        m.addAttribute("msg",ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(m);

    }
}
