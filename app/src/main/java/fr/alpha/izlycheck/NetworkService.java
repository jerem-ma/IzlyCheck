package fr.alpha.izlycheck;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class NetworkService extends IntentService
{
	private static final String LOG_TAG = "IzlyCheck";

	public NetworkService()
	{
		super("NetworkService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		updateBalanceFrequently();
	}

	private void updateBalanceFrequently()
	{
		Runnable balanceUpdater = getBalanceUpdater();
		Repeater updaterLoop = new Repeater(balanceUpdater, 60*60*1000);
		updaterLoop.start();
	}

	private Runnable getBalanceUpdater()
	{
		Runnable task = new Runnable()
		{
			@Override
			public void run()
			{
				updateBalance();
			}
		};

		return task;
	}

	private void updateBalance()
	{
		try
		{
			tryToUpdateBalance();
		}
		catch (BalanceNotFoundInPageException | IOException e)
		{
			logException(e);
		}
	}

	private void tryToUpdateBalance()
		throws BalanceNotFoundInPageException, IOException
	{
		float balance = getBalance();
		saveBalanceInPreferences(balance);
	}

	private float getBalance() throws IOException, BalanceNotFoundInPageException
	{
		String page = getIzlyPage();
		float balance = extractBalanceFromPage(page);

		return balance;
	}

	private String getIzlyPage() throws IOException
	{
		String websiteLink = "https://mon-espace.izly.fr/Home/Logon";
		Map<String, String> credentials = getData();

		EasyHttpURLConnection connection = new EasyHttpURLConnection
		(
			websiteLink,
			credentials
		);

		String page = connection.getPage();
		connection.disconnect();

		return page;
	}

	private Map<String, String> getData()
	{
		Map<String, String> data = new HashMap<String, String>();
		String login = getLogin();
		String password = getPassword();

		data.put("username", login);
		data.put("password", password);

		return data;
	}

	private String getLogin()
	{
		String loginKey = getString(R.string.loginKey);
		return getStringFromPreferences(loginKey);
	}

	private String getPassword()
	{
		String passwordKey = getString(R.string.passwdKey);
		return getStringFromPreferences(passwordKey);
	}

	private String getStringFromPreferences(String key)
	{
		SharedPreferences pref = MainActivity.getPreferences();
		return pref.getString(key, null);
	}


	private void saveBalanceInPreferences(float balance)
	{
		SharedPreferences pref = MainActivity.getPreferences();
		SharedPreferences.Editor editor = pref.edit();
		editor.putFloat("balance", balance);
		editor.commit();
	}

	private void logException(Exception e)
	{
		StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		Log.e(LOG_TAG, stackTrace.toString());
	}

	private float extractBalanceFromPage(String page)
		throws BalanceNotFoundInPageException
	{
		Pattern pattern = Pattern.compile("(\\d+\\.\\d+)<em>\\s€");
		Matcher matcher = pattern.matcher(page);
		float balance;

		if (matcher.find())
			balance = Float.valueOf(matcher.group(1));
		else
			throw new BalanceNotFoundInPageException();

		return balance;
	}
}
