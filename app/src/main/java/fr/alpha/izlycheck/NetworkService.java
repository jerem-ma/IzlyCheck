package fr.alpha.izlycheck;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.IntentService;
import android.content.Intent;
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
		return this::updateBalance;
	}

	private void updateBalance()
	{
		try
		{
			tryToUpdateBalance();
		}
		catch (BalanceNotFoundInPageException | ConnectionFailedException e)
		{
			logException(e);
		}
	}

	private void tryToUpdateBalance()
		throws BalanceNotFoundInPageException, ConnectionFailedException
	{
		String login = Credentials.getLogin(this);
		String password = Credentials.getPassword(this);

		Balance balanceUpdater = new Balance(login, password);
		balanceUpdater.updateBalance();
	}


	private void logException(Exception e)
	{
		StringWriter stackTrace = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTrace));
		Log.e(LOG_TAG, stackTrace.toString());
	}

}
