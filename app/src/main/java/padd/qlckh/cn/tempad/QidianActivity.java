package padd.qlckh.cn.tempad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.http.interceptor.Transformer;
import padd.qlckh.cn.tempad.http.observer.CommonObserver;
import padd.qlckh.cn.tempad.manager.OnOpenSerialPortListener;
import padd.qlckh.cn.tempad.manager.OnSerialPortDataListener;

/**
 * @author Andy
 * @date 2019/5/30 9:14
 * Desc:
 */
public class QidianActivity extends BaseActivity {
    private static final long TASK_DELAY_TIME = 2500L;
    private static final int TIMER = 299993;

    private static final int SCAN_WHAT = 1010100;
    private static final int PANEL_WHAT = 39999;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_statu)
    TextView tvStatu;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;
    @BindView(R.id.tv6)
    TextView tv6;
    @BindView(R.id.tv7)
    TextView tv7;
    private String panelNode;
    private int panelRate;
    private String scanNode;
    private int scanRate;
    private MyTimeTask task;
    private boolean canScan = true;
    private int scanCount=0;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            int what = msg.what;
            switch (what) {
                //定时发送秤的数据
                case TIMER:
                    mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(Constant.QIAN_READ_STATUS));
                    break;

                case SCAN_WHAT:
                    if (canScan) {
                        handScan((byte[]) msg.obj);
                    } else {
                        showLong("正在出货,请稍后刷卡重试...");
                        scanCount++;
                        if (scanCount==2){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    restart();
                                }
                            }, 10000);
                        }
                    }
                    break;

                case PANEL_WHAT:
                    handPanl((byte[]) msg.obj);
                    break;
                default:
            }
            return false;
        }
    });
    private ScanDao.DataBean scanDao;

    int j = 0;
    StringBuffer bufferPanl = new StringBuffer();

    private void handPanl(byte[] obj) {
        j += 1;
        String result1 = ConvertUtils.bytes2HexString(obj);
        bufferPanl.append(result1);

        if (j == 2) {
            String result = bufferPanl.toString();
            //清除结果
            if (result.toUpperCase().substring(4, 6).equals("A3")) {
                //清除之后要发送电机转动
                String h_code = scanDao.getH_code();
                if (isEmpty(h_code)) {
                    j = 0;
                    canScan = true;
                    bufferPanl.delete(0, bufferPanl.toString().length());
                    tvStatu.setText("垃圾袋已领完，请等待上货-");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvStatu.setText("欢迎领取垃圾袋!!");
                        }
                    }, 1000);
                    return;
                }
                String replace = Constant.QIAN_TURN.replace("****", h_code);
                final String turn = CommUtils.addCheckNum(replace);

                tv6.setText("出货-->" + turn + "\n--->" + h_code);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(turn));
                        tvStatu.setText("正在出货...");
                    }
                }, 1000);

            }
            //发动机转一圈 后 要定时检查机器状态
            if (result.toUpperCase().substring(4, 6).equals("A2")) {
                if (task == null) {
                    task = new MyTimeTask(TASK_DELAY_TIME, new TimerTask() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(TIMER);
                        }
                    });
                }
                task.start();
            }
            //读取机器状态
            if (result.toUpperCase().substring(4, 6).equals("A1")) {
                //读取出货结果: 20 00 A1 03 00 00 1A 98
                //0: 空闲等待
                //1: 正在出货
                //2: 出货成功
                //3: 出货失败
                String status = result.substring(10, 12);
                switch (status) {
                    case "00":
                        tvStatu.setText("空闲等待");
                        break;
                    case "01":
                        tvStatu.setText("正在出货...");
                        break;
                    //出货成功  提交后台 停止task
                    case "02":
                        task.stop();
                        task = null;
                        tvStatu.setText("出货成功!");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                postData(1);
                            }
                        }, 1000);

                        break;
                    case "03":
                        task.stop();
                        task = null;
                        tvStatu.setText("出货失败");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                postData(0);
                            }
                        }, 3000);
                        break;
                    default:
                }

