package com.kinggrid.scan.seal.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CharacterEncoding implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String servletPath = ((HttpServletRequest) request).getServletPath();
        if (!(servletPath.contains(".pdf"))) {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            if (response instanceof HttpServletResponse) {
                ((HttpServletResponse)response).setHeader("content-type","application/json;charset=UTF-8");
            }
        } else {
            ((HttpServletResponse)response).setHeader("content-type","application/pdf;charset=UTF-8");
        }
        filterChain.doFilter(request,response);
    }

    public void destroy() {

    }
}
