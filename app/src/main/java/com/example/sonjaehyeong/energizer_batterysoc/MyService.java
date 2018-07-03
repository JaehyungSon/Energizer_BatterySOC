package com.example.sonjaehyeong.energizer_batterysoc;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyService extends Service {

    String userId="";
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddhhmmss");

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onCreate();
        this.userId=intent.getExtras().get("user_id").toString();   //사용자 구분

        Log.d("MyService","서비스 호출 및 스레드 호출");

        firebaseUploadThread thread = new firebaseUploadThread();
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    class firebaseUploadThread extends Thread{
        public void run(){
            //파이어베이스 선언
            FirebaseDatabase fd = FirebaseDatabase.getInstance();
            DatabaseReference Ref = fd.getReference().child("BatteryData").child(userId);


            for(;;){
                //배터리 상태 가져오는 부분
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = registerReceiver(null, ifilter);

                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
                float temperature = (float) batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                float volt = (float) batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE,-1);


                //파이어베이스 데이터 업로드부분
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("level",level+"");
                data.put("volt",volt+"");
                data.put("temperature",temperature+"");
                data.put("health",health+"");

                HashMap<String,Object> child = new HashMap<String,Object>();

                //현재시간을 기준으로 하여 하위 폴더에 업로드

                long nowTime = System.currentTimeMillis();
                Date newDate = new Date(nowTime);


                child.put(mFormat.format(newDate),data);
                Ref.updateChildren(child);


                try {
                    Thread.sleep(30 * 1000);//30초에 한번씩 전송
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public MyService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
