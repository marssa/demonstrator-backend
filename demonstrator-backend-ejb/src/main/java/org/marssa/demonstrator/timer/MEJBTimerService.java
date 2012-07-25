/**
 * Copyright 2012 MARSEC-XL International Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
