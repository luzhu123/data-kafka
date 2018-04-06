package com.keruyun.fintech.commons;

import com.keruyun.fintech.commons.exception.BizException;
import com.keruyun.fintech.commons.exception.ExceptionTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author liangb
 * @version 1.0
 * @date 16/7/7 下午2:15
 */
@Slf4j
public class SignUtils {
    private static final String SIGN_SHA1 = "SHA1";
    private static final String SIGN_MD5 = "MD5";
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String KEY_STR = "&key=";//SHA1签名拼接的字符串

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @param charset    编码格式
     * @return 签名值
     */
    public static String rsa(String content, String privateKey, String charset) throws Exception {
        return RSA.sign(content.getBytes(charset), privateKey);
    }

    /**
     * 生成RSA签名
     *
     * @param text
     * @param privateKey
     * @return
     */
    public static String rsaByText(String text, String privateKey) throws BizException {
        String result = null;
        try {
            result = rsa(text, privateKey, Constant.DEFAULT_CHARSET);
        } catch (Exception e) {
            String errInfo = "计算RSA签名出错，请检查支付宝秘钥配置是否正确";
            log.error(errInfo + text, e);
            ExceptionTool.createBizException(ErrorCode.VALIDATE_SIGN_ERROR.getCode(), errInfo);
        }
        log.info("String: {} , RSA: {}", text, result);
        return result;
    }

    /**
     * 生成RSA签名
     *
     * @param paramesMap
     * @param privateKey
     * @return
     */
    public static String rsaByMap(TreeMap<String, String> paramesMap, String privateKey) throws BizException {
        StringBuilder sb = MapUtils.map2StringBuilder(paramesMap, null);
        return SignUtils.rsaByText(sb.toString(), privateKey);
    }

    /**
     * 校验RSA签名是否正确
     *
     * @param paramesMap
     * @param publicKey
     * @param sign
     * @return
     */
    public static boolean rsaVerify(TreeMap<String, String> paramesMap, String publicKey, String sign) {
        StringBuilder sb = MapUtils.map2StringBuilder(paramesMap, null);
        String text = sb.toString();
        log.debug("rsa签名验证text：{}, sign：{}", text, sign);
        try {
            return RSA.verify(text.getBytes(Constant.DEFAULT_CHARSET_OBJ), publicKey, sign);
        } catch (Exception e) {
            log.error("rsa verify error" + e.getMessage());
        }
        return false;
    }

    /**
     * 生成sha1签名(大写)
     *
     * @param map
     * @param key
     * @return
     */
    public static String sha1ByMap(TreeMap<String, Object> map, String key) throws BizException {
        String text = MapUtils.treeMap2ascString(map);//生成待签名字符串
        text = text + KEY_STR + key;//拼接key
        return sha1ByText(text);
    }

    /**
     * 生成sha1签名(大写)
     *
     * @param text
     * @return
     */
    public static String sha1ByText(String text) throws BizException {
        String sign = null;
        try {
            sign = SignUtils.security(text, SIGN_SHA1, null).toUpperCase();
        } catch (Exception e) {
            String errInfo = "计算SHA1签名出错";
            log.error(errInfo + text, e);
            ExceptionTool.createBizException(ErrorCode.VALIDATE_SIGN_ERROR.getCode(), errInfo);
        }
        log.info("String: {} , SHA1: {}", text, sign);
        return sign;
    }

    private static String security(String s, String algorithm, Charset charset) throws NoSuchAlgorithmException {
        if (null == charset) {
            charset = Constant.DEFAULT_CHARSET_OBJ;
        }
        byte[] btInput = s.getBytes(charset);
        // 获得摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance(algorithm);
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }

    /**
     * 取字符串的md5值(大写)，使用默认的UTF-8编码
     *
     * @param text
     * @return
     */
    public static String md5ByText(String text) throws BizException {
        return SignUtils.md5ByText(text, null);
    }

    /**
     * 取字符串的md5值(大写)，使用默认的UTF-8编码
     *
     * @param text
     * @return
     */
    public static String md5ByText(String text, Charset charset) throws BizException {
        String rs = null;
        try {
            rs = security(text, SIGN_MD5, charset).toUpperCase();
        } catch (Exception e) {
            String errInfo = "计算MD5签名出错";
            log.error(errInfo + rs, e);
            ExceptionTool.createBizException(ErrorCode.VALIDATE_SIGN_ERROR.getCode(), errInfo);
        }
        log.info("String: {} , MD5: {}", text, rs);
        return rs;
    }

    /**
     * 将treeMap中的键值对串联起来，并加上后缀suffix，最后取md5
     * 串联方式为：key1=value1&key2=value2&...&keyn=valuen，如果suffix不为空则加在后面
     *
     * @param map
     * @param suffix
     * @return
     */
    public static String md5ByMap(TreeMap<String, String> map, String suffix) throws BizException {
        StringBuilder sb = MapUtils.map2StringBuilder(map, null);
        if (null != suffix) {
            sb.append(suffix);
        }
        return SignUtils.md5ByText(sb.toString());
    }


    /**
     * 名称: getTokenKey
     * 作者：陈祥
     * 日期：2017年10月11日 下午2:23:36
     * 描述: 生成TokenKey
     * 参数： url-地址  tokenId-客户的tokenId  param-参数
     * 返回值： String 返回的tokenKey
     * 异常：
     *
     */
    public static String getTokenKey(String url, String tokenId, Map<String, String> param) {
        String paramStr = sortParam(param);
        return md5Hex(url + tokenId + paramStr);
    }

    /**
     *
     * 名称: sortParam
     * 作者：陈祥
     * 日期：2017年10月11日 下午2:25:12
     * 描述: 生成参数字符串，参数key按字典序排列
     * 参数： param-生成token需要的参数
     * 返回值： String
     * 异常：
     *
     */
    public static String sortParam(Map<String, String> param) {
        if (null == param || 0 == param.size()) {
            return "";
        }
        // 排序键，按照字母先后进行排序
        Iterator<String> iterator = param.keySet().iterator();
        String[] arr = new String[param.size()];
        for (int i = 0; iterator.hasNext(); i++) {
            arr[i] = iterator.next();
        }
        Arrays.sort(arr);
        // 生成进行MD5摘要的字符串
        StringBuffer sb = new StringBuffer();
        for (String key : arr) {
            String value = param.get(key);
            if (StringUtils.isNotBlank(value)) {
                sb.append(key).append("=").append(value).append(",");
            }
        }
        // 检查结果
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        } else {
            return "";
        }
    }

    /**
     *
     * 名称: md5Hex
     * 作者：陈祥
     * 日期：2017年10月11日 下午2:47:44
     * 描述: 对字符串进行md5摘要，然后转化成16进制字符串
     *       使用标准的md5摘要算法
     * 参数： text-需要进行摘要的字符串
     * 返回值： 进行MD5摘要以及16进制转化后的字符串
     *
     */
    public static String md5Hex(String text) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(text.trim().getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                int high = (bytes[i] >> 4) & 0x0f;
                int low = bytes[i] & 0x0f;
                sb.append(high > 9 ? (char) ((high - 10) + 'a') : (char) (high + '0'));
                sb.append(low > 9 ? (char) ((low - 10) + 'a') : (char) (low + '0'));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("系统不支持MD5算法"+e.getMessage());

        } catch (UnsupportedEncodingException e) {
            log.error("系统不支持指定的编码格式"+e.getMessage());
        }
        return null;
    }

}
