package fr.alpha.izlycheck;

import org.apache.commons.lang3.Validate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MainActivity extends Activity {

	private static SharedPreferences pref;

	public static SharedPreferences getPreferences()
	{
		return pref;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		updateBalance();
	}

	private void setPreferences()
	{
		Context appContext = this.getApplicationContext();
		pref = appContext.getSharedPreferences("IzlyCheck", Context.MODE_PRIVATE);
	}


	private void updateBalance(){
		final float balance = getBalance();

		final TextView balanceView = (TextView) findViewById(R.id.balance);
		final String defaultText = getString(R.string.balance);
		final String balanceText = defaultText.replace("%balance%", String.valueOf(balance));

		balanceView.setText(balanceText);
	}

	public void saveButtonClicked(View view)
	{
		EditText loginWidget = (EditText) findViewById(R.id.login);
		EditText passwdWidget = (EditText) findViewById(R.id.passwd);
		String login = loginWidget.getText().toString();
		String passwd = passwdWidget.getText().toString();

		saveCredentials(login, passwd);
	}

	private float getBalance(){
		return 0.0f;
 }
	private void saveCredentials(@NonNull String login, @NonNull String passwd)
	{
		Validate.notNull(login);
		Validate.notNull(passwd);

		SharedPreferences.Editor editor = pref.edit();
		editor.putString(getString(R.string.loginKey), login);
		editor.putString(getString(R.string.passwdKey), passwd);
		editor.commit();
	}

}
