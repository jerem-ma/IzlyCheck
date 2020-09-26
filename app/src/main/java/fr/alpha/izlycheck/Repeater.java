package fr.alpha.izlycheck;

import android.os.Handler;

public class Repeater{

	private static final Handler handler = new Handler();

	private final Runnable repeatingTask;

	public Repeater(Runnable task, long delay){
		repeatingTask = addAutoCallbackToTask(task, delay);
	}

	private Runnable addAutoCallbackToTask(Runnable task, long delay){
		Runnable repeatingTask = new Runnable(){
			@Override
			public void run(){
				handler.post(task);
				handler.postDelayed(this, delay);
			}
		};

		return repeatingTask;
	}

	public void start(){
		handler.post(repeatingTask);
	}

}
