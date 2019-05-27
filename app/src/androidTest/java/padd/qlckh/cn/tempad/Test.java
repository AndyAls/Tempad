package padd.qlckh.cn.tempad;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import padd.qlckh.cn.tempad.http.utils.MD5;

/**
 * @author Andy
 * @date 2018/12/19 10:34
 * Desc:
 */
@RunWith(AndroidJUnit4.class)
public class Test {

   private Map<String,Object> map=new HashMap<String,Object>();
    @Before
    public void init(){

        map.put("size","[]");
        map.put("title","");
        map.put("typeId","0");
        map.put("catId","0");
        map.put("unionId","0");
        map.put("sortType","0");
        map.put("page","0");
        map.put("limit","20");
//        map.put("productId","9670");
//        map.put("isChest","1");
        map.put("uuid","309c23acc4953851");
        map.put("platform","android");
        map.put("v","3.5.1");
        map.put("loginToken","51cf4799|30509751|440810d5560dabcb");
    }

    @org.junit.Test
    protected void test(){
        ArrayList<Map.Entry<String, Object>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuilder localStringBuilder1 = new StringBuilder();
        int i = 0;
        while (i < entries.size())
        {
            Map.Entry localEntry = (Map.Entry)entries.get(i);
            Object localObject = "aq";
            StringBuilder localStringBuilder2 = new StringBuilder();
            localStringBuilder2.append((String)localEntry.getKey());
            localStringBuilder2.append(" : ");
            localStringBuilder2.append((String)localEntry.getValue());
            localStringBuilder2.append("\n");
            Log.e((String)localObject, localStringBuilder2.toString());
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append((String)localEntry.getKey());
            ((StringBuilder)localObject).append((String)localEntry.getValue());
            localStringBuilder1.append(((StringBuilder)localObject).toString());
            i += 1;
        }
        localStringBuilder1.append("3542e676b4c80983f6131cdfe577ac9b");

        XLog.e("----",MD5.EncoderByMd5(localStringBuilder1.toString()));
        XLog.e("----",localStringBuilder1.toString());
        XLog.e("----",ae.a(localStringBuilder1.toString()));

        Assert.assertNull(localStringBuilder1.toString());
    }
}
