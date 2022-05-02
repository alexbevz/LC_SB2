package ru.bevz.LC_SB2.util;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String arguments = request.getQueryString() != null ? request.getQueryString() : "";
            String url = request.getRequestURI().toString() + "?" + arguments;
            response.setHeader("Turbolinks-Location", url);
        }
    }
}
