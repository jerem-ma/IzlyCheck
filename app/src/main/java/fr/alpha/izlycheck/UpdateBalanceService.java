package fr.alpha.izlycheck;

import android.app.IntentService;
import android.content.Intent;

public class UpdateBalanceService extends IntentService
{
	public UpdateBalanceService()
	{
		super("UpdateBalanceService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		updateBalance();
	}

	private void updateBalance()
	{
		String login = Credentials.getLogin(this);
		String password = Credentials.getPassword(this);

		Balance updater = new Balance(login, password);
		updater.updateBalanceAndLogErrors();
	}
}
