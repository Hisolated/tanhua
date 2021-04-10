package com.tanhua.common.advice;

import com.tanhua.common.exception.BusinessException;
import com.tanhua.common.exception.SystemException;
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

//    @ExceptionHandler(BusinessException.class)
//    public String businessException(Exception ex, Model m){
//        m.addAttribute("msg",ex.getMessage());
//        return "error.jsp";
//    }
//
//    @ExceptionHandler(SystemException.class)
//    public String systemException(Exception ex, Model m){
//        m.addAttribute("msg",ex.getMessage());
//        return "error.jsp";
//    }
//
//    @ExceptionHandler(Exception.class)
//    public String exception(Exception ex, Model m){
//        m.addAttribute("msg",ex.getMessage());
//        return "error.jsp";
//    }
}
