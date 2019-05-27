package padd.qlckh.cn.tempad;


import java.util.Map;

import io.reactivex.Observable;
import padd.qlckh.cn.tempad.view.RateDao;
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


    String BASE_URL="http://chunlv.hanziyi.cn/";
    long DEFAULT_TIME=20;

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

}
