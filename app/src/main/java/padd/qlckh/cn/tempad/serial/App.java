/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package padd.qlckh.cn.tempad.serial;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.tencent.bugly.crashreport.CrashReport;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import padd.qlckh.cn.tempad.ApiService;
import padd.qlckh.cn.tempad.CommUtils;
import padd.qlckh.cn.tempad.Constant;
import padd.qlckh.cn.tempad.CustomErrorActivity;
import padd.qlckh.cn.tempad.R;
import padd.qlckh.cn.tempad.XLog;
import padd.qlckh.cn.tempad.http.RxHttpUtils;
import padd.qlckh.cn.tempad.manager.SerialPortManager;
import padd.qlckh.cn.tempad.yipingfang.YiMainActivity;

/**
 * @author Andy
 * @date   2018/9/19 15:51
 * @link   {http://blog.csdn.net/andy_l1}
 * Desc:    App.java
 */
public class App extends Application {

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    private SerialPortManager mWeightManager = null;
    private SerialPortManager mScanManager = null;
    private SerialPortManager mPrintManager = null;
    private SerialPortManager mPanelManager = null;


    @Override
    public void onCreate() {
        super.onCreate();
        initHttp();
        initCarsh();
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(this);
        userStrategy.setDeviceID(CommUtils.getIMEI(this));
        CrashReport.setUserId(CommUtils.getIMEI(this));
        CrashReport.initCrashReport(getApplicationContext(), "6f38b53273", false,userStrategy);
    }

    private void initCarsh() {

        //整个配置属性，可以设置一个或多个，也可以一个都不设置
        CaocConfig.Builder.create()
                //程序在后台时，发生崩溃的三种处理方式
                //BackgroundMode.BACKGROUND_MODE_SHOW_CUSTOM: //当应用程序处于后台时崩溃，也会启动错误页面，
                //BackgroundMode.BACKGROUND_MODE_CRASH:      //当应用程序处于后台崩溃时显示默认系统错误（一个系统提示的错误对话框），
                //BackgroundMode.BACKGROUND_MODE_SILENT:     //当应用程序处于后台时崩溃，默默地关闭程序！
                .backgroundMode(CaocConfig.BACKGROUND_MODE_CRASH)
                .enabled(true)     //false表示对崩溃的拦截阻止。用它来禁用customactivityoncrash框架
                .showErrorDetails(true) //这将隐藏错误活动中的“错误详细信息”按钮，从而隐藏堆栈跟踪,—》针对框架自带程序崩溃后显示的页面有用(DefaultErrorActivity)。。
                .showRestartButton(true)    //是否可以重启页面,针对框架自带程序崩溃后显示的页面有用(DefaultErrorActivity)。
                .trackActivities(true)     //错误页面中显示错误详细信息；针对框架自带程序崩溃后显示的页面有用(DefaultErrorActivity)。
                .minTimeBetweenCrashesMs(500)      //定义应用程序崩溃之间的最短时间，以确定我们不在崩溃循环中。比如：在规定的时间内再次崩溃，框架将不处理，让系统处理！
                .errorDrawable(R.mipmap.ic_launcher)     //崩溃页面显示的图标
                .restartActivity(YiMainActivity.class)      //重新启动后的页面
                .errorActivity(CustomErrorActivity.class) //程序崩溃后显示的页面
                .apply();
    }

    private void initHttp() {
        /**
         * 全局请求的统一配置
         */
        RxHttpUtils.init(this);

        RxHttpUtils
                .getInstance()
                //开启全局配置
                .config()
                //全局的BaseUrl
                .setBaseUrl(ApiService.BASE_URL)
                //开启缓存策略
//                .setCache()
                //全局的请求头信息
                //.setHeaders(headerMaps)
                //全局持久话cookie,保存本地每次都会携带在header中
                .setCookie(false)
                //全局ssl证书认证
                //信任所有证书,不安全有风险
                .setSslSocketFactory()
                //使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.cer"))
                //使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
                //.setSslSocketFactory(getAssets().open("your.bks"), "123456", getAssets().open("your.cer"))
                //全局超时配置
                .setReadTimeout(ApiService.DEFAULT_TIME)
                //全局超时配置
                .setWriteTimeout(ApiService.DEFAULT_TIME)
                //全局超时配置
                .setConnectTimeout(ApiService.DEFAULT_TIME)
                //全局是否打开请求log日志
                .setLog(true);
    }

    public SerialPortManager getmWeightManager() {
        if (mWeightManager == null) {
            mWeightManager = new SerialPortManager();
            SharedPreferences sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
            String weightNode = sp.getString(Constant.WEGHT_NODE, "");
            int weightRate = Integer.decode(sp.getString(Constant.WEGHT_RATE, "-1"));
            mWeightManager.openSerialPort(new File(weightNode), weightRate, activity);

        }
        return mWeightManager;
    }

    public SerialPortManager getmScanManager() {
        if (mScanManager == null) {
            mScanManager = new SerialPortManager();
            SharedPreferences ssp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
//            String scanNode = ssp.getString(Constant.SCAN_NODE, "");
//            int scanRate = Integer.decode(ssp.getString(Constant.SCAN_RATE, "-1"));
            String scanNode ="/dev/ttyO4";
            //9600
            int scanRate = 9600;
//            mScanManager.openSerialPort(new File(scanNode), scanRate, activity);
        }
        return mScanManager;
    }

    public SerialPortManager getmPrintManager() {
        if (mPrintManager == null) {
            mPrintManager = new SerialPortManager();

            SharedPreferences prsp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
            String printNode = prsp.getString(Constant.PRINT_NODE, "");
            int printRate = Integer.decode(prsp.getString(Constant.PRINT_RATE, "-1"));
//            mPrintManager.openSerialPort(new File(printNode), printRate, activity);
        }
        return mPrintManager;
    }

    public SerialPortManager getmPanelManager(Activity activity) {
        if (mPanelManager == null) {
            mPanelManager = new SerialPortManager();
            SharedPreferences psp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
            String panelNode = psp.getString(Constant.PANEL_NODE, "");
            int panelRate = Integer.decode(psp.getString(Constant.PRINT_RATE, "-1"));
            /*String panelNode ="/dev/ttyO3";
            int panelRate = 38400;*/
            mPanelManager.openSerialPort(new File(panelNode), panelRate,activity);
        }
        return mPanelManager;
    }

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            SharedPreferences sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
            String path = sp.getString(Constant.WEGHT_NODE, "");
            int baudrate = Integer.decode(sp.getString(Constant.WEGHT_RATE, "-1"));

            XLog.e("---", "getSerialPort()", path, baudrate);
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    public void closeAllSerialPort() {
        if (mPanelManager != null) {
            mPanelManager.closeSerialPort();
            mPanelManager = null;
        }

        if (mPrintManager != null) {
            mPrintManager.closeSerialPort();
            mPrintManager = null;
        }

        if (mScanManager != null) {
            mScanManager.closeSerialPort();
            mScanManager = null;
        }
        if (mWeightManager != null) {
            mWeightManager.closeSerialPort();
            mWeightManager = null;
        }
    }
}
