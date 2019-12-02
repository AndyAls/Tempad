package padd.qlckh.cn.tempad;


import java.util.Map;

import io.reactivex.Observable;
import padd.qlckh.cn.tempad.view.RateDao;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Andy
 * @date 2018/9/17 17:01
 * Desc:
 */
public interface ApiService {


//    String BASE_URL="http://chunlv.hanziyi.cn/";
    String BASE_URL="http://qidian.365igc.cn/api/";
    long DEFAULT_TIME=60;

    /**
     * 投放
     */
    @FormUrlEncoded
    @POST("index.php/api/suggest/zhong")
    Observable<RequestDao> push(@FieldMap Map<String,String>parms);

    @GET("index.php/api/suggest/suoyou")
    Observable<ResponeDao> query(@Query("id")String userId, @Query("wyid")String wyid);

    @GET("index.php/api/suggest/xyg")
    Observable<RateDao> queryRate(@Query("code")String userId);


    @GET("jqi/index")
    Observable<ScanDao> scanResult(@Query("id")String id,@Query("ids") String ids,@Query("code") String code);


    @FormUrlEncoded
    @POST("jqi/fan")
    Observable<Object> bindUser(@Field("id") String id, @Field("status") int status,  @Field("code") String code, @Field("h_code") String h_code);

    @FormUrlEncoded
    @POST("jqi/bding")
    Observable<Object> bindCode(@Field("id") String id, @Field("item") String item);


}