//                0: 正常。
//                1:电机未接入或霍尔板有问题。
//                2:电机运行超时。
//                3:电机编号错误
//                4:红外线通信失败。
//                5:红外线被阻挡。
                String machine = result.substring(8, 10);
                switch (machine) {
                    case "00":
                        tv7.setText("机器正常");
                        break;
                    case "01":
                        tv7.setText("电机未接入或霍尔板有问题。");
                        break;
                    case "02":
                        tv7.setText("电机运行超时。");
                        break;
                    case "03":
                        tv7.setText("电机编号错误");
                        break;
                    case "04":
                        tv7.setText("红外线通信失败。");
                        break;
                    case "05":
                        tv7.setText("红外线被阻挡。");
                        break;
                    default:
                }
            }

            j = 0;
            bufferPanl.delete(0, bufferPanl.toString().length());
        }


    }

    private void postData(int status) {
        loading();
        tv3.setText("提交传参" + "id=" + scanDao.getId() + "status=" + status + "code=" + CommUtils.getIMEI(this) + "h_code=" + scanDao.getH_code());
        RxHttpUtils.createApi(ApiService.class)
                .bindUser(scanDao.getId(), status, CommUtils.getIMEI(this), scanDao.getH_code())
                .compose(Transformer.<Object>switchSchedulers())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    protected void onError(String errorMsg) {
                        if (j != 0) {
                            j = 0;
                        }
                        if (bufferPanl.toString().length() > 0) {
                            bufferPanl.delete(0, bufferPanl.toString().length());
                        }
                        canScan = true;
                        cancelLoading();
                        showLong(errorMsg);
                        tvName.setText("");
                        tvStatu.setText("欢迎领取垃圾袋!!");
                        restart();
                    }

                    @Override
                    protected void onSuccess(Object responeDao) {
                        cancelLoading();
                        if (j != 0) {
                            j = 0;
                        }
                        if (bufferPanl.toString().length() > 0) {
                            bufferPanl.delete(0, bufferPanl.toString().length());
                        }
                        tvName.setText("");
                        tv4.setText(JsonUtil.object2Json(responeDao));
                        tvStatu.setText("欢迎领取垃圾袋!!");
                        canScan = true;
                        restart();
                    }
                });


    }

    private void restart() {
        Intent intent = new Intent(QidianActivity.this, QidianActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }


    int i = 0;
    StringBuffer buffer = new StringBuffer();
    StringBuffer buffer1 = new StringBuffer();

    private void handScan(byte[] obj) {
        i += 1;
        String result = new String(obj);
//        tvName.append(result+i+ConvertUtils.bytes2HexString(obj));
        buffer.append(result);
        buffer1.append(ConvertUtils.bytes2HexString(obj));
        tv5.setText(buffer.toString() + "====" + i + "-->\n");
        if (i == 2) {
            canScan = false;
            if (JsonUtil.isJsonValid(buffer.toString())) {
                String id = JsonUtil.getString("id", buffer.toString());
                scanResult("", id);
            } else {
//                scanResult(buffer.toString(),"");

                //下面处理的是反码问题,正码直接用上面的就行
                try {
                    String hex = Long.toHexString(Long.parseLong(buffer.toString().replace("\n", "")));
                    String[] group = RateUtil.group2(hex);
                    String s = String.valueOf(Long.parseLong(group[3] + group[2] + group[1] + group[0], 16));
                    if (s.length() == 9) {
                        s = "0" + s;
                    } else if (s.length() < 9) {
                        int i = 10 - s.length();
                        StringBuilder sb = new StringBuilder();
                        for (int k = 0; k < i; k++) {
                            sb.append("0");
                        }
                        s=sb.toString()+s;
                    }
                    scanResult(s, "");

                } catch (Exception e) {
                    e.printStackTrace();
                    tvStatu.setText("反码出现异常,请联系客服解决");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (i != 0) {
                                i = 0;
                            }
                            if (buffer.toString().length() > 0) {
                                buffer.delete(0, buffer.toString().length());
                            }
                            tvStatu.setText("欢迎领取垃圾袋!!");
                            canScan = true;
                        }
                    }, 2000);

                }
            }
