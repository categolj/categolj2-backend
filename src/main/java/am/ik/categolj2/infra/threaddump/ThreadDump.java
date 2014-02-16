/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package am.ik.categolj2.infra.threaddump;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThreadDump {

	public static Map<String, ThreadDumpInfo> getThreadDump() {
		Map<String, ThreadDumpInfo> r = new LinkedHashMap<>();
		ThreadInfo[] data = getThreadInfos();
		ThreadGroupMap map = sortThreadsAndGetGroupMap(data);
		for (ThreadInfo ti : data) {
			r.put(ti.getThreadName(), dumpThreadInfo(ti, map));
		}
		return r;
	}

	public static ThreadGroupMap sortThreadsAndGetGroupMap(ThreadInfo[] list) {
		ThreadGroupMap sorter = new ThreadGroupMap();
		Arrays.sort(list, sorter);
		return sorter;
	}

	// ThreadInfo.toString() truncates the stack trace by first 8, so needed my
	// own version
	public static ThreadDumpInfo dumpThreadInfo(ThreadInfo ti,
			ThreadGroupMap map) {
		String grp = map.getThreadGroup(ti);
		String threadName = ti.getThreadName();
		long threadId = ti.getThreadId();
		String threadGroup = grp != null ? grp : "?";
		String threadState = null;
		{
			StringBuilder sb = new StringBuilder(ti.getThreadState().toString());
			if (ti.getLockName() != null) {
				sb.append(" on " + ti.getLockName());
			}
			if (ti.getLockOwnerName() != null) {
				sb.append(" owned by \"" + ti.getLockOwnerName() + "\" Id="
						+ ti.getLockOwnerId());
			}
			if (ti.isSuspended()) {
				sb.append(" (suspended)");
			}
			if (ti.isInNative()) {
				sb.append(" (in native)");
			}
			threadState = sb.toString();
		}
		String stackTraceString = null;
		{
			StringBuilder sb = new StringBuilder();
			StackTraceElement[] stackTrace = ti.getStackTrace();
			for (int i = 0; i < stackTrace.length; i++) {
				StackTraceElement ste = stackTrace[i];
				sb.append("\tat ").append(ste);
				sb.append('\n');
				if (i == 0 && ti.getLockInfo() != null) {
					Thread.State ts = ti.getThreadState();
					switch (ts) {
					case BLOCKED:
						sb.append("\t-  blocked on ").append(ti.getLockInfo());
						sb.append('\n');
						break;
					case WAITING:
						sb.append("\t-  waiting on ").append(ti.getLockInfo());
						sb.append('\n');
						break;
					case TIMED_WAITING:
						sb.append("\t-  waiting on ").append(ti.getLockInfo());
						sb.append('\n');
						break;
					default:
					}
				}

				for (MonitorInfo mi : ti.getLockedMonitors()) {
					if (mi.getLockedStackDepth() == i) {
						sb.append("\t-  locked ").append(mi);
						sb.append('\n');
					}
				}
			}
			LockInfo[] locks = ti.getLockedSynchronizers();
			if (locks.length > 0) {
				sb.append("\n\tNumber of locked synchronizers = "
						+ locks.length);
				sb.append('\n');
				for (LockInfo li : locks) {
					sb.append("\t- ").append(li);
					sb.append('\n');
				}
			}
			sb.append('\n');

			stackTraceString = sb.toString();
		}

		return new ThreadDumpInfo(threadName, threadId, threadGroup,
				threadState, stackTraceString);
	}

	public static ThreadInfo[] getThreadInfos() {
		ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
		return mbean.dumpAllThreads(mbean.isObjectMonitorUsageSupported(),

		mbean.isSynchronizerUsageSupported());
	}

	// Common code for sorting Threads/ThreadInfos by ThreadGroup
	private static class ThreadSorterBase {
		protected Map<Long, String> map = new HashMap<>();

		private ThreadSorterBase() {
			ThreadGroup tg = Thread.currentThread().getThreadGroup();
			while (tg.getParent() != null)
				tg = tg.getParent();
			Thread[] threads = new Thread[tg.activeCount() * 2];
			int threadsLen = tg.enumerate(threads, true);
			for (int i = 0; i < threadsLen; i++) {
				ThreadGroup group = threads[i].getThreadGroup();
				map.put(threads[i].getId(), group != null ? group.getName()
						: null);
			}
		}

		protected int compare(long idA, long idB) {
			String tga = map.get(idA), tgb = map.get(idB);
			int result = (tga != null ? -1 : 0) + (tgb != null ? 1 : 0); // Will
																			// be
																			// non-zero
																			// if
																			// only
																			// one
																			// is
																			// null
			if (result == 0 && tga != null)
				result = tga.compareToIgnoreCase(tgb);
			return result;
		}
	}

	public static class ThreadGroupMap extends ThreadSorterBase implements
			Comparator<ThreadInfo> {

		/**
		 * @return ThreadGroup name or null if unknown
		 */
		public String getThreadGroup(ThreadInfo ti) {
			return map.get(ti.getThreadId());
		}

		public int compare(ThreadInfo a, ThreadInfo b) {
			int result = compare(a.getThreadId(), b.getThreadId());
			if (result == 0)
				result = a.getThreadName().compareToIgnoreCase(
						b.getThreadName());
			return result;
		}
	}
}
