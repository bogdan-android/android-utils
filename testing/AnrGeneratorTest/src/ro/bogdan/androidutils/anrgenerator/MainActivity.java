package ro.bogdan.androidutils.anrgenerator;

import ro.bogdan.androidutils.anrdetector.AnrDetector;
import ro.bogdan.androidutils.anrdetector.loggers.IntentLogger;
import ro.bogdan.anrdetectorutils.AnrType;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(this);
		Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(this);
		Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(this);
		Button button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(this);
		Button button7 = (Button) findViewById(R.id.button7);
		button7.setOnClickListener(this);
		Button button8 = (Button) findViewById(R.id.button8);
		button8.setOnClickListener(this);
		Button button9 = (Button) findViewById(R.id.button9);
		button9.setOnClickListener(this);
		AnrDetector.getInstance().init(new IntentLogger(this));
		AnrDetector.getInstance().setIgnoreNotFromPackage(getPackageName()); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button1:
				causeANRType1(AnrType.SMALL);
				break;

			case R.id.button2:
				causeANRType1(AnrType.MEDIUM);
				break;

			case R.id.button3:
				causeANRType1(AnrType.LARGE);
				break;

			case R.id.button4:
				causeANRType2(AnrType.SMALL);
				break;

			case R.id.button5:
				causeANRType2(AnrType.MEDIUM);
				break;

			case R.id.button6:
				causeANRType2(AnrType.LARGE);
				break;

			case R.id.button7:
				causeANRType3(AnrType.SMALL);
				break;
			case R.id.button8:
				causeANRType3(AnrType.MEDIUM);
				break;
			case R.id.button9:
				causeANRType3(AnrType.LARGE);
				break;

			default:
				break;
		}

	}

	private void causeANRType1(AnrType type) {
		long wait = type.getWaitTime() + 300;
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void causeANRType2(AnrType type) {
		long wait = type.getWaitTime() + 300;
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void causeANRType3(AnrType type) {
		long wait = type.getWaitTime() + 300;
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
