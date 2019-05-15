package com.tdwl.wife.sql.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.Entry;

public class HttpClientUtils {

    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String CHARTSET = "UTF-8";
    private static final int CONNTIMEOUT = 30000;
    private static final int READTIMEOUT = 30000;
    private static CloseableHttpClient httpClient = null;

    static {
        setProxy(null, null);
    }

    public static void  setProxy(String ip , Integer port){
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();

        // SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
        // public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // return true;
        // }
        // }).build();
        // SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加到200
        cm.setMaxTotal(200);
        // 将每个路由基础的连接增加到20
        cm.setDefaultMaxPerRoute(20);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };
        if (StringUtils.isNotEmpty(ip) && port != null){
            HttpHost proxy = new HttpHost(ip, port);
            httpClient = HttpClients.custom().setConnectionManager(cm).setProxy(proxy).setRetryHandler(httpRequestRetryHandler).build();
        }else {
            httpClient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(httpRequestRetryHandler).build();
        }
    }

    public static <T> T postForObject(String url, String body, Class<T> toClass) throws Exception {
        String res = post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, CHARTSET, CONNTIMEOUT, READTIMEOUT);
        return JsonUtils.parseFasterToObject(res, toClass);
    }

    public static Map<?, ?> postForMap(String url, String body) throws Exception {
        String res = post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, CHARTSET, CONNTIMEOUT, READTIMEOUT);
        return JsonUtils.parseToMap(res);
    }

    public static String post(String url, String body) throws Exception {
        return post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, CHARTSET, CONNTIMEOUT, READTIMEOUT);
    }

    public static String post(String url, String body, String charset, Integer connTimeout, Integer readTimeout) throws Exception {
        return post(url, body, APPLICATION_X_WWW_FORM_URLENCODED, charset, connTimeout, readTimeout);
    }

    public static <T> T postFormData(String url, Map<String, String> params, Class<T> toClass) throws Exception {
        String res = postForm(url, params, null, CONNTIMEOUT, READTIMEOUT);
        return JsonUtils.parseFasterToObject(res, toClass);
    }

    public static String postFormData(String url, Map<String, String> params) throws Exception {
        return postForm(url, params, null, CONNTIMEOUT, READTIMEOUT);
    }

    public static String postFormData(String url, Map<String, String> params, Integer connTimeout, Integer readTimeout) throws Exception {
        return postForm(url, params, null, connTimeout, readTimeout);
    }

    public static <T> T getForObject(String url, Class<T> toClass) throws Exception {
        String res = get(url, CHARTSET, null, null);
        return JsonUtils.parseFasterToObject(res, toClass);
    }

    public static String get(String url) throws Exception {
        return get(url, CHARTSET, null, null);
    }

    public static String get(String url,Map<String,String> headMap) throws Exception {
        return getByHeader(url, CHARTSET, null, null, headMap);
    }

    public static String get(String url, String charset) throws Exception {
        return get(url, charset, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * Post请求
     * 
     * @throws Exception
     */
    public static String post(String url, String body, String mimeType, String charset, Integer connTimeout, Integer readTimeout)
            throws Exception {
        HttpPost post = new HttpPost(url);
        String result = null;
        try {
            if (StringUtils.isNotBlank(body)) {
                HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
                post.setEntity(entity);
            }
            RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
            post.setConfig(customReqConfig);
            post.setHeader("Content-Type","application/x-www-form-urlencoded");
            HttpResponse res = httpClient.execute(post);
            result = convertInputStreamToString(res.getEntity().getContent(), charset);
        } finally {
            post.releaseConnection();
        }
        return result;
    }

    /**
     * 提交form表单
     * 
     * @param url
     * @throws Exception
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers, Integer connTimeout,
            Integer readTimeout) throws Exception {
        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                Set<Entry<String, String>> entrySet = params.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
            post.setConfig(customReqConfig);
            HttpResponse res = httpClient.execute(post);
            return convertInputStreamToString(res.getEntity().getContent(), CHARTSET);
        } finally {
            post.releaseConnection();
        }
    }

    /**
     * GET请求
     *
     * @throws Exception
     */
    public static String get(String url, String charset, Integer connTimeout, Integer readTimeout) throws Exception {
        HttpGet get = new HttpGet(url);
        String result = "";
        try {
            RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
            get.setConfig(customReqConfig);
            HttpResponse res = httpClient.execute(get);
            result = convertInputStreamToString(res.getEntity().getContent(), charset);
        } finally {
            get.releaseConnection();
        }
        return result;
    }

    /**
     * GET请求
     *
     * @throws Exception
     */
    public static String getByHeader(String url, String charset, Integer connTimeout, Integer readTimeout, Map<String,String> headMap) throws Exception {
        HttpGet get = new HttpGet(url);
        for (Entry<String, String> entry : headMap.entrySet()) {
            get.setHeader(entry.getKey(),entry.getValue());
        }
        String result = "";
        try {
            RequestConfig customReqConfig = getCustomReqConfig(connTimeout, readTimeout);
            get.setConfig(customReqConfig);
            HttpResponse res = httpClient.execute(get);
            result = convertInputStreamToString(res.getEntity().getContent(), charset);
        } finally {
            get.releaseConnection();
        }
        return result;
    }

    private static RequestConfig getCustomReqConfig(Integer connTimeout, Integer readTimeout) {
        Builder customReqConf = RequestConfig.custom();
        if (connTimeout != null) {
            customReqConf.setConnectTimeout(connTimeout);
        }
        if (readTimeout != null) {
            customReqConf.setSocketTimeout(readTimeout);
        }
        return customReqConf.build();
    }

    private static String convertInputStreamToString(InputStream inputStream, String charset) throws UnsupportedEncodingException,
            IOException {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            return buffer.toString();
        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    public static void main(String[] args) {
        try {
//            HttpClientUtils.setProxy("182.88.128.170", 8123);
//            String currencyMarket= HttpClientUtils.get("https://api.jinse.com/v4/market/currencyList?position=m_market&_source=m");
            Map<String ,String> headerMap = new HashMap<>();
            headerMap.put("X-App-Info","2.5.0/baidu/android");
            String currencyMarket= HttpClientUtils.get("https://api.finbtc.net/app/ranklist/coin/all?sortColumn=hsl24h&dire=DESC",headerMap);
            System.out.println(currencyMarket);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
