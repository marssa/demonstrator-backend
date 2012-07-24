package org.marssa.demonstrator.timer;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import org.marssa.services.scheduling.ITimerService;
import org.marssa.services.scheduling.MTimerTask;

/**
 * @author Warren Zahra
 * 
 */
@Singleton
public class MEJBTimerService implements ITimerService {

	private @Resource
	SessionContext ctx;

	public MEJBTimerService() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.marssa.footprint.interfaces.Timer.ITimer#schedule(java.util.Date)
	 */
	@Override
	public void addSchedule(MTimerTask task, Date date) {
		ctx.getTimerService().createTimer(date, task.getTaskName().toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.marssa.footprint.interfaces.Timer.ITimer#schedule(java.util.Date,
	 * long)
	 */
	@Override
	public void addSchedule(MTimerTask task, Date date, long period) {
		ctx.getTimerService().createTimer(date, period,
				task.getTaskName().toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marssa.footprint.interfaces.Timer.ITimer#schedule(long)
	 */
	@Override
	public void addSchedule(MTimerTask task, long delay) {
		ctx.getTimerService().createTimer(delay, task.getTaskName().toString());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.marssa.footprint.interfaces.Timer.ITimer#schedule(long, long)
	 */
	@Override
	public void addSchedule(MTimerTask task, long delay, long period) {
		ctx.getTimerService().createTimer(delay, period,
				task.getTaskName().toString());

	}

	// @Override
	public Collection<Timer> getTimers() {
		return ctx.getTimerService().getTimers();
	}

	public void createCalenderTimer(ScheduleExpression schedule) {
		ctx.getTimerService().createCalendarTimer(schedule);
	}

	public void createCalenderTimer(TimerConfig timerConfig,
			ScheduleExpression schedule) {
		ctx.getTimerService().createCalendarTimer(schedule, timerConfig);
	}
}
