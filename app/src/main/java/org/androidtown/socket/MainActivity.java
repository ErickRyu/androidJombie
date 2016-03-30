package org.androidtown.socket;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 안드로이드에서 소켓 클라이언트로 연결하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {
    Map<String, User> userMap = new HashMap<String, User>();
    EditText input01;
    Button button02;
    Button button;
    Button button2;
    Button button03;

    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userMap.put("E", new User("E"));
        userMap.put("G", new User("G"));
        input01 = (EditText) findViewById(R.id.input01);

        // 소켓 연결하기
        Button sockConn = (Button) findViewById(R.id.socketConn);
        sockConn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String addr = input01.getText().toString().trim();
//
                ConnectThread thread = new ConnectThread(addr);
                thread.start();
            }
        });

        // 입장버튼
        button02 = (Button) findViewById(R.id.button02);
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent1);
            }
        });
    }

    /**
     * 소켓 연결할 스레드 정의
     */
    class User {
        String name;
        int hp;
        boolean isJombie;

        public User(String name) {
            this.name = name;
            hp = 100;
            isJombie = Math.random() > 0.5 ? true : false;
        }
    }

    class ConnectThread extends Thread {
        String hostname;

        public ConnectThread(String addr) {
            hostname = addr;
        }

        public void run() {
            String ip = "172.200.110.143";
            String port = "5000";
            try {
                sock = new Socket(ip, Integer.parseInt(port));
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(sock.getOutputStream());
                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                int count = 0;
                Map<Integer, String> tmpMap = new HashMap<>();

                while (true) {
                    SystemClock.sleep(2000);
//                    writer.println(++count);
//                    writer.print("hh");
//                    writer.flush();
                    tmpMap.put(++count, "test");
                    outstream.writeUnshared(tmpMap);
                    outstream.flush();
                }
            } catch (Exception ex) {
            } // end try
//        private Handler handler = new Handler(){
//            public void handleMessage9Message msg{
//                Toast.makeText(getApplicationContext(), "TEST", 0).show();
//                super.handleMessage(msg);
//            }
//        }
        } // end run()

    }
    /*
    class ConnectThread extends Thread {
        String hostname;

        public ConnectThread(String addr) {
            hostname = addr;
        }

        public void run() {

            try {
                int port = 5001;
                Log.d("MainActivity", "[Info] before send the outstream");
                hostname = "192.168.44.1";
                Socket sock = new Socket(hostname, port);
                //172.200.100.29
                int count = 0;
                while(true) {
                    sleep(2000);
                    ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                    //이거 내 위치로
                    count++;
                    double location_y = 38 + count;
                    double location_x = 39 + count;

                    outstream.writeObject(location_y + " / " + location_x);
//                    outstream.writeObject(_userMap);
                    outstream.flush();

                    //지도 어플에서 이 어플로 hashmap을 어떻게 보내지?
                    //hashmap 정보 받으면 전부 flush
                    Log.d("MainActivity", "[Info] after send the outstream");
//                location_y = 23.23;
//                location_x = 32.32;
//                outstream.writeObject(location_y + " / " + location_x);
//                outstream.flush();

                    ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
                    Object obj = instream.readObject();


                    Log.d("MainActivity", "[Info] 서버에서 받은 메시지 : " + obj);

                    System.out.print(obj);
                }

            } catch (Exception ex) {

                Log.d("MainActivity", "[Error] Toast Error???");
                ex.printStackTrace();
            }
        }
//        private Handler handler = new Handler(){
//            public void handleMessage9Message msg){
//                Toast.makeText(getApplicationContext(),"TEST", 0).show();
//                super.handleMessage(msg);
//            }
//        }
    }
    */
}