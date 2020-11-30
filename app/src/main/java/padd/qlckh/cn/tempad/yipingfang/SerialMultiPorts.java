package padd.qlckh.cn.tempad.yipingfang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import java.util.Arrays;

import padd.qlckh.cn.tempad.XLog;
import tw.com.prolific.pl2303multilib.PL2303MultiLib;

public class SerialMultiPorts {
    private static final String ACTION_USB_PERMISSION = "padd.qlckh.cn.tempad.USB_PERMISSION";
    private static final int DeviceIndex1 = 0;
    private static final int DeviceIndex2 = 1;
    private static final int DeviceIndex3 = 2;
    private static final int DeviceIndex4 = 3;
    private static final int MAX_DEVICE_COUNT = 4;
    private static final int[] SORT_ID_ARRAY = new int[]{4, 5, 3, 2};
    private static final byte[] SORT_TYPE_ARRAY = new byte[]{(byte) 29, (byte) 45, (byte) 61, (byte) 77};
    private static final byte SORT_TYPE_DEFAULT = (byte) 13;
    private static final String TAG = "SerialMultiPorts";
    private static final int TIME_DELAY_READ = 300;
    private final BroadcastReceiver PLMultiLibReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SerialMultiPorts.this.mSerialMulti.PLUART_MESSAGE)) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    for (int i = Integer.valueOf((String) extras.get(SerialMultiPorts.this.mSerialMulti.PLUART_DETACHED)).intValue(); i < 4; i++) {
                        SerialMultiPorts.this.bDeviceOpened[i] = false;
                    }
                }
            }
        }
    };
    private Runnable ReadLoop1 = new Runnable() {
        @Override
        public void run() {
            do {
                SerialMultiPorts.this.readBufferArray[0].readLen = SerialMultiPorts.this.mSerialMulti.PL2303Read(0, SerialMultiPorts.this.readBufferArray[0].readBuf);
                if (SerialMultiPorts.this.readBufferArray[0].readLen > 0) {
                    SerialMultiPorts.this.mHandler1.post(new Runnable() {
                        @Override
                        public void run() {
                            String sbHex = SerialMultiPorts.this.bytesToHexString(SerialMultiPorts.this.readBufferArray[0].readBuf).toUpperCase();
                            ReadBufferModel readBufferModel = SerialMultiPorts.this.readBufferArray[0];
                            readBufferModel.time++;
                            SerialMultiPorts.this.setPortType(0, sbHex);
                        }
                    });
                }
                SerialMultiPorts.this.DelayTime(300);
            } while (!SerialMultiPorts.this.gThreadStop[0]);
            SerialMultiPorts.this.gRunningReadThread[0] = false;
        }
    };
    private Runnable ReadLoop2 = new Runnable() {
        @Override
        public void run() {
            do {
                SerialMultiPorts.this.readBufferArray[1].readLen = SerialMultiPorts.this.mSerialMulti.PL2303Read(1, SerialMultiPorts.this.readBufferArray[1].readBuf);
                if (SerialMultiPorts.this.readBufferArray[1].readLen > 0) {
                    SerialMultiPorts.this.mHandler2.post(new Runnable() {
                        @Override
                        public void run() {
                            String sbHex = SerialMultiPorts.this.bytesToHexString(SerialMultiPorts.this.readBufferArray[1].readBuf).toUpperCase();
                            ReadBufferModel readBufferModel = SerialMultiPorts.this.readBufferArray[1];
                            readBufferModel.time++;
                            SerialMultiPorts.this.setPortType(1, sbHex);
                            XLog.d(SerialMultiPorts.TAG, "readBufferArray1 : " + sbHex);
                        }
                    });
                }
                SerialMultiPorts.this.DelayTime(300);
            } while (!SerialMultiPorts.this.gThreadStop[1]);
            SerialMultiPorts.this.gRunningReadThread[1] = false;
        }
    };
    private Runnable ReadLoop3 = new Runnable() {
        @Override
        public void run() {
            do {
                SerialMultiPorts.this.readBufferArray[2].readLen = SerialMultiPorts.this.mSerialMulti.PL2303Read(2, SerialMultiPorts.this.readBufferArray[2].readBuf);
                if (SerialMultiPorts.this.readBufferArray[2].readLen > 0) {
                    SerialMultiPorts.this.mHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            String sbHex = SerialMultiPorts.this.bytesToHexString(SerialMultiPorts.this.readBufferArray[2].readBuf).toUpperCase();
                            ReadBufferModel readBufferModel = SerialMultiPorts.this.readBufferArray[2];
                            readBufferModel.time++;
                            SerialMultiPorts.this.setPortType(2, sbHex);
                        }
                    });
                }
                SerialMultiPorts.this.DelayTime(300);
            } while (!SerialMultiPorts.this.gThreadStop[2]);
            SerialMultiPorts.this.gRunningReadThread[2] = false;
        }
    };
    private Runnable ReadLoop4 = new Runnable() {
        @Override
        public void run() {
            do {
                SerialMultiPorts.this.readBufferArray[3].readLen = SerialMultiPorts.this.mSerialMulti.PL2303Read(3, SerialMultiPorts.this.readBufferArray[3].readBuf);
                if (SerialMultiPorts.this.readBufferArray[3].readLen > 0) {
                    SerialMultiPorts.this.mHandler4.post(new Runnable() {
                        @Override
                        public void run() {
                            String sbHex = SerialMultiPorts.this.bytesToHexString(SerialMultiPorts.this.readBufferArray[3].readBuf).toUpperCase();
                            ReadBufferModel readBufferModel = SerialMultiPorts.this.readBufferArray[3];
                            readBufferModel.time++;
                            SerialMultiPorts.this.setPortType(3, sbHex);
                        }
                    });
                }
                SerialMultiPorts.this.DelayTime(300);
            } while (!SerialMultiPorts.this.gThreadStop[3]);
            SerialMultiPorts.this.gRunningReadThread[3] = false;
        }
    };
    /* access modifiers changed from: private */
    public boolean[] bDeviceOpened = new boolean[4];
    /* access modifiers changed from: private */
    public boolean[] gRunningReadThread = new boolean[4];
    /* access modifiers changed from: private */
    public boolean[] gThreadStop = new boolean[4];
    private UARTSettingInfo[] gUARTInfoList;
    /* access modifiers changed from: private */
    public int iDeviceCount = 0;
    /* access modifiers changed from: private */
    public Context mCtx;
    Handler mHandler1 = new Handler();
    Handler mHandler2 = new Handler();
    Handler mHandler3 = new Handler();
    Handler mHandler4 = new Handler();
    /* access modifiers changed from: private */
    public PL2303MultiLib mSerialMulti;
    /* access modifiers changed from: private */
    public ReadBufferModel[] readBufferArray = new ReadBufferModel[4];

    public void init(Context ctx) {
        this.mCtx = ctx;
        this.mSerialMulti = new PL2303MultiLib((UsbManager) this.mCtx.getSystemService(Context.USB_SERVICE), this.mCtx, ACTION_USB_PERMISSION);
        this.mSerialMulti.PL2303Device_SetCommTimeouts(500);
        this.gUARTInfoList = new UARTSettingInfo[4];
        for (int i = 0; i < 4; i++) {
            this.gUARTInfoList[i] = new UARTSettingInfo();
            this.readBufferArray[i] = new ReadBufferModel();
            this.gUARTInfoList[i].iPortIndex = i;
            this.gThreadStop[i] = false;
            this.gRunningReadThread[i] = false;
            this.bDeviceOpened[i] = false;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SerialMultiPorts.this.mSerialMulti == null) {
                    SerialMultiPorts.this.iDeviceCount = 0;
                } else {
                    SerialMultiPorts.this.iDeviceCount = SerialMultiPorts.this.mSerialMulti.PL2303Enumerate();
                }
                if (SerialMultiPorts.this.iDeviceCount == 0) {
                    Toast.makeText(SerialMultiPorts.this.mCtx, "未检测到串口设备", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(SerialMultiPorts.this.mCtx, "检测到" + SerialMultiPorts.this.iDeviceCount + "个串口设备", Toast.LENGTH_LONG).show();
                int[] resArray = new int[Math.min(4, SerialMultiPorts.this.iDeviceCount)];
                for (int i = 0; i < Math.min(4, SerialMultiPorts.this.iDeviceCount); i++) {
                    if (!SerialMultiPorts.this.bDeviceOpened[i]) {
                        SerialMultiPorts.this.OpenUARTDevice(i);
                        SerialMultiPorts.this.DelayTime(1000);
                    }
                }
            }
        }, IDEConstants.IDE_DATA_XFER_TIMEOUT);
    }

    public int findPortIndexBySortId(int sortId) {
        byte chooseSortType = sortId == SORT_ID_ARRAY[0] ? SORT_TYPE_ARRAY[0] : sortId == SORT_ID_ARRAY[1] ? SORT_TYPE_ARRAY[1] : sortId == SORT_ID_ARRAY[2] ? SORT_TYPE_ARRAY[2] : sortId == SORT_ID_ARRAY[3] ? SORT_TYPE_ARRAY[3] : SORT_TYPE_DEFAULT;
        if (this.gUARTInfoList == null) {
            return -1;
        }
        int i = 0;
        while (i < this.gUARTInfoList.length) {
            if (chooseSortType != SORT_TYPE_DEFAULT && chooseSortType == this.gUARTInfoList[i].sortType) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public void OpenUARTDevice(int index) {
        if (this.mSerialMulti != null && this.mSerialMulti.PL2303IsDeviceConnectedByIndex(index)) {
            UARTSettingInfo info = this.gUARTInfoList[index];
            if (this.mSerialMulti.PL2303OpenDevByUARTSetting(index, info.mBaudrate, info.mDataBits, info.mStopBits, info.mParity, info.mFlowControl)) {
                Toast.makeText(this.mCtx, "设备" + index + "打开成功!", Toast.LENGTH_LONG).show();
                this.bDeviceOpened[index] = true;
                if (!this.gRunningReadThread[index]) {
                    UpdateDisplayView(index);
                    return;
                }
                return;
            }
            Toast.makeText(this.mCtx, "设备" + index + "打开失败!", Toast.LENGTH_LONG).show();
        }
    }

    private void UpdateDisplayView(int index) {
        this.gThreadStop[index] = false;
        this.gRunningReadThread[index] = true;
        if (index == 0) {
            new Thread(this.ReadLoop1).start();
        } else if (1 == index) {
            new Thread(this.ReadLoop2).start();
        } else if (2 == index) {
            new Thread(this.ReadLoop3).start();
        } else if (3 == index) {
            new Thread(this.ReadLoop4).start();
        }
    }

    public synchronized byte[] getBuffer(int index) {
        return Arrays.copyOf(this.readBufferArray[index].readBuf, this.readBufferArray[index].readBuf.length);
    }

    /* access modifiers changed from: private */
    public void setPortType(int portIndex, String sbHex) {
        if (sbHex.contains("0A1D")) {
            this.gUARTInfoList[portIndex].sortType = SORT_TYPE_ARRAY[0];
        } else if (sbHex.contains("0A2D")) {
            this.gUARTInfoList[portIndex].sortType = SORT_TYPE_ARRAY[1];
        } else if (sbHex.contains("0A3D")) {
            this.gUARTInfoList[portIndex].sortType = SORT_TYPE_ARRAY[2];
        } else if (sbHex.contains("0A4D")) {
            this.gUARTInfoList[portIndex].sortType = SORT_TYPE_ARRAY[3];
        } else {
            this.gUARTInfoList[portIndex].sortType = SORT_TYPE_DEFAULT;
        }
    }

    /* access modifiers changed from: private */
    public void DelayTime(int dwTimeMS) {
        if (dwTimeMS == 0) {
            Thread.yield();
            return;
        }
        long StartTime = System.currentTimeMillis();
        long CheckTime;
        do {
            CheckTime = System.currentTimeMillis();
            Thread.yield();
        } while (CheckTime - StartTime <= ((long) dwTimeMS));
    }

    public int getWeight(int index) {
        byte[] readData = getBuffer(index);
        StringBuffer sbHex = new StringBuffer();
        if (readData.length > 0) {
            for (byte b : readData) {
                String hv = Integer.toHexString(b & 255);
                if (hv.length() < 2) {
                    sbHex.append(0);
                }
                sbHex.append(hv);
            }
        } else {
            Toast.makeText(this.mCtx, "无重量数据信息", Toast.LENGTH_LONG).show();
        }
        byte currentSortType = this.gUARTInfoList[index].sortType;
        int i = 0;
        while (i < readData.length - 7) {
            if (readData[i] == (byte) 10 && readData[i + 1] == currentSortType && readData[i + 2] == (byte) 17) {
                return ((((readData[i + 3] & 255) * ((int) Math.pow(256.0d, 3.0d))) + ((readData[i + 4] & 255) * ((int) Math.pow(256.0d, 2.0d)))) + ((readData[i + 5] & 255) * ((int) Math.pow(256.0d, 1.0d)))) + (readData[i + 6] & 255);
            }
            i++;
        }
        return 0;
    }

    public int getWeightBySortId(int sortId) {
        int index = findPortIndexBySortId(sortId);
        if (index >= 0) {
            return getWeight(index);
        }
        Toast.makeText(this.mCtx, "找不到对应箱子,请联系客服，检查设备后重启!", Toast.LENGTH_LONG).show();
        return 0;
    }

    public int writeDataToSerial(int index, int cmdType) {
        if (this.mSerialMulti == null) {
            return -1;
        }
        if (!this.mSerialMulti.PL2303IsDeviceConnectedByIndex(index)) {
            return -1;
        }
        byte currentSortType = this.gUARTInfoList[index].sortType;
        byte[] cmd = new byte[7];
        if (cmdType == 1) {
            cmd = new byte[]{(byte) 10, currentSortType, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        }
        if (cmdType == 2) {
            cmd = new byte[]{(byte) 10, currentSortType, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        }
        if (cmdType == 3) {
            cmd = new byte[]{(byte) 10, currentSortType, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        }
        if (cmdType == 4) {
            cmd = new byte[]{(byte) 10, currentSortType, (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        }
        int res = this.mSerialMulti.PL2303Write(index, cmd);
        if (res >= 0) {
            return res;
        }
        Toast.makeText(this.mCtx, "串口" + index + "写入失败，返回值为：" + res, Toast.LENGTH_LONG).show();
        return res;
    }

    public int writeDataToBySortId(int sortId, int cmdType) {
        int index = findPortIndexBySortId(sortId);
        if (index >= 0) {
            return writeDataToSerial(index, cmdType);
        }
        Toast.makeText(this.mCtx, "找不到对应箱子,请检查设备后重启!", Toast.LENGTH_LONG).show();
        return -1;
    }

    public void destroy() {
        if (this.mSerialMulti != null) {
            for (int i = 0; i < 4; i++) {
                this.gThreadStop[i] = true;
            }
            if (this.iDeviceCount > 0) {
                this.mCtx.unregisterReceiver(this.PLMultiLibReceiver);
            }
            this.mSerialMulti.PL2303Release();
            this.mSerialMulti = null;
        }
    }

    public void resume() {
        this.iDeviceCount = this.mSerialMulti.PL2303Enumerate();
        if (this.iDeviceCount != 0) {
            for (int i = 0; i < Math.min(4, this.iDeviceCount); i++) {
                if (!this.bDeviceOpened[i]) {
                    OpenUARTDevice(i);
                    DelayTime(1000);
                }
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(this.mSerialMulti.PLUART_MESSAGE);
            this.mCtx.registerReceiver(this.PLMultiLibReceiver, filter);
        }
    }

    /* access modifiers changed from: private */
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}