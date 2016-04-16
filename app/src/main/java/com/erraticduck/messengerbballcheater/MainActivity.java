package com.erraticduck.messengerbballcheater;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText mScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mScore = (EditText) findViewById(R.id.score);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_WORLD_READABLE);
		if (prefs.contains(Cheater.SHARED_PREFS_KEY_SCORE_OVERRIDE)) {
			mScore.setText(Integer.toString(prefs.getInt(Cheater.SHARED_PREFS_KEY_SCORE_OVERRIDE, 0)));
		}
	}

	public void saveScore(View v) {
		if (mScore.getText().length() == 0) {
			Toast.makeText(this, "Must enter a number!", Toast.LENGTH_SHORT).show();
		} else {
			int scoreToSave = Integer.parseInt(mScore.getText().toString());
			SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), Context.MODE_WORLD_READABLE).edit();
			editor.putInt(Cheater.SHARED_PREFS_KEY_SCORE_OVERRIDE, scoreToSave);
			editor.apply();
			Toast.makeText(this, "Your score will be replaced by " + scoreToSave, Toast.LENGTH_SHORT).show();
		}
	}

	public void clearScore(View v) {
		mScore.getText().clear();
		SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), Context.MODE_WORLD_READABLE).edit();
		editor.clear().apply();
		Toast.makeText(this, "Score override disabled", Toast.LENGTH_SHORT).show();
	}

	public void toggleIntegerMaxValue(View v) {
		mScore.setText(Integer.toString(Integer.MAX_VALUE));
	}

}
