/*
 * Copyright 2011 Cedric Priscal
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

import java.io.IOException;
import java.util.Arrays;

import android.os.Bundle;

import padd.qlckh.cn.tempad.R;

public class Sending01010101Activity extends SerialPortActivity {

	SendingThread mSendingThread;
	byte[] mBuffer;

    @Override
    protected int getContentView() {
        return R.layout.sending01010101;
    }

    @Override
	protected void onDataReceived(byte[] buffer, int size) {
		// ignore incoming data
	}

    @Override
    public void initView() {
        mBuffer = new byte[1024];
        Arrays.fill(mBuffer, (byte) 0x55);
        if (mSerialPort != null) {
            mSendingThread = new SendingThread();
            mSendingThread.start();
        }
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

    private class SendingThread extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				try {
					if (mOutputStream != null) {
						mOutputStream.write(mBuffer);
					} else {
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
}
