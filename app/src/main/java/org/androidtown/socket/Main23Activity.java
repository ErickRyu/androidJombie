package org.androidtown.socket;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.Attributes;

public class Main23Activity extends AppCompatActivity {
    GoogleMap map;
    LocationManager manager;
    MyLocationListener listener;
    SensorManager sensorManager;
    MySensorListener sensorListener;
/*    static double changeLocationValue = 0.0000200;
    static HashMap<String, User> usermap = new HashMap<String, User>();
    class User {
        Double x;
        Double y;

        User(Double x, Double y) {
            this.x = x;
            this.y = y;

        }
    }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main23);

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map); //fragment를 찾아야하기 때문에
        map = fragment.getMap(); //구글맵 가져오기

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //위치관리자 시스템에서 호출
        listener = new MyLocationListener(); // 위치정보를 원할때만 받을 수 있는 리스너 생성

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //센서관리자 시스템에서 호출
        sensorListener = new MySensorListener(); // 센서리스너

    }




    public void onButton1Clicked () { //버튼 클릭시 정보 보내고 받기

    }




    @Override
    protected void onPause() {
        super.onPause();

        map.setMyLocationEnabled(false); // 위치 찍기

        if (manager != null) {
            manager.removeUpdates(listener); // pause가 뒤로가기 눌렀을때 반응하는 메소드니까 지속적으로 업데이트하는것을 꺼줘야함
        }

        sensorManager.unregisterListener(sensorListener); //
    }

    @Override
    protected void onResume() {
        super.onResume();

        map.setMyLocationEnabled(true); // 내 위치 보여주기

        requestMyLocation();

        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);


    }





    public void requestMyLocation() {

        long minTime = 1000; //ms단위 1초당
        float minDistance = 0; // 항상 업데이트 하기


        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                minTime, minDistance, listener); // 업데이트된 정보를 리스너에게 보내는 메소드

        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 건물안에 있을때는 gps가 정상 작동 안되므로 기지국 거리를 이용한 네트워크 위치기반 사용
                minTime, minDistance, listener);
        Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastLocation != null) {
            Double latitude = lastLocation.getLatitude();
            Double longitude = lastLocation.getLongitude();


            Log.d("mainActivity", "가장 최근의 내 위치 : " + latitude + ", " + longitude);
//            changeLocationValue += 0.00002000;
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
        }
    }
    private void showCurrentMap(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);

  //      map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.clear();

        MarkerOptions marker = new MarkerOptions();
        //changeLocationValue += 0.000200;
//        marker.position(new LatLng(latitude + 0.00001, longitude + changeLocationValue));
//        marker.title("1");
//        marker.snippet("잠실 지점");
//        marker.draggable(true);
//        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bank));
//        map.addMarker(marker);


        //맵에 점찍기
        map.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(2)
                .strokeColor(Color.RED)
                .fillColor(Color.RED));
    }

    class MySensorListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) { // 센서의 값을 여기서 전달받음 센서의 방향을 받아서 이미지를 돌려주면 됨
            Log.d("MySensorListener", "sensor #0 : " + event.values[0]);
        }
    }

 /*   public void drawCircle(Double x, Double y) { // 1초마다 업데이트, 단순 점찍기. 클릭했을때 이름 나오게하려면 마커로

        map.addCircle(new CircleOptions()
                .center(new LatLng(x, y))
                .radius(2)
                .strokeColor(Color.RED)
                .fillColor(Color.RED));
    }*/

    class MyLocationListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {  // 위치정보가 갱신될 때 호출되는 메소드

            Double latitude = location.getLatitude();  // 위도와 경도를 받아오는 방법
            Double longitude = location.getLongitude();


            Log.d("mainActivity", "내 위치 : " + latitude + ", " + longitude);

            showCurrentMap(latitude, longitude);
            //showCurrentMap(37.296634, 126.836134);



            //서버에서 User 정보(x좌표, y좌표) 받기


/*
            usermap.put("Er", new User(38.222,40.333));
            User user1 = usermap.get("Er");
            drawCircle(user1.x, user1.y); // 위치정보가 갱신될 때 호출됨 requestMyLocation() 에서 1초마다 업데이트 하도록 바꿨으니 이 메소드도 1초마다 갱신될듯
*/


        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

///////////////


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
