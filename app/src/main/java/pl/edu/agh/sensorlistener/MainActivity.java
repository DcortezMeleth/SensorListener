package pl.edu.agh.sensorlistener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    WebSocket ws;

    private SensorManager sensorManager;

    private Sensor sensor;

    private Sensor sensor2;

    private TextView valueX;

    private TextView valueY;

    private TextView valueZ;

    private TextView valueX2;

    private TextView valueY2;

    private TextView valueZ2;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ws.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        valueX = (TextView) findViewById(R.id.x_value);
        valueY = (TextView) findViewById(R.id.y_value);
        valueZ = (TextView) findViewById(R.id.z_value);

        valueX2 = (TextView) findViewById(R.id.x_value2);
        valueY2 = (TextView) findViewById(R.id.y_value2);
        valueZ2 = (TextView) findViewById(R.id.z_value2);

        try {
            ws = new WebSocketFactory().createSocket("ws:///192.168.0.16/endpoint");
            ws.connectAsynchronously();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
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
        if(id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            valueX.setText(String.valueOf(event.values[0]));
            valueY.setText(String.valueOf(event.values[1]));
            valueZ.setText(String.valueOf(event.values[2]));
            ws.sendText("ACC;" + String.valueOf(event.values[0]) + ";" +
                    String.valueOf(event.values[1]) + ";" + String.valueOf(event.values[2]));
        } else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            valueX2.setText(String.valueOf(event.values[0]));
            valueY2.setText(String.valueOf(event.values[1]));
            valueZ2.setText(String.valueOf(event.values[2]));
            ws.sendText("GYR;" + String.valueOf(event.values[0]) + ";" +
                    String.valueOf(event.values[1]) + ";" + String.valueOf(event.values[2]));
        }

    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int accuracy) {

    }
}
