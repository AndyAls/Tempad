package padd.qlckh.cn.tempad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.http.interceptor.Transformer;
import padd.qlckh.cn.tempad.http.observer.CommonObserver;
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener;
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener;

/**
 * @author Andy
 * @date 2018/9/17 11:58
 * Desc:
 */
public class ScreenActivity extends BaseActivity {
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    @BindView(R.id.tv_hint1)
    TextView tvHint1;
    private String weightNode;
    private int weightRate;
    private String panelNode;
    private int panelRate;
    private String scanNode;
    private int scanRate;
    private String printNode;
    private int printRate;
    private static final int SCAN_WHAT = 1010100;
    private static final int PANEL_WHAT = 39999;
    private ResponeDao.ResponeBean responeBean;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            int what = msg.what;
            switch (what) {

                case SCAN_WHAT:
                    handScan((byte[]) msg.obj);
                    break;

                case PANEL_WHAT:
                    handPanl((byte[]) msg.obj);
                    break;
                default:
            }
            return false;
        }
    });


    StringBuffer buffer = new StringBuffer();

    private void handPanl(byte[] bys) {

        buffer.append(ConvertUtils.bytes2HexString(bys));
        if (buffer.length() == 12) {
            String cover = buffer.toString();
            tvHint1.setText(cover);
            if (cover.startsWith("0001")) {
                tvHint.setText("01门打开成功...");

            } else if (cover.startsWith("0000")) {
                tvHint.setText("01门已关闭...");

            } else if (cover.startsWith("0101")) {
                tvHint.setText("02门打开成功...");

            } else if (cover.startsWith("0100")) {
                tvHint.setText("02门已关闭...");

            } else if (cover.startsWith("0201")) {
                tvHint.setText("03门打开成功...");

            } else if (cover.startsWith("0200")) {
                tvHint.setText("03门已关闭...");

            } else if (cover.startsWith("0301")) {
                tvHint.setText("04门打开成功...");

            } else if (cover.startsWith("0300")) {
                tvHint.setText("04门已关闭...");

            } else if (cover.startsWith("8001")) {
                tvHint.setText("打印机连接成功..");
            } else {
                tvHint.setText("门连接成功...");

            }

            buffer.delete(0, 12);
        }

    }


    StringBuffer b = new StringBuffer();

    private void handScan(byte[] obj) {

        b.append(new String(obj));
        if (b.length() == 10) {
            queryData(b.toString());
            b.delete(0, 10);
        }
//        String json = new String(obj);
//        showDialog(json);
//        if (json.contains("\"c\",")) {
//            json = json.replaceAll("\"c\",", "");
//        }
//        HomeDao homeDao = null;
//        try {
//            homeDao = JsonUtil.json2Object2(json, HomeDao.class);
//        } catch (Exception e) {
//            showLong("用户信息解析错误");
//            return;
//        }
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("home", homeDao);
//        startActivity(intent);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_screen;
    }

    @Override
    public void initView() {
        initNoteAndRote();
        setScanListerer();
        setPanelListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void queryData(String id) {

        loading();
        RxHttpUtils.createApi(ApiService.class)
                .query(id, "0105")
                .compose(Transformer.<ResponeDao>switchSchedulers())
                .subscribe(new CommonObserver<ResponeDao>() {
                    @Override
                    protected void onError(String errorMsg) {
                        cancelLoading();
                        showLong(errorMsg);
                    }

                    @Override
                    protected void onSuccess(ResponeDao responeDao) {
                        cancelLoading();
                        if (responeDao.getRow() != null) {
                            responeBean = responeDao.getRow();

                            Intent intent = new Intent(ScreenActivity.this, MainActivity.class);
                            intent.putExtra("home", responeBean);
                            startActivity(intent);
                        }
                    }
                });

    }


    private void setPanelListener() {
        mPanelManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {
                tvHint.setText("开关门串口连接成功..");
            }

            @Override
            public void onFail(File device, Status status) {
                tvHint.setText("开关门串口连接失败..");
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

    private void setScanListerer() {
        mScanManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {
                tvHint.setText("扫码枪串口打开成功..");
            }

            @Override
            public void onFail(File device, Status status) {
                tvHint.setText("扫码枪串口打开失败..");
            }
        });
        mScanManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {
                Message message = new Message();
                message.what = SCAN_WHAT;
                message.obj = bytes;
                mHandler.sendMessage(message);
            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });
    }

    private void initNoteAndRote() {
        SharedPreferences sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        weightNode = sp.getString(Constant.WEGHT_NODE, "");
        weightRate = Integer.decode(sp.getString(Constant.WEGHT_RATE, "-1"));

        SharedPreferences psp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        panelNode = psp.getString(Constant.PANEL_NODE, "");
        panelRate = Integer.decode(psp.getString(Constant.PRINT_RATE, "-1"));

        SharedPreferences ssp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        scanNode = ssp.getString(Constant.SCAN_NODE, "");
        scanRate = Integer.decode(ssp.getString(Constant.SCAN_RATE, "-1"));

        SharedPreferences prsp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        printNode = prsp.getString(Constant.PRINT_NODE, "");
        printRate = Integer.decode(prsp.getString(Constant.PRINT_RATE, "-1"));
        checkNodeAndRate();
    }


    /**
     * 检查设备节点和波特率 没设置 跳转到设置界面
     */
    private void checkNodeAndRate() {

        if (weightNode.length() == 0 || weightRate == -1
                || panelNode.length() == 0 || panelRate == -1
                || scanNode.length() == 0 || scanRate == -1
                || printNode.length() == 0 || printRate == -1) {

            Toast.makeText(this, "请设置全部的设备节点和波特率", Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, SettingActivity.class));
        }
    }

    @Override
    public void initDate() {

//        test();
    }

    private void test() {

        String hexLine1 = null;
        String hexLine2 = null;
        String hexLine3 = null;
        String hexLine4 = null;
        String hexLine5 = null;
        String hexLine6 = null;
        try {
            hexLine1 = ConvertUtils.bytes2HexString("社区: 梧桐湖社区".getBytes(ConvertUtils.GB2312));
            hexLine2 = ConvertUtils.bytes2HexString("用户名: 李兵站".getBytes(ConvertUtils.GB2312));
            hexLine3 = ConvertUtils.bytes2HexString(("积分: 当前积分  0" + "  总积分  20").getBytes(ConvertUtils.GB2312));
            hexLine4 = ConvertUtils.bytes2HexString(("重量: 当前投放重量  56g" + "  总重量  80g").getBytes(ConvertUtils.GB2312));
            hexLine5 = ConvertUtils.bytes2HexString("时间: 2018年9月18日".getBytes(ConvertUtils.GB2312));
            hexLine6 = ConvertUtils.bytes2HexString("公司: 浙江春绿环保科技有限公司".getBytes(ConvertUtils.GB2312));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String printHexSrc = "1b40" + hexLine1 + "0d0a" +
                hexLine2 + "0d0a" +
                hexLine3 + "0d0a" +
                hexLine4 + "0d0a" +
                hexLine5 + "0d0a" +
                hexLine6 + "0d0a0d0a0d0a1b69";
        byte[] bytes = ConvertUtils.hexString2Bytes(printHexSrc);
        XLog.e("---", printHexSrc, bytes.toString());
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void release() {
        mApplication.closeAllSerialPort();
        mWeightManager = null;
        mScanManager = null;
        mPanelManager = null;
        mPrintManager = null;
    }


    @OnClick({R.id.tv_exit, R.id.iv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_exit:
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case R.id.iv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
        }
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (System.currentTimeMillis() - exitTime > 2000) {
            showShort("再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }

        return true;

    }

}
