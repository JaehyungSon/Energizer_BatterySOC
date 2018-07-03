package com.example.sonjaehyeong.energizer_batterysoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);
        final EditText user_id_editText = (EditText)findViewById(R.id.user_id);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 서비스 시작하기
                Log.d("test", "액티비티-서비스 시작버튼클릭");
                Intent intent = new Intent(
                        getApplicationContext(),//현재제어권자
                        MyService.class); // 이동할 컴포넌트


                if(user_id_editText.getText().toString().equals("")){
                    intent.putExtra("user_id","null");
                }else{
                    intent.putExtra("user_id",user_id_editText.getText());
                }
                startService(intent); // 서비스 시작
            }
        });


        //서비스 종료 - 사용안함
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("test", "액티비티-서비스 종료버튼클릭");
                Intent intent = new Intent(
                        getApplicationContext(),
                        MyService.class);
                stopService(intent);
            }
        });
    }
}
