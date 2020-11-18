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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends Activity
{
	private static SharedPreferences pref;

	public static SharedPreferences getPreferences()
	{
		return pref;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		registerEvents();

		setContentView(R.layout.activity_main);

		setPreferences();
		updateBalanceFrequently();
		refreshBalance();
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		Events.getEvent("balanceUpdated").register(this);
	}

	@Override
	protected void onStop()
	{
		Events.getEvent("balanceUpdated").unregister(this);
		super.onStop();
	}

	private void registerEvents()
	{
		Events.registerEvent("balanceUpdateAsked", new EventBus());
		Events.registerEvent("balanceUpdated", new EventBus());
	}

	@Subscribe
	public void onBalanceUpdate(BalanceUpdateEvent e)
	{
		refreshBalance();
	}

	private void setPreferences()
	{
		Context appContext = this.getApplicationContext();
		pref = appContext.getSharedPreferences("IzlyCheck", Context.MODE_PRIVATE);
	}

	private void updateBalanceFrequently()
	{
		Intent intent = new Intent(this, UpdateBalanceService.class);
		startService(intent);
	}

	private void refreshBalance()
	{
		float balance = Balance.getSavedBalance();
		setBalanceDisplayTo(balance);
	}

	private void setBalanceDisplayTo(float balance)
	{
		this.runOnUiThread(new Runnable()
		{
			public void run()
			{
				TextView balanceView = (TextView) findViewById(R.id.balance);
				String balanceDisplay = formatBalanceDisplay(balance);
				balanceView.setText(balanceDisplay);
			}
		});
	}

	private String formatBalanceDisplay(float balance)
	{
		String defaultText = getString(R.string.balance);
		String balanceString = String.valueOf(balance);
		String formatedBalance = defaultText.replace("%balance%", balanceString);

		return formatedBalance;
	}

	public void saveButtonClicked(View view)
	{
		EditText loginWidget = (EditText) findViewById(R.id.login);
		EditText passwdWidget = (EditText) findViewById(R.id.passwd);
		String login = loginWidget.getText().toString();
		String passwd = passwdWidget.getText().toString();

		saveCredentials(login, passwd);
		Events.getEvent("balanceUpdateAsked").post(new BalanceUpdateEvent());
	}

	private void saveCredentials(@NonNull String login, @NonNull String passwd)
	{
		Validate.notNull(login);
		Validate.notNull(passwd);

		SharedPreferences.Editor editor = pref.edit();
		editor.putString(getString(R.string.loginKey), login);
		editor.putString(getString(R.string.passwdKey), passwd);
		editor.apply();
	}

}
