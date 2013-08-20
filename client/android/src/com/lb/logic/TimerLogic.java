package com.lb.logic;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;

public class TimerLogic extends LogicBase {
	
	public TimerLogic(Context context) {
		super(context);
	}

	public TimerTask create(Runnable task) {
		MyTimerTask myTimerTask = new MyTimerTask();
		myTimerTask.setTask(task);
		
		return myTimerTask;
	}
	
	public void start(TimerTask timerTask, long interval) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(timerTask, 0, interval);
	}
	
	public void cancel(TimerTask timerTask) {
		timerTask.cancel();
	}

	private class MyTimerTask extends TimerTask {
		private Handler handler;
		private Runnable task;

		public MyTimerTask() {
			handler = new Handler();
		}
		
		public void setTask(Runnable task) {
			this.task = task;
		}

		@Override
		public void run() {
			handler.post(task);
		}

	}
}
