package com.arpith.dice;

/*****************************************************************************
 * Dice Roller application
 * Author		: 	Arpith and Nagabharan
 * USN			: 	1PE10CS{018,054}
 * Platform		:	Android 2.3.7 (CyanogenMod 7.2)
 * DevelopedOn	:	Ubuntu 12.04.1 using Eclipse Juno
 * TestedOn		:	Xperia Neo V
 * Date			:	Sept 24
 *****************************************************************************/

import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	int value;
	Button activate;
	Button sensetivity;
	ImageView image;
	TextView display;
	TextView display2;
	EditText input;
	int sense;
	boolean acclometerUse;
	private SensorManager sensorManager;
	boolean mInitialized;
	float mLastX, mLastY, mLastZ;
	boolean valFinilized;
	boolean flag;
	float NOISE = (float) 20.0; // Increase sensitivity from here
	Random crazy = new Random();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		acclometerUse = true;
		activate = (Button) findViewById(R.id.button1);
		display = (TextView) findViewById(R.id.textView1);
		image = (ImageView) findViewById(R.id.imageView1);
		input = (EditText) findViewById(R.id.editText1);
		sensetivity = (Button) findViewById(R.id.button2);
		display2 = (TextView) findViewById(R.id.textView2);

		sensetivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sense = Integer.parseInt(input.getText().toString());
				NOISE = (float) sense;
				display2.setText("Sensitivity set to: " + sense);
			}

		});

		activate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				display.setText("Acclerometer Active. Shake to genetate number");
				acclometerUse = true;
				/*
				 * if (!acclometerUse) { acclometerUse = true;
				 * activate.setText("Deactivate Acclerometer"); } else {
				 * acclometerUse = false;
				 * display.setText("Acclerometer deactivated!");
				 * image.setImageResource(R.drawable.icon);
				 * activate.setText("Activate Acclerometer"); }
				 */
			}
		});

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// add listener. The listener will be HelloAndroid (this) class
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	public void onSensorChanged(SensorEvent event) {

		// check sensor type
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			// acclometerUse=false;
			// assign directions
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			if (!mInitialized) {
				mLastX = x;
				mLastY = y;
				mLastZ = z;
				mInitialized = true;
			} else {
				float deltaX = Math.abs(mLastX - x);
				float deltaY = Math.abs(mLastY - y);
				float deltaZ = Math.abs(mLastZ - z);
				if (deltaX < NOISE)
					deltaX = (float) 0.0;
				if (deltaY < NOISE)
					deltaY = (float) 0.0;
				if (deltaZ < NOISE)
					deltaZ = (float) 0.0;
				mLastX = x;
				mLastY = y;
				mLastZ = z;
				if (deltaX > deltaY | deltaY > deltaX) {
					if (acclometerUse) {
						acclometerUse = false;
						value = crazy.nextInt(6) + 1;
						display.setText("Value: " + value);
						switch (value) {
						case 1:
							image.setImageResource(R.drawable.d1);
							break;
						case 2:
							image.setImageResource(R.drawable.d2);
							break;
						case 3:
							image.setImageResource(R.drawable.d3);
							break;
						case 4:
							image.setImageResource(R.drawable.d4);
							break;
						case 5:
							image.setImageResource(R.drawable.d5);
							break;
						case 6:
							image.setImageResource(R.drawable.d6);
							break;
						default:
							image.setImageResource(R.drawable.icon);
						}
						// call function here
						// return Integer.toString(value);
						initializeData(Integer.toString(value));
					}
				}
			}
		}

	}

	public void initializeData(String val) {

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				1);
		nameValuePairs.add(new BasicNameValuePair("value", val));
		sendData(nameValuePairs);
	}

	public void sendData(ArrayList<NameValuePair> data) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://andymysql.host22.com/ins.php");
			httppost.setEntity(new UrlEncodedFormEntity(data));
			HttpResponse response = httpclient.execute(httppost);
		} catch (Exception e) {
			Log.e("log_tag", "Error:  " + e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
