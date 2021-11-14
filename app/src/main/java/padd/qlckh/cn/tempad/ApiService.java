package padd.qlckh.cn.tempad;


import java.util.Map;

import io.reactivex.Observable;
import padd.qlckh.cn.tempad.view.RateDao;
import padd.qlckh.cn.tempad.yipingfang.AddCodeResq;
import padd.qlckh.cn.tempad.yipingfang.YiJifen;
import padd.qlckh.cn.tempad.yipingfang.YiMarkDetailBean;
import padd.qlckh.cn.tempad.yipingfang.YiPutBean;
import padd.qlckh.cn.tempad.yipingfang.YiScanInfo;
import padd.qlckh.cn.tempad.yipingfang.YiUserInfo;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Andy
 * @date   2021/11/4 16:42
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    ApiService.java
 */
public interface ApiService {

    String BASE_URL = "http://www.lsypf.cn/api/";
    long DEFAULT_TIME = 39;
    String IMG_URL = "http://www.lsypf.cn/Public/Public/";

    @FormUrlEncoded
    @POST("suggest/huanjing")
    Observable<Object> postData(@Field("username") String str, @Field("usercode") String str2, @Field("ji_duinum") String str3, @Field("address") String str4, @Field("tel") String str5, @Field("code") String str6, @Field("zong") String str7, @Field("status") String str8, @Field("sid") String str9);


    @GET("cmp/lj_fen")
    Observable<YiJifen> queryJifen(@Query("id") String str);

    @GET("cmp/caiji")
    Observable<YiScanInfo> scanCode(@Query("code") String str);

    @GET("cmp/tf")
    Observable<YiMarkDetailBean> queryMarkDetail(@Query("id") String str, @Query("status") String str2);


    @FormUrlEncoded
    @POST("order/tj_order")
    Observable<Object> reservationRow(@Field("username") String str, @Field("phone") String str2, @Field("userid") String str3, @Field("prdid") String str4, @Field("title") String str5, @Field("dizhi") String str6);

    @FormUrlEncoded
    @POST("App/hsou")
    Observable<YiPutBean> queryPut(@Field("card") String str);

    @GET("cmp/card")
    Observable<YiUserInfo> queryYiInfo(@Query("card") String str);
    @GET("cmp/code")
    Observable<AddCodeResq> addCode(@Query("code") String str);

    @GET("cmp/clerts")
    Observable<Object> clearCode(@Query("code") String str);

    /**
     * 投放
     */
    @FormUrlEncoded
    @POST("index.php/api/suggest/zhong")
    Observable<RequestDao> push(@FieldMap Map<String, String> parms);

    @GET("index.php/api/suggest/suoyou")
    Observable<ResponeDao> query(@Query("id") String userId, @Query("wyid") String wyid);

    @GET("index.php/api/suggest/xyg")
    Observable<RateDao> queryRate(@Query("code") String userId);


    @GET("jqi/index")
    Observable<ScanDao> scanResult(@Query("id") String id, @Query("ids") String ids, @Query("code") String code,
                                   @Query("id_qren") String id_qren, @Query("flag") int flag,
                                   @Query("h_code_id") String h_code_id, @Query("addtime") String addtime);


    @FormUrlEncoded
    @POST("jqi/fan")
    Observable<Object> bindUser(@Field("id") String id, @Field("status") int status, @Field("code") String code, @Field("h_code") String h_code,
                                @Field("h_code_id") String h_code_id);

    @FormUrlEncoded
    @POST("jqi/bding")
    Observable<Object> bindCode(@Field("id") String id, @Field("item") String item);


}
