package com.keruyun.fintech.commons.http;

import com.fasterxml.jackson.databind.JavaType;
import com.keruyun.fintech.commons.MapUtils;
import com.keruyun.fintech.commons.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author shuw
 * @version 1.0
 * @date 2016/10/24 11:45
 */
@Component
@Slf4j
public class RestInterface {
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final Charset DEFAULT_CHARSET_OBJ = Charset.forName("UTF-8");
    private static final MediaType MEDIA_TYPE_FORM = new MediaType("application", "x-www-form-urlencoded", DEFAULT_CHARSET_OBJ);
    private static final MediaType MEDIA_TYPE_JSON = new MediaType("application", "json", DEFAULT_CHARSET_OBJ);


    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    private JsonMapper jsonMapper;

    @PostConstruct
    public void init() {
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter : messageConverters) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
                break;
            }
        }
    }

    public <T> T postJson(String url, Object request, Class<T> responseType) throws RestClientException {
        return this.postJson(url, request, jsonMapper.contructType(responseType));
    }

    public <T> T postJson(String url, Object request, JavaType responseType) throws RestClientException {
        String res = this.postJson(url, request);
        return jsonMapper.fromJson(res, responseType);
    }

    public String postJsonWithHeader(String url, Object request,Map headerMap) throws RestClientException {
        return this.postJsonWithHeader(url,jsonMapper.toJson(request),headerMap);
    }
    public String postJsonWithHeader(String url, String json,Map headerMap) throws RestClientException {
        return this.exchange(url, HttpMethod.POST,headerMap,json);
    }

    public String postJson(String url, Object request) throws RestClientException {
        return this.exchange(url, HttpMethod.POST, jsonMapper.toJson(request));
    }

    public String postJson(String url, String json) throws RestClientException {
        return this.exchange(url, HttpMethod.POST, json);
    }

    public <T> T postJsonForm(String url, Object request, Class<T> responseType) throws RestClientException {
        return this.postJsonForm(url, request, jsonMapper.contructType(responseType));
    }

    public <T> T postJsonForm(String url, Object request, JavaType responseType) throws RestClientException {
        String res = this.postJsonForm(url, request);
        return jsonMapper.fromJson(res, responseType);
    }

    public String postJsonForm(String url, Object request) throws RestClientException {
        Map<String, Object> map = MapUtils.toJsonMap(request);
        return this.postForm(url, map);
    }

    public <T> T postForm(String url, Map<String, ? extends Object> request, Class<T> responseType) throws RestClientException {
        String res = this.postForm(url, request);
        return jsonMapper.fromJson(res, responseType);
    }

    public <T> T postForm(String url, Map<String, ? extends Object> request, JavaType responseType) throws RestClientException {
        String res = this.postForm(url, request);
        return jsonMapper.fromJson(res, responseType);
    }

    public String postForm(String url, Map<String, ? extends Object> request) throws RestClientException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap();
        for (Map.Entry<String, ?> entry : request.entrySet()) {
            Object value = entry.getValue();
            if (null == value) {
                continue;
            }
            String s;
            if (value instanceof String) {
                s = (String) value;
            } else {
                s = value.toString();
            }
            map.add(entry.getKey(), s);
        }
        HttpHeaders headers = this.getHeaders(MEDIA_TYPE_FORM);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        return this.httpExchange(url, HttpMethod.POST, httpEntity);
    }


    public String postForm(String url, Map<String, ? extends Object> request,Map header) throws RestClientException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap();
        for (Map.Entry<String, ?> entry : request.entrySet()) {
            Object value = entry.getValue();
            if (null == value) {
                continue;
            }
            String s;
            if (value instanceof String) {
                s = (String) value;
            } else {
                s = value.toString();
            }
            map.add(entry.getKey(), s);
        }
        HttpHeaders headers = this.getHeaders(MEDIA_TYPE_FORM,header);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        return this.httpExchange(url, HttpMethod.POST, httpEntity);
    }


    public <T> T get(String url, JavaType responseType) throws RestClientException {
        String res = this.get(url);
        return jsonMapper.fromJson(res, responseType);
    }

    public String getWithParameters(String url,Map parameterMap) throws RestClientException {
        if((null!=parameterMap)&&!(parameterMap.isEmpty())) {
            final StringBuilder queryString = new StringBuilder("?");
            parameterMap.forEach((k, v) -> {
                queryString.append(k.toString().toLowerCase());
                queryString.append("=");
                queryString.append(v);
                queryString.append("&");
            });
            url+=queryString.toString();
        }
        return this.exchange(url, HttpMethod.GET, null);
    }
    public String get(String url) throws RestClientException {
        return this.exchange(url, HttpMethod.GET, null);
    }

    private String exchange(String url, HttpMethod method, Map headerMap,String body) throws RestClientException {
        HttpHeaders headers = getHeaders(MEDIA_TYPE_JSON,headerMap);
        HttpEntity<String> httpEntity;
        if (HttpMethod.POST == method) {
            httpEntity = new HttpEntity<>(body, headers);
        } else {
            httpEntity = new HttpEntity<>(headers);
        }
        return this.httpExchange(url, method, httpEntity);
    }

    private String exchange(String url, HttpMethod method, String body) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> httpEntity;
        if (HttpMethod.POST == method) {
            headers.setContentType(MEDIA_TYPE_JSON);
            httpEntity = new HttpEntity<>(body, headers);
        } else {
            httpEntity = new HttpEntity<>(headers);
        }

        return this.httpExchange(url, method, httpEntity);
    }
    private HttpHeaders getHeaders(MediaType mediaType,Map headerMap) {
        HttpHeaders headers = this.getHeaders();
        if (null != mediaType) {
            headers.setContentType(mediaType);
        }
        if((null!=headerMap) && (!headerMap.isEmpty())){
            headerMap.forEach((k,v)->{
                headers.add(k.toString(),v.toString());
            });
        }

        return headers;
    }
    private HttpHeaders getHeaders(MediaType mediaType) {
        HttpHeaders headers = this.getHeaders();
        if (null != mediaType) {
            headers.setContentType(mediaType);
        }
        return headers;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();

        return headers;
    }

    public String httpExchange(String url, HttpMethod method, HttpEntity httpEntity) throws RestClientException {
        if (StringUtils.isBlank(url)) {
            throw new RestClientException("request url can not blank");
        }
        if (url.indexOf(' ') >= 0) {
            log.warn("url中含有空格，现将空格转换为20%，以便能正常执行请求");
            url = url.replaceAll(" ", "20%");
        }
        try {
            log.info("request: " + url + " ,method: " + method.name() + " ,header: " + httpEntity.getHeaders() + " ,body: " + httpEntity.getBody());
            long start = System.currentTimeMillis();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, httpEntity, String.class);
            long spent = System.currentTimeMillis() - start;
            HttpStatus status = responseEntity.getStatusCode();
            String res = responseEntity.getBody();
            log.info("controller status：{} ,spent [" + spent + "]ms ,body: {}", status, res);
            if (status == HttpStatus.OK) {
                return res;
            }
        } catch (RestClientException e) {
            log.error("request RestClientException：" + e.getLocalizedMessage());
            throw e;
        }
        return null;
    }
}
