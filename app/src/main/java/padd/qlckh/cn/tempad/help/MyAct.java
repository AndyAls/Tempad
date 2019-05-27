package padd.qlckh.cn.tempad.help;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import padd.qlckh.cn.tempad.BaseActivity;
import padd.qlckh.cn.tempad.Constant;
import padd.qlckh.cn.tempad.ConvertUtils;
import padd.qlckh.cn.tempad.R;

/**
 * @author Andy
 * @date 2018/10/10 14:19
 * Desc:
 */
public class MyAct extends BaseActivity {
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.bt2)
    Button bt2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.bt3)
    Button bt3;
    @BindView(R.id.et4)
    EditText et4;
    @BindView(R.id.bt4)
    Button bt4;
    @BindView(R.id.zhong1)
    TextView zhong1;
    @BindView(R.id.zhong2)
    TextView zhong2;
    @BindView(R.id.zhong3)
    TextView zhong3;
    private SerialControl control;

    @Override
    protected int getContentView() {
        return R.layout.act_my;
    }

    @Override
    public void initView() {

        control = new SerialControl();

        SharedPreferences sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        String weightNode = sp.getString(Constant.WEGHT_NODE, "");
        int weightRate = Integer.decode(sp.getString(Constant.WEGHT_RATE, "-1"));
        control.setBaudRate(weightRate);
        control.setPort(weightNode);
        OpenComPort(control);
    }

    @Override
    public void initDate() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void release() {

    }
    //----------------------------------------------------串口发送
    private void sendPortData(SerialHelper ComPort,String sOut){
        if (ComPort!=null && ComPort.isOpen()) {
            ComPort.sendHex(sOut);
        }
    }
    //----------------------------------------------------打开串口
    private void OpenComPort(SerialHelper ComPort){
        try
        {
            ComPort.open();
        } catch (SecurityException e) {
            showLong("打开串口失败:没有串口读/写权限!");
        } catch (IOException e) {
            showLong("打开串口失败:未知错误!");
        } catch (InvalidParameterException e) {
            showLong("打开串口失败:参数错误!");
        }
    }
    //----------------------------------------------------串口控制类
    private class SerialControl extends SerialHelper{

        //		public SerialControl(String sPort, String sBaudRate){
//			super(sPort, sBaudRate);
//		}
        public SerialControl(){
        }

        @Override
        protected void onDataReceived(final byte[] ComRecData)
        {
            //数据接收量大或接收时弹出软键盘，界面会卡顿,可能和6410的显示性能有关
            //直接刷新显示，接收数据量大时，卡顿明显，但接收与显示同步。
            //用线程定时刷新显示可以获得较流畅的显示效果，但是接收数据速度快于显示速度时，显示会滞后。
            //最终效果差不多-_-，线程定时刷新稍好一些。
//			DispQueue.AddQueue(ComRecData);//线程定时刷新显示(推荐)

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    et3.append(Arrays.toString(ComRecData));
                    et4.append(ConvertUtils.bytes2HexString(ComRecData));
                    zhong1.append(Arrays.toString(ComRecData));
                    zhong2.setText(Arrays.toString(ComRecData));
                }
            });

			/*
			runOnUiThread(new Runnable()//直接刷新显示
			{
				public void run()
				{
					DispRecData(ComRecData);
				}
			});*/
        }
    }
    @OnClick(R.id.bt2)
    public void onViewClicked() {
        sendPortData(control,et2.getText().toString());
    }
}
