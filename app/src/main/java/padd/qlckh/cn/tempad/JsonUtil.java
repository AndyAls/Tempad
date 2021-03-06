package padd.qlckh.cn.tempad;

import com.alibaba.fastjson.JSON;
import org.json.JSONException;

import java.util.List;

/**
 * @author Andy
 * @date   2018/5/15 15:25
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    JsonUtil.java
 */

public class JsonUtil {

    public static <T> List<T> json2List2(String jsonData, Class<T> clazz) {
        return JSON.parseArray(jsonData, clazz);
    }

    public static <T> T json2Object2(String json, Class<T> clazz){
        return JSON.parseObject(json,  clazz);
    }

    public static <T> String object2Json(T t){
        return JSON.toJSONString(t);
    }

    public static String getString(String tag, String json){
        try {

            org.json.JSONObject jsonObject = new org.json.JSONObject(json);
            if (jsonObject.has(tag)){
                return jsonObject.getString(tag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
