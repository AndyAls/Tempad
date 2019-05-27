package padd.qlckh.cn.tempad;

import java.text.SimpleDateFormat;

/**
 * @author
 *串口数据
 */
public class ComBean {
	public byte[] bRec=null;
	public ComBean(byte[] buffer){
	    bRec=buffer;
	}
}