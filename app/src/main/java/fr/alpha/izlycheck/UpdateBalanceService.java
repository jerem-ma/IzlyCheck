package fr.alpha.izlycheck;

import android.app.IntentService;
import android.content.Intent;

public class UpdateBalanceService extends IntentService
{
	private static final String LOG_TAG = "IzlyCheck";

	public UpdateBalanceService()
	{
		super("UpdateBalanceService");
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
		String login = Credentials.getLogin(this);
		String password = Credentials.getPassword(this);

		Balance updater = new Balance(login, password);
		return updater::updateBalanceAndLogErrors;
	}
}
