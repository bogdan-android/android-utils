package ro.bogdan.androidutils.anrdetectorexplorer.view;

import org.holoeverywhere.widget.TextView;

import ro.bogdan.androidutils.anrdetectorexplorer.R;
import ro.bogdan.androidutils.anrdetectorexplorer.db.AnrLog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class AnrLogView extends RelativeLayout {

	private TextView title;

	public AnrLogView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context);
	}

	public AnrLogView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AnrLogView(Context context) {
		this(context, null, 0);
	}

	private void inflate(Context context) {
		RelativeLayout.inflate(context, R.layout.anr_list_item, this);
		title = (TextView) findViewById(R.id.anr_title);
	}

	public void setAnrLog(AnrLog log) {
		title.setText(log.getTitle());
	}
}