//            tvStatu.setText(buffer.toString()+"======"+JsonUtil.getString("id", buffer.toString()));

            i = 0;
            buffer.delete(0, buffer.toString().length());
            buffer1.delete(0, buffer1.toString().length());
        }
    }

    private void scanResult(final String id, String ids) {
        tv1.setText("扫描传参" + "id=" + id + "----ids=" + ids + "------getIMEI=" + CommUtils.getIMEI(this));
        loading();
        RxHttpUtils.createApi(ApiService.class)
                .scanResult(id, ids, CommUtils.getIMEI(this))
//                .scanResult("4017515658", ids, "867012039777450")
                .compose(Transformer.<ScanDao>switchSchedulers())
                .subscribe(new CommonObserver<ScanDao>() {
                    @Override
                    protected void onError(String errorMsg) {
                        cancelLoading();
                        if (i != 0) {
                            i = 0;
                        }
                        if (buffer.toString().length() > 0) {
                            buffer.delete(0, buffer.toString().length());
                        }
                        canScan = true;
                        showLong(errorMsg);
                        tvName.setText("");
                        tvStatu.setText(errorMsg);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvStatu.setText("欢迎领取垃圾袋!!");
                            }
                        }, 1000);
                    }

                    @Override
                    protected void onSuccess(ScanDao responeDao) {
                        cancelLoading();
                        if (i != 0) {
                            i = 0;
                        }
                        if (buffer.toString().length() > 0) {
                            buffer.delete(0, buffer.toString().length());
                        }
                        tv2.setText("扫描获取的数据=" + JsonUtil.object2Json(responeDao) + "\n" + JsonUtil.object2Json(responeDao.getData()));
                        scanDao = responeDao.getData();
                        if ("1".equals(responeDao.getStatus())) {
                            tvName.setText(scanDao.getFullname());
//                        tvStatu.setText(JsonUtil.object2Json(responeDao));
                            if ("0".equals(scanDao.getStatus())) {
                                mPanelManager.sendBytes(ConvertUtils.hexString2Bytes(Constant.QIAN_CLEAR));
                            }
                            //用户没关联上去
                            else if ("3".equals(scanDao.getStatus())) {
                                /*Intent intent = new Intent(QidianActivity.this, InfoErrorActivity.class);
                                intent.putExtra("item", id);
                                startActivity(intent);
                                overridePendingTransition(0,0);
                                finish();*/
                                showLong("联系物业,激活卡片");

                                canScan = true;
                            } else {
                                canScan = true;
                                tvName.setText("");
                                tvStatu.setText("对不起,您本月已经领取了");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvStatu.setText("欢迎领取垃圾袋!!");
                                    }
                                }, 1000);
                            }
                        } else {


                            canScan = true;
                            tvName.setText("");
                            tvStatu.setText("垃圾袋已领完，请等待上货");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tvStatu.setText("欢迎领取垃圾袋!!");
                                }
                            }, 1000);

                        }

                    }
                });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_qidian;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void initView() {
        XLog.e("");
        setTimer();
//        initNoteAndRote();
        setScanListerer();
        setPanelListener();
    }

    @Override
    public void initDate() {

//        initListener();
    }

    private void initListener() {

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XLog.e("--", v.toString());
                restart();
               /* String json = "{'id':7281,'address':浙江省杭州市余杭区中泰街道幸福社区德信早安,fullname:朱文明、曹丽霞,company:  1-1-301}";
//                startActivity(new Intent(QidianActivity.this,QiandianTestActivity.class));

                if (JsonUtil.isJsonValid(json)) {
                    String id = JsonUtil.getString("id", buffer.toString());
                    scanResult("", id);
                } else {
                    scanResult(json, "");
                }*/

            }
        });

        tvStatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                postData(1);
//                finish();
//                scanResult("4017515658", "");
                XLog.e("---", "");
            }
        });
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

    private void setScanListerer() {
        mScanManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {
            }

            @Override
            public void onFail(File device, Status status) {
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

    private void setTimer() {
        task = new MyTimeTask(TASK_DELAY_TIME, new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(TIMER);
            }
        });
    }


    private void initNoteAndRote() {

        SharedPreferences psp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        panelNode = psp.getString(Constant.PANEL_NODE, "");
        panelRate = Integer.decode(psp.getString(Constant.PRINT_RATE, "-1"));

        SharedPreferences ssp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        scanNode = ssp.getString(Constant.SCAN_NODE, "");
        scanRate = Integer.decode(ssp.getString(Constant.SCAN_RATE, "-1"));
//        checkNodeAndRate();
    }


    /**
     * 检查设备节点和波特率 没设置 跳转到设置界面
     */
    private void checkNodeAndRate() {

        if (panelNode.length() == 0 || panelRate == -1
                || scanNode.length() == 0 || scanRate == -1) {

            Toast.makeText(this, "请设置全部的设备节点和波特率", Toast.LENGTH_LONG).show();

            startActivity(new Intent(this, QidianSettingActivity.class));
        }
    }

    @Override
    public void showError(String msg) {
        showLong(msg);

    }

    @Override
    public void release() {
        if (task != null) {
            task.stop();
            task = null;
        }

    }

}
