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

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import padd.qlckh.cn.tempad.BaseActivity;
import padd.qlckh.cn.tempad.R;
import padd.qlckh.cn.tempad.SettingActivity;

public class MainMenu extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.main;
    }

    @Override
    public void initView() {

        /**
         * 设置设备和端口
         */
        final Button buttonSetup = (Button)findViewById(R.id.ButtonSetup);
        buttonSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, SettingActivity.class));
            }
        });

        /**
         * 控制台 接收数据和发送数据
         */
        final Button buttonConsole = (Button)findViewById(R.id.ButtonConsole);
        buttonConsole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, ConsoleActivity.class));
            }
        });

        /**
         * 回送数据
         */
        final Button buttonLoopback = (Button)findViewById(R.id.ButtonLoopback);
        buttonLoopback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, LoopbackActivity.class));
            }
        });

        /**
         * 发送
         */
        final Button button01010101 = (Button)findViewById(R.id.Button01010101);
        button01010101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, Sending01010101Activity.class));
            }
        });

        final Button buttonAbout = (Button)findViewById(R.id.ButtonAbout);
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setTitle("About");
                builder.setMessage(R.string.about_msg);
                builder.show();
            }
        });

        final Button buttonQuit = (Button)findViewById(R.id.ButtonQuit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenu.this.finish();
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
