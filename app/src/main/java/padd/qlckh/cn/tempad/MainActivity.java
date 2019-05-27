package padd.qlckh.cn.tempad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.http.interceptor.Transformer;
import padd.qlckh.cn.tempad.http.observer.CommonObserver;
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener;
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener;
import padd.qlckh.cn.tempad.view.RateDao;

/**
 * @author Andy
 * @date 2018/9/13 13:21
 * Desc:
 */
public class MainActivity extends BaseActivity {

    private static final int WEIGHT_WHAT = 100000;
    private static final int SCAN_WHAT = 1010100;
    private static final int PRINT_WHAT = 19990;
    private static final int PANEL_WHAT = 3030;
    private static final int TIMER = 299993;
    private static final long TASK_DELAY_TIME = 2 * 1000L;

    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_total_jifen)
    TextView tvTotalJifen;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_total_weight)
    TextView tvTotalWeight;
    @BindView(R.id.iv_door4)
    ImageView ivDoor4;
    @BindView(R.id.iv_temp)
    TmepView ivTemp;
    @BindView(R.id.iv_door1)
    ImageView ivDoor1;
    @BindView(R.id.iv_door3)
    ImageView ivDoor3;
    @BindView(R.id.iv_door2)
    ImageView ivDoor2;
    @BindView(R.id.tv_door1)
    TextView tvDoor1;
    @BindView(R.id.tv_door3)
    TextView tvDoor3;
    @BindView(R.id.tv_door2)
    TextView tvDoor2;
    @BindView(R.id.tv_door4)
    TextView tvDoor4;
    @BindView(R.id.test)
    TextView test;
    @BindView(R.id.test2)
    TextView test2;
    private MyTimeTask task;
    private ResponeDao.ResponeBean responeBean;
    private static String order="";

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            int what = msg.what;
            switch (what) {

                case WEIGHT_WHAT:
                    handWeight((byte[]) msg.obj);
                    break;

                case PRINT_WHAT:
                    break;

                case PANEL_WHAT:
                    handPanl((byte[]) msg.obj);
                    break;
                //定时发送秤的数据
                case TIMER:
                    mWeightManager.sendBytes(ConvertUtils.hexString2Bytes(Constant.SEND_WEIGHT_THREE));
                    break;
                default:
            }
            return false;
        }
    });
    private String[] weights;
    private RateDao.RateBean rateBean;

    private void handWeight(byte[] obj) {
//        showDialog(Arrays.toString(obj)+"\n----------------------------\n"+new String(obj));
//        tvWeight.setText(ConvertUtils.bytes2HexString(obj));
//        tvTotalWeight.append(Arrays.toString(obj));

        String weight = ConvertUtils.bytes2HexString(obj);
        String hexWeight =weight.substring(2);
        if (!TextUtils.isEmpty(hexWeight) && hexWeight.startsWith("040C") && hexWeight.length() == 32) {
            weights = RateUtilThree.getWeight(hexWeight);
            String currentWeight = RateUtilThree.getCurrentWeight(weights, responeBean,order);
//            test.setText(hexWeight);
//            test2.setText(Arrays.toString(weights)+"------>order="+order);
            if (tvWeight!=null) {
                tvWeight.setText(MessageFormat.format("{0}kg", currentWeight));
                if (rateBean!=null) {
                    setJifen(currentWeight);
                }
                setTrash(weights);
            }
        }
    }

    private void setJifen(String currentWeight) {
        String format="";
        switch (order){
            case "01":
                double v = Double.parseDouble(currentWeight) * Double.parseDouble(rateBean.getFz());
                BigDecimal bigDecimal=new BigDecimal(v);
                double v1 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                format= new DecimalFormat("0.00").format(v1);
                break;
            case "02":
                double vv = Double.parseDouble(currentWeight) * Double.parseDouble(rateBean.getBl());
                BigDecimal bigDecimal1=new BigDecimal(vv);
                double vv1 = bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                format= new DecimalFormat("0.00").format(vv1);
                break;
            case "03":
                double vvv = Double.parseDouble(currentWeight) * Double.parseDouble(rateBean.getZl());
                BigDecimal bigDecimal11=new BigDecimal(vvv);
                double vvv1 = bigDecimal11.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                format= new DecimalFormat("0.00").format(vvv1);
                break;
            case "04":
                double vv2 = Double.parseDouble(currentWeight) * Double.parseDouble(rateBean.getYd());
                BigDecimal bigDecimal12=new BigDecimal(vv2);
                double vvvv1 = bigDecimal12.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                format= new DecimalFormat("0.00").format(vvvv1);
                break;
            default:
                break;
        }
        tvJifen.setText(format);
    }

    /**
     * 设置各个垃圾桶
     */
    private void setTrash(String[] weights) {
        String weight1 = weights[0];
        String weight2 = weights[1];
        String weight3 = weights[2];
        double v1 = Double.parseDouble(weight1) / Constant.MAX_WEIGHT;
        double v2 = Double.parseDouble(weight2) / Constant.MAX_WEIGHT;
        double v3 = Double.parseDouble(weight3) / Constant.MAX_WEIGHT;
        setTrashDrawable(v1, v2, v3,0.0);
        setTrashText(v1, v2, v3,0.0);


    }

    private void setTrashText(double v1, double v2, double v3, double v4) {
        double text1 = v1 * 100;
        double text2 = v2 * 100;
        double text3 = v3 * 100;
        double text4 = v4 * 100;
        if (text1 < 0) {
            tvDoor1.setText("0%");
        } else {
            tvDoor1.setText(MessageFormat.format("{0}%", new DecimalFormat("0.0").format(text1)));
        }

        if (text2 < 0) {
            tvDoor2.setText("0%");
        } else {
            tvDoor2.setText(MessageFormat.format("{0}%", new DecimalFormat("0.0").format(text2)));
        }

        if (text3 < 0) {
            tvDoor3.setText("0%");
        } else {
            tvDoor3.setText(MessageFormat.format("{0}%", new DecimalFormat("0.0").format(text3)));
        }

        if (text4 < 0) {
            tvDoor4.setText("0%");
        } else {
            tvDoor4.setText(MessageFormat.format("{0}%", new DecimalFormat("0.0").format(text4)));
        }
    }

    private void setTrashDrawable(double v1, double v2, double v3, double v4) {
        if (v1 <= 0.2) {
            ivDoor1.setImageResource(R.drawable.tong_ic11);
        } else if (v1 > 0.2 && v1 <= 0.4) {
            ivDoor1.setImageResource(R.drawable.tong_ic12);
        } else if (v1 > 0.4 && v1 <= 0.6) {
            ivDoor1.setImageResource(R.drawable.tong_ic13);
        } else if (v1 > 0.6 && v1 <= 0.8) {
            ivDoor1.setImageResource(R.drawable.tong_ic14);
        } else if (v1 > 0.8) {
            ivDoor1.setImageResource(R.drawable.tong_ic15);
        } else {
            ivDoor1.setImageResource(R.drawable.tong_ic11);
        }

        if (v2 <= 0.2) {
            ivDoor2.setImageResource(R.drawable.tong_ic21);
        } else if (v2 > 0.2 && v2 <= 0.4) {
            ivDoor2.setImageResource(R.drawable.tong_ic22);
        } else if (v2 > 0.4 && v2 <= 0.6) {
            ivDoor2.setImageResource(R.drawable.tong_ic23);
        } else if (v2 > 0.6 && v2 <= 0.8) {
            ivDoor2.setImageResource(R.drawable.tong_ic24);
        } else if (v2 > 0.8) {
            ivDoor2.setImageResource(R.drawable.tong_ic25);
        } else {
            ivDoor2.setImageResource(R.drawable.tong_ic21);
        }


        if (v3 <= 0.2) {
            ivDoor3.setImageResource(R.drawable.tong_ic31);
        } else if (v3 > 0.2 && v3 <= 0.4) {
            ivDoor3.setImageResource(R.drawable.tong_ic32);
        } else if (v3 > 0.4 && v3 <= 0.6) {
            ivDoor3.setImageResource(R.drawable.tong_ic33);
        } else if (v3 > 0.6 && v3 <= 0.8) {
            ivDoor3.setImageResource(R.drawable.tong_ic34);
        } else if (v3 > 0.8) {
            ivDoor3.setImageResource(R.drawable.tong_ic35);
        } else {
            ivDoor3.setImageResource(R.drawable.tong_ic31);
        }


        if (v4 <= 0.2) {
            ivDoor4.setImageResource(R.drawable.tong_ic41);
        } else if (v4 > 0.2 && v4 <= 0.4) {
            ivDoor4.setImageResource(R.drawable.tong_ic42);
        } else if (v4 > 0.4 && v4 <= 0.6) {
            ivDoor4.setImageResource(R.drawable.tong_ic43);
        } else if (v4 > 0.6 && v4 <= 0.8) {
            ivDoor4.setImageResource(R.drawable.tong_ic44);
        } else if (v4 > 0.8) {
            ivDoor4.setImageResource(R.drawable.tong_ic45);
        } else {
            ivDoor4.setImageResource(R.drawable.tong_ic41);
        }
    }

    private TimeThread timeThread;
    private WeightThread weightThread;
    StringBuffer buffer = new StringBuffer();

    /**
     * 处理面板
     *
     * @param bys
     */
    private void handPanl(byte[] bys) {

//        showDialog(ConvertUtils.bytes2HexString(bys));

        buffer.append(ConvertUtils.bytes2HexString(bys));
        if (buffer.length() == 12) {
            String cover = buffer.toString();

           if (cover.startsWith("00")){
                order="01";
            }else if (cover.startsWith("01")){
                order="02";
            }else if (cover.startsWith("02")){
                order="03";
            }else if (cover.startsWith("03")){
                order="04";
            }


            if (cover.startsWith("8001")) {
                String hexLine1 = null;
                String hexLine2 = null;
                String hexLine3 = null;
                String hexLine4 = null;
                String hexLine5 = null;
                String hexLine6 = null;
                try {
                    hexLine1 = ConvertUtils.bytes2HexString(("社区: 三都王村").getBytes(ConvertUtils.GB2312));
                    hexLine2 = ConvertUtils.bytes2HexString(("用户名: " + tvName.getText().toString()).getBytes(ConvertUtils.GB2312));
                    hexLine3 = ConvertUtils.bytes2HexString(("积分: 当前积分  " + tvJifen.getText().toString() + "  总积分  " + tvTotalJifen.getText().toString()).getBytes(ConvertUtils.GB2312));
                    hexLine4 = ConvertUtils.bytes2HexString(("重量: 当前投放重量  " + tvWeight.getText().toString() + "  总重量  " + tvTotalWeight.getText().toString()).getBytes(ConvertUtils.GB2312));
                    hexLine5 = ConvertUtils.bytes2HexString(("时间: " + tvDate.getText().toString()).getBytes(ConvertUtils.GB2312));
                    hexLine6 = ConvertUtils.bytes2HexString(("公司: 浙江春绿环保科技有限公司").getBytes(ConvertUtils.GB2312));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                String printHexSrc = "1b40" + hexLine1 + "0d0a" +
                        hexLine2 + "0d0a" +
                        hexLine3 + "0d0a" +
                        hexLine4 + "0d0a" +
                        hexLine5 + "0d0a" +
                        hexLine6 + "0d0a0d0a0d0a0d0a0d0a0d0a1b69";
                mPrintManager.sendBytes(ConvertUtils.hexString2Bytes(printHexSrc));

            }
            else if (cover.startsWith("0000") || cover.startsWith("0100") || cover.startsWith("0200") || cover.startsWith("0300")) {
                if (tvWeight!=null&&weights!=null) {
                    savaData();
                }

            }
            buffer.delete(0, 12);

        }
    }

    /**
     * 提交数据到服务器
     */
    private void savaData() {
        HashMap<String, String> map = new HashMap<>();

        map.put("dw", tvWeight.getText().toString().replace("kg", ""));
        map.put("zwight", Double.parseDouble(weights[2]) < 0.0 ? "0.0" : weights[2]);
        map.put("bwight", Double.parseDouble(weights[1]) < 0.0 ? "0.0" : weights[1]);
        map.put("fwight", Double.parseDouble(weights[0]) < 0.0 ? "0.0" : weights[0]);
        map.put("wendu", "20");
        map.put("id", responeBean.getIid());
        map.put("user", responeBean.getUser());
        map.put("address", responeBean.getAddress());
        map.put("wyid", "0105");
        map.put("jifen", tvJifen.getText().toString());
        RxHttpUtils.createApi(ApiService.class)
                .push(map)
                .compose(Transformer.<RequestDao>switchSchedulers())
                .subscribe(new CommonObserver<RequestDao>() {
                    @Override
                    protected void onError(String errorMsg) {

                        showLong(errorMsg);
                    }

                    @Override
                    protected void onSuccess(RequestDao requestDao) {
                        showLong("数据已提交");
                        task.stop();
                        delayFinsh();
                    }
                });
    }

    private void delayFinsh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 6 * 1000);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        //Integer.parseInt("oop");
        long l = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format("yyyy年MM月dd日 HH:mm", l);
        tvDate.setText(sysTimeStr);
        setTimer();
        setSerialListener();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);

    }

    private void setTimer() {
        task = new MyTimeTask(TASK_DELAY_TIME, new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(TIMER);
            }
        });
        task.start();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                long l = System.currentTimeMillis();
                CharSequence sysTimeStr = DateFormat.format("yyyy年MM月dd日 HH:mm", l);
                tvDate.setText(sysTimeStr);
            }
        }
    };

    private void setSerialListener() {
        //发送再主线程 接收子线程
        setPanelListener();
        setPrintLisener();
        setWeightListener();


    }

    private void setPanelListener() {
        mPanelManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {

            }

            @Override
            public void onFail(File device, Status status) {

            }
        });

        mPanelManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {
                Message message = new Message();
                message.what = PANEL_WHAT;
                message.obj = bytes;
                mHandler.sendMessage(message);
            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });


    }

    private void setPrintLisener() {

        mPrintManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {


            }

            @Override
            public void onFail(File device, Status status) {
                showLong("打印机串口打开失败");
            }
        });
        mPrintManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {

                reciveData(bytes, PRINT_WHAT);
            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });
    }

    private void setWeightListener() {
        mWeightManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {

            }

            @Override
            public void onFail(File device, Status status) {

                showLong("称重串口打开失败");
            }
        });

        mWeightManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {

                reciveData(bytes, WEIGHT_WHAT);
            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });
    }

    private void reciveData(byte[] bytes, int what) {
        Message message = new Message();
        message.what = what;
        message.obj = bytes;
        mHandler.sendMessage(message);
    }

    @Override
    public void initDate() {
        timeThread = new TimeThread(tvDate);
//        timeThread.start();
        responeBean = getIntent().getParcelableExtra("home");
        if (responeBean == null) {
            return;
        }
        tvName.setText(responeBean.getUser());
        tvAddress.setText(responeBean.getAddress());
        tvTotalJifen.setText(responeBean.getJifen());
        tvJifen.setText(responeBean.getDjifen());
        tvTotalWeight.setText(MessageFormat.format("{0}kg", responeBean.getWight()));
//       weightThread = new WeightThread(tvWeight, tvTotalWeight, weight);
//       weightThread.start();
        queryRate();


    }

    private void queryRate() {

        RxHttpUtils.createApi(ApiService.class)
                .queryRate("0105")
                .compose(Transformer.<RateDao>switchSchedulers())
                .subscribe(new CommonObserver<RateDao>() {
                    @Override
                    protected void onError(String errorMsg) {
                        showLong(errorMsg);
                    }

                    @Override
                    protected void onSuccess(RateDao rateDao) {

                        rateBean = rateDao.getRow();
                    }
                });
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void release() {
        if (timeThread != null) {
            if (!timeThread.isInterrupted()) {
                timeThread.interrupt();
            }
            timeThread = null;
        }

        if (weightThread != null) {
            weightThread.interrupt();
            weightThread = null;
        }
        if (task != null) {
            task.stop();
            task = null;
        }
        unregisterReceiver(receiver);
    }

    @OnClick({R.id.tv_exit, R.id.iv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exit:
                startActivity(new Intent(this, TestActivity.class));
                break;
            case R.id.iv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
        }
    }

}
