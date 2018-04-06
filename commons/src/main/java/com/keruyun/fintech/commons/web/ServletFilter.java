package com.keruyun.fintech.commons.web;

import com.keruyun.fintech.commons.Constant;
import com.keruyun.fintech.commons.MDCUtils;
import com.keruyun.fintech.commons.NetworkUtils;
import com.keruyun.fintech.commons.http.BufferedRequestWrapper;
import com.keruyun.fintech.commons.http.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author shuw
 * @version 1.0
 * @date 2016/10/20 14:41
 */
@Slf4j
public class ServletFilter implements Filter {
    //响应码为302时，controller header中跳转地址的key
    private static final String RESPONSE_HEADER_NAME_REDIRECT = "Location";
    private static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    private Set<String> excludesPattern;
    private String contextPath;

    @Override
    public void init(FilterConfig config) throws ServletException {
        contextPath = this.getContextPath(config.getServletContext());
        excludesPattern = new HashSet<>();
        String exclusions = config.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (StringUtils.isNotBlank(exclusions)) {
            String[] arr = exclusions.trim().split("\\s*,\\s*");
            excludesPattern.addAll(Arrays.asList(arr));
        }
        excludesPattern.add("favicon.ico");
        excludesPattern.add("health");
        excludesPattern.add("info");

        NetworkUtils.IpAddress ipAddress = NetworkUtils.getIp();
        String ip = ipAddress.getIP();
        MDCUtils.setIp(ip);
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String requestUri = req.getRequestURI();
        this.setMdcData(req);
        if (this.isInvalid(requestUri)) {
            this.refuseRequest(req,res);
            return;
        }
        if (this.isExclusion(requestUri)) {
            chain.doFilter(request, response);
            log.info("请求:method=" + req.getMethod());
            return;
        }

        long start = System.currentTimeMillis();

        StringBuilder logBuffer = new StringBuilder();
        this.logRequestParameters(logBuffer, req);
        String method = req.getMethod().toUpperCase();
        if ("POST".equals(method)) {
            BufferedRequestWrapper wrappedRequest = new BufferedRequestWrapper(req);
            this.logRequestBody(logBuffer, wrappedRequest);
            req = wrappedRequest;
        }
        log.info(logBuffer.toString());

        ResponseWrapper respWrapper = new ResponseWrapper(res);
        try {
            chain.doFilter(req, respWrapper);
        } finally {
            this.logResponse(respWrapper, start);
            this.removeMdcData();
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * 判断是否无效的请求地址
     * @param uri
     * @return
     */
    private boolean isInvalid(String uri) {
        if (StringUtils.isBlank(uri)) {
            return true;
        }
        return uri.lastIndexOf('.') > 0;
    }
    private boolean isExclusion(String requestURI) {
        if (excludesPattern == null) {
            return false;
        }

        if (contextPath != null && requestURI.startsWith(contextPath)) {
            requestURI = requestURI.substring(contextPath.length());
        }

        for (String pattern : excludesPattern) {
            if (this.matches(pattern, requestURI)) {
                return true;
            }
        }

        return false;
    }

    private void setMdcData(HttpServletRequest request) {
        MDCUtils.setMsgId(this.getMsgId(request));
        MDCUtils.setUrL(request.getRequestURI());
    }
    private void removeMdcData() {
        MDCUtils.removeMsgId();
        MDCUtils.removeUrL();
    }
    /**
     * 从http header中获取消息埋点id，没有则生成一个
     * @return
     */
    private String getMsgId(HttpServletRequest request) {
        String msgId = request.getHeader(MDCUtils.KEY_MSG_ID);
        if (StringUtils.isBlank(msgId)) {
            msgId = MDCUtils.generateId();
        }
        return msgId;
    }

    private void logRequestParameters(
            StringBuilder logBuffer, HttpServletRequest request) throws IOException {
        logBuffer.append("请求:method=").append(request.getMethod()).append(" Request:");
        Enumeration<String> en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String key = en.nextElement();
            if (StringUtils.isNotBlank(key)) {
                String value = request.getParameter(key);
                logBuffer.append("&" + key + "=" + value);
            }
        }
    }
    private void logRequestBody(StringBuilder logBuffer, BufferedRequestWrapper request) throws IOException {
        String requestBody;
        String contentType = request.getHeader("Content-Type");
        if (contentType == null) {
            contentType = "";
        }
        logBuffer.append(" content-type:").append(contentType).append(" ");
        logBuffer.append("RequestBody: ");
        if (contentType.contains("application/x-www-form-urlencoded")) {
            requestBody = new String(request.toByteArray(), Constant.DEFAULT_CHARSET_OBJ);
            logBuffer.append(URLDecoder.decode(requestBody, Constant.DEFAULT_CHARSET)
                    .replaceAll("\n|\r|  ", ""));
        }
        if (contentType.contains("application/json")) {
            requestBody = new String(request.toByteArray(), Constant.DEFAULT_CHARSET_OBJ);
            logBuffer.append(requestBody);//.replaceAll("\n|\r| ", "")
        }
    }

    private void logResponse(ResponseWrapper response, long startMs) {
        StringBuilder logBuffer = new StringBuilder();
        logBuffer.append("响应:Spent=")
                .append(System.currentTimeMillis() - startMs)
                .append("ms");
        try {
            byte[] data = response.toByteArray();
            String content = new String(data, Constant.DEFAULT_CHARSET_OBJ);//.replaceAll("\n|\r", "");
            logBuffer.append(" ,size=")
                    .append(data.length)
                    .append(" bytes");
            int status = response.getStatus();
            logBuffer.append(" ,controller status:")
                    .append(status);
            if (HttpStatus.SC_MOVED_TEMPORARILY == status) {
                //重定向
                logBuffer.append(", redirect to ").append(response.getHeader(RESPONSE_HEADER_NAME_REDIRECT));
            } else if (data.length > 0){
                logBuffer.append(" ,ResponseBody:");
                logBuffer.append(content);
            }
        } catch (Exception e) {
            log.warn("Failed to parse controller payload", e);
        }
        log.info(logBuffer.toString());
    }

    /**
     * 拒绝无效请求，打印日志并返回提示
     * @param reqest
     * @param response
     * @throws IOException
     */
    private void refuseRequest(HttpServletRequest reqest, HttpServletResponse response) throws IOException {
        StringBuilder logBuffer = new StringBuilder();
        //获取客户端ip
        logBuffer.append("client ip:").append(NetworkUtils.getClientIp(reqest));
        logBuffer.append(",无效");
        this.logRequestParameters(logBuffer, reqest);
        BufferedRequestWrapper wrappedRequest = new BufferedRequestWrapper(reqest);
        this.logRequestBody(logBuffer, wrappedRequest);
        log.warn(logBuffer.toString());
        PrintWriter pw = response.getWriter();
        pw.println("invalid request");
        pw.flush();
        pw.close();
    }

    private String getContextPath(ServletContext context) {
        String contextPath;
        try {
            contextPath = context.getContextPath();
            if (StringUtils.isEmpty(contextPath)) {
                contextPath = "/";
            }
        } catch (Exception e) {
            contextPath = "/";
        }

        return contextPath;
    }

    private boolean matches(String pattern, String source) {
        int start;
        if(pattern.endsWith("*")) {
            start = pattern.length() - 1;
            if(source.length() >= start && pattern.substring(0, start).equals(source.substring(0, start))) {
                return true;
            }
        } else if(pattern.startsWith("*")) {
            start = pattern.length() - 1;
            if(source.length() >= start && source.endsWith(pattern.substring(1))) {
                return true;
            }
        } else if(pattern.contains("*")) {
            start = pattern.indexOf("*");
            int end = pattern.lastIndexOf("*");
            if(source.startsWith(pattern.substring(0, start)) && source.endsWith(pattern.substring(end + 1))) {
                return true;
            }
        } else if(pattern.equals(source)) {
            return true;
        }

        return false;
    }
}
