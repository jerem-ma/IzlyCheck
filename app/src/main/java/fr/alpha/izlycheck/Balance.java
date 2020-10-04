package fr.alpha.izlycheck;

import java.io.IOException;
import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Balance
{
	private final String login;
	private final String password;

	public static float getSavedBalance()
	{
		float balance = MainActivity.getPreferences().getFloat("balance", 0.0f);

		return balance;
	}

	public Balance(String login, String password)
	{
		this.login = login;
		this.password = password;
	}

	public void updateBalance()
		throws BalanceNotFoundInPageException, IOException
	{
		float balance = getBalance();
		saveBalanceInPreferences(balance);
	}

	private float getBalance()
		throws IOException, BalanceNotFoundInPageException
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

		data.put("username", this.login);
		data.put("password", this.password);

		return data;
	}

	private void saveBalanceInPreferences(float balance)
	{
		SharedPreferences pref = MainActivity.getPreferences();
		SharedPreferences.Editor editor = pref.edit();
		editor.putFloat("balance", balance);
		editor.apply();
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
