package padd.qlckh.cn.tempad.yipingfang;

import tw.com.prolific.pl2303multilib.PL2303MultiLib;

/**
 * @author Andy
 * @date 2020/11/27 16:45
 * Desc:
 */
class UARTSettingInfo {
    public int iPortIndex = 0;
    public PL2303MultiLib.BaudRate mBaudrate = PL2303MultiLib.BaudRate.B115200;
    public PL2303MultiLib.DataBits mDataBits = PL2303MultiLib.DataBits.D8;
    public PL2303MultiLib.FlowControl mFlowControl = PL2303MultiLib.FlowControl.OFF;
    public PL2303MultiLib.Parity mParity = PL2303MultiLib.Parity.NONE;
    public PL2303MultiLib.StopBits mStopBits = PL2303MultiLib.StopBits.S1;
    public byte sortType = (byte) 13;

    UARTSettingInfo() {
    }
}
