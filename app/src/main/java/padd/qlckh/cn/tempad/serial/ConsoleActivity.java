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

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import padd.qlckh.cn.tempad.R;

public class ConsoleActivity extends SerialPortActivity {

    EditText mReception;
    private TextView textView;

    @Override
    protected int getContentView() {
        return R.layout.console;
    }

    StringBuffer stringBuffer = new StringBuffer();

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mReception != null) {
                    String s = new String(buffer, 0, size);
                    StringBuffer append = stringBuffer.append(s);
                    mReception.append(append.toString());
                    textView.setText(append.toString());
                    Toast.makeText(ConsoleActivity.this,s,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void initView() {

        mReception = (EditText) findViewById(R.id.EditTextReception);

        textView = (TextView)findViewById(R.id.textview);
        EditText Emission = (EditText) findViewById(R.id.EditTextEmission);
        Emission.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int i;
                CharSequence t = v.getText();
                char[] text = new char[t.length()];
                for (i = 0; i < t.length(); i++) {
                    text[i] = t.charAt(i);
                }
                try {
                    String s = new String(text);

                    mOutputStream.write(new String(text).getBytes());
                    mOutputStream.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

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
}
