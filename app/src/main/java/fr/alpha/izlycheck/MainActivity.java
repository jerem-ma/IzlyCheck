package fr.alpha.izlycheck;

import org.apache.commons.lang3.Validate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		updateBalance();
	}

	public void clickSaveButton(View view){
		final EditText loginWidget = (EditText) findViewById(R.id.login);
		final EditText passwdWidget = (EditText) findViewById(R.id.passwd);
		final String login = loginWidget.getText().toString();
		final String passwd = passwdWidget.getText().toString();

		saveCredentials(login, passwd);
	}

	private String[] getCredentials(){
		final SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
		final String login = pref.getString(getString(R.string.loginKey), null);
		final String passwd = pref.getString(getString(R.string.passwdKey), null);

		final String[] credentials = {login, passwd};
		
		return credentials;
	}

	private void saveCredentials(@NonNull final String login, @NonNull final String passwd){
		Validate.notNull(login);
		Validate.notNull(passwd);

		final SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = pref.edit();
		editor.putString(getString(R.string.loginKey), login);
		editor.putString(getString(R.string.passwdKey), passwd);
		editor.commit();
	}

	private void updateBalance(){
		final float balance = getBalance();

		final TextView balanceView = (TextView) findViewById(R.id.balance);
		final String defaultText = getString(R.string.balance);
		final String balanceText = defaultText.replace("%balance%", String.valueOf(balance));

		balanceView.setText(balanceText);
	}

	private float getBalance(){
		return 0.0f;
	}

}
