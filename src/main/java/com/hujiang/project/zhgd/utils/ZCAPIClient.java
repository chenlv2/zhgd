package com.hujiang.project.zhgd.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hujiang.common.utils.JsonUtils;
import com.hujiang.common.utils.ThreadUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ZCAPIClient {

    private static Logger logger = Logger.getLogger(ZCAPIClient.class);

    /**
     * 中车上报设备数据
     *
     * @param url        请求地址
     * @param jsonObject 提交的数据
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String reportedData(String url, JSONObject jsonObject) throws URISyntaxException, IOException {
        //获取时间
        String simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //拼接签名
        StringBuilder str4MD5 = new StringBuilder();
        str4MD5.append("api_key").append(jsonObject.getString("api_key"))
                .append("api_version").append(jsonObject.getString("api_version"))
                .append("body").append(jsonObject.getString("body"))
                .append("eng_code=").append(jsonObject.getString("eng_code"))
                .append("project_code=").append(jsonObject.getString("project_code"))
                .append("timestamp").append(simpleDateFormat);
        //转MD5 32位大写
        String serverSignature = Tools.encodeToMD5(str4MD5.toString()).toUpperCase();
        //设置签名
        jsonObject.put("signature", serverSignature);
        jsonObject.put("timestamp", simpleDateFormat);
        System.out.println(jsonObject);
        String s = Util.httpPostWithJSON(Constants.ZCAPI + url, jsonObject);
        System.out.println("请求服务：" + url);
        return s;
    }

    /**
     * 上报惠州住建局两制
     */
    public static String xieTwoSystems(String url, JSONObject jsonObject) throws IOException, URISyntaxException {
        //获取时间
        String simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

//        StringBuilder str4MD5 = new StringBuilder();
//        str4MD5.append("api_key").append(jsonObject.getString("api_key"))
//                .append("api_version").append(jsonObject.getString("api_version"))
//                .append("timestamp").append(simpleDateFormat)
//                .append("body").append(jsonObject.getString("body"));

        String save = jsonObject.getString("api_key") + "api_key" + jsonObject.getString("api_key") + "api_version" + jsonObject.getString("api_version")
                + "body" + jsonObject.getString("body") + "timestamp" + simpleDateFormat + jsonObject.getString("api_key");

        //转MD5 32位大写
        String serverSignature = Tools.encodeToMD5(save);
        //设置签名
        jsonObject.put("signature", serverSignature);
        jsonObject.put("timestamp", simpleDateFormat);
        String s = Util.httpPostWithJSON(Constants.XIETWOSYSTEMS + url, jsonObject);
        return s;
    }

    /**
     * 中车上报设备数据
     *
     * @param url        请求地址
     * @param jsonObject 提交的数据
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String reportedData2019(String url, JSONObject jsonObject) throws URISyntaxException, IOException {

        ThreadUtils.async(new Runnable() {
            private final Logger logger = (Logger) LoggerFactory.getLogger(ZCAPIClient.class);

            @Override
            public void run() {
                try {
                    //获取时间
                    String simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    //拼接签名
                    StringBuilder str4MD5 = new StringBuilder();
                    str4MD5.append("api_key").append(jsonObject.getString("api_key"))
                            .append("api_version").append(jsonObject.getString("api_version"))
                            .append("body").append(jsonObject.getString("body"))
                            .append("eng_code=").append(jsonObject.getString("eng_code"))
                            .append("project_code=").append(jsonObject.getString("project_code"))
                            .append("timestamp").append(simpleDateFormat);
                    //转MD5 32位大写
                    String serverSignature = Tools.encodeToMD5(str4MD5.toString()).toUpperCase();
                    //设置签名
                    jsonObject.put("signature", serverSignature);
                    jsonObject.put("timestamp", simpleDateFormat);
                    System.out.println(jsonObject);

                    Util.httpPostWithJSON(Constants.Version + url, jsonObject);
                } catch (URISyntaxException e) {
                    logger.error("中车上报设备数据错误: " + e.getMessage());
                } catch (IOException e) {
                    logger.error("中车上报设备数据错误: " + e.getMessage());
                } catch (Exception e) {
                    logger.error("中车上报设备数据错误: " + e.getMessage());
                }
                logger.error("请求服务：" + url);
                logger.error("请求参数：" + JsonUtils.toJson(jsonObject));
            }
        });

        return "{\"result\":\"\", \"code\":0, \"message\":\"\"}";
    }

    /**
     * 上传至城安院获取项目id（市管项目）
     */
    public static final String reportedCay2019(String url, JSONObject jsonObject) throws URISyntaxException, IOException {
        String returnDate = Util.httpPostWithJSON(Constants.CAY_CS + url + Constants.TOKEN_CS, jsonObject);
        logger.info("请求服务：" + Constants.CAY_CS + url + Constants.TOKEN_CS);
        JSONObject jSONObject = JSONObject.parseObject(returnDate);
        String k = null;
        if (!"0".equals(jSONObject.getString("total"))) {
            JSONArray data = jSONObject.getJSONArray("res");
            JSONObject datas = data.getJSONObject(0);
            k = datas.getString("xmid");
        }
        return k;
    }

    /**
     * 上传至城安院获取项目监督编号和工程ID（市管项目）
     */
    public static final JSONObject reportedCay(String url, JSONObject jsonObject) throws URISyntaxException, IOException {
        String returnJson = Util.httpPostWithJSON(Constants.CAY_CS + url + Constants.TOKEN_CS, jsonObject);
        logger.info("请求服务：" + Constants.CAY_CS + url + Constants.TOKEN_CS);
        JSONObject jSONObject = JSONObject.parseObject(returnJson);
//        JSONArray data = a.getJSONArray("res");
//        JSONObject datas = data.getJSONObject(0);
//        String gcid = datas.getString("gcid");
//        String jdbh = datas.getString("jdbh");
//        String xmid = datas.getString("xmid");
//        JSONObject object = new JSONObject();
//        object.put("gcid",gcid);
//        object.put("jdbh",jdbh);
//        object.put("xmid",xmid);
        return jSONObject;
    }

    /**
     * @Author xieya
     * @Description 城安院(区管项目)模糊查询项目工程信息
     * @Date 2020/4/1 10:45
     * @param url
     * @param jsonObject
     * @return com.alibaba.fastjson.JSONObject
     **/
    public static final JSONObject cayArea(String url, JSONObject jsonObject) throws URISyntaxException, IOException {
        logger.info("请求城安院区管项目接口地址：" + Constants.CAY_QGXM + url + Constants.TOKEN_CS_QG);
        String qgReturnList = Util.httpPostWithJSON(Constants.CAY_QGXM + url + Constants.TOKEN_CS_QG, jsonObject);

        JSONObject jSONObject = JSONObject.parseObject(qgReturnList);
        logger.info(jSONObject);
        JSONObject object = new JSONObject();
        if("SUCCESS".equals(jSONObject.get("msg"))){
            List<Map<String, Object>> dataJSONObject = (List<Map<String, Object>>) jSONObject.get("data");
            //暂时取第一个(一个项目id对应多个工程id)
            String xmid = (String) dataJSONObject.get(0).get("项目id");
            String jdbh = (String) dataJSONObject.get(0).get("项目监督编号");
            String gcid = (String) dataJSONObject.get(0).get("工程id");

            object.put("xmid", xmid);
            object.put("jdbh", jdbh);
            object.put("gcid", gcid);
            return object;
        }

        object.put("xmid", null);
        object.put("jdbh", null);
        object.put("gcid", null);
        return object;
    }

    /**
     * 上报城安院升降机基本信息(区管项目)
     */
    public static final String QGXMCAY(String url, JSONObject jsonObject) throws IOException, URISyntaxException {
        String returnArea = Util.httpPostWithJSON(Constants.CAY_QGXM + url + Constants.TOKEN_CS, jsonObject);
        logger.info("升降机基本信息(区管项目)返回数据"+returnArea);
        return returnArea;
    }

    /**
     * 上报城安院升降机基本信息(市管项目)
     */
    public static final String SGXMCAY(String url, JSONObject jsonObject) throws IOException, URISyntaxException {
        String s = Util.httpPostWithJSON(Constants.CAY_CS + url + Constants.TOKEN_CS, jsonObject);
        logger.info("升降机基本信息(市管项目)返回数据"+s);
        return s;
    }
}
