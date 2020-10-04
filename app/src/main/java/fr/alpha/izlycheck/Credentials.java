package fr.alpha.izlycheck;

import android.content.Context;
import android.content.SharedPreferences;

public class Credentials
{
	public static String getLogin(Context context)
	{
		String loginKey = context.getString(R.string.loginKey);
		return getStringFromPreferences(loginKey);
	}

	public static String getPassword(Context context)
	{
		String passwordKey = context.getString(R.string.passwdKey);
		return getStringFromPreferences(passwordKey);
	}

	private static String getStringFromPreferences(String key)
	{
		SharedPreferences pref = MainActivity.getPreferences();
		return pref.getString(key, null);
	}
}
