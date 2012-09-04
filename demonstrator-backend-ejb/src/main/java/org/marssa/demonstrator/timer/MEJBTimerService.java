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
//package org.marssa.demonstrator.timer;
//
//import java.io.Serializable;
//import java.util.Collection;
//import java.util.Date;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.annotation.Resource;
//import javax.ejb.EJBException;
//import javax.ejb.ScheduleExpression;
//import javax.ejb.SessionContext;
//import javax.ejb.Singleton;
//import javax.ejb.Timer;
//import javax.ejb.TimerConfig;
//import javax.ejb.TimerService;
//import javax.enterprise.context.ApplicationScoped;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author Warren Zahra
// * 
// */
//@ApplicationScoped
//@Singleton
//public class MEJBTimerService implements TimerService {
//
//	private static final Logger logger = LoggerFactory
//			.getLogger(MEJBTimerService.class.getName());
//
//	private @Resource
//	SessionContext ctx;
//
//	public MEJBTimerService() {
//	}
//
//	@PostConstruct
//	private void init() {
//		logger.info("Initialized MEJBTimerService Bean");
//	}
//
//	@PreDestroy
//	private void destroy() {
//		logger.info("Destroyed MEJBTimerService Bean");
//	}
//
//	public void addSchedule(String taskName, Date date) {
//		ctx.getTimerService().createTimer(date, taskName);
//
//	}
//
//	public void addSchedule(String taskName, Date date, long period) {
//		ctx.getTimerService().createTimer(date, period, taskName);
//
//	}
//
//	public void addSchedule(String taskName, long delay) {
//		ctx.getTimerService().createTimer(delay, taskName);
//
//	}
//
//	// public void addSchedule(String taskName, long delay, long period) {
//	// ctx.getTimerService().createTimer(delay, period, taskName);
//	//
//	// }
//
//	@Override
//	public Collection<Timer> getTimers() {
//		return ctx.getTimerService().getTimers();
//	}
//
//	public void createCalenderTimer(ScheduleExpression schedule) {
//		ctx.getTimerService().createCalendarTimer(schedule);
//	}
//
//	public void createCalenderTimer(TimerConfig timerConfig,
//			ScheduleExpression schedule) {
//		ctx.getTimerService().createCalendarTimer(schedule, timerConfig);
//	}
//
//	public void cancel(String taskName) {
//		for (Timer t : ctx.getTimerService().getTimers()) {
//			if (t.getInfo().equals(taskName)) {
//				t.cancel();
//			}
//		}
//	}
//
//	@Override
//	public Timer createCalendarTimer(ScheduleExpression schedule)
//			throws IllegalArgumentException, IllegalStateException,
//			EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createCalendarTimer(ScheduleExpression schedule,
//			TimerConfig timerConfig) throws IllegalArgumentException,
//			IllegalStateException, EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createIntervalTimer(Date initialExpiration,
//			long intervalDuration, TimerConfig timerConfig)
//			throws IllegalArgumentException, IllegalStateException,
//			EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createIntervalTimer(long initialDuration,
//			long intervalDuration, TimerConfig timerConfig)
//			throws IllegalArgumentException, IllegalStateException,
//			EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createSingleActionTimer(Date expiration,
//			TimerConfig timerConfig) throws IllegalArgumentException,
//			IllegalStateException, EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createSingleActionTimer(long duration, TimerConfig timerConfig)
//			throws IllegalArgumentException, IllegalStateException,
//			EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createTimer(long duration, Serializable info)
//			throws IllegalArgumentException, IllegalStateException,
//			EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createTimer(long initialDuration, long intervalDuration,
//			Serializable info) throws IllegalArgumentException,
//			IllegalStateException, EJBException {
//		return ctx.getTimerService().createTimer(initialDuration,
//				intervalDuration, info);
//	}
//
//	@Override
//	public Timer createTimer(Date expiration, Serializable info)
//			throws IllegalArgumentException, IllegalStateException,
//			EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Timer createTimer(Date initialExpiration, long intervalDuration,
//			Serializable info) throws IllegalArgumentException,
//			IllegalStateException, EJBException {
//		// TODO Auto-generated method stub
//		return null;
//	}
// }
