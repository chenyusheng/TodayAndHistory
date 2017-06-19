package ysheng.todayandhistory.Util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import ysheng.todayandhistory.MyApplication;

public class Debug_AdLog {

	private static String mTag = "shengBro";
	private static boolean mIsEnableLog = true;

	private static Context context = MyApplication.getInstance()
			.getApplicationContext();

	public static void setAdLogEnabled(boolean flag) {
		mIsEnableLog = flag;
	}

	public static void setAdLogTag(String tag) {
		mTag = tag;
	}

	private static void printLineInfo(String tag) {
		if (mIsEnableLog) {
			try {
				StackTraceElement[] elements = Thread.currentThread()
						.getStackTrace();
				if (elements.length > 5) {

					StackTraceElement element = elements[5];

					@SuppressLint("DefaultLocale") String txt = String
							.format("Time:[ %d ] Thread:[ %d ] Method:( %s ) FileName:[ %s ] Line:( %d )",
									System.currentTimeMillis(), Thread
											.currentThread().getId(), element
											.getMethodName(), element
											.getFileName(), element
											.getLineNumber());
					Debug_AdLog.ti(tag, txt);
				}
			} catch (Throwable e) {
			}
		}
	}

	// Info
	public static void ti(String tag, String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.INFO, tag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void ti(String tag, String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.INFO, tag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	public static void i(String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.INFO, mTag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void i(Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.INFO, mTag, "", tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void i(String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.INFO, mTag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	// Error
	public static void te(String tag, String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.ERROR, tag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void te(String tag, String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.ERROR, tag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	public static void e(String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {

				taglog(Log.ERROR, mTag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void e(Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.ERROR, mTag, "", tr);

			} catch (Throwable e) {
			}
		}


	}

	public static void e(String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.ERROR, mTag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	// warn
	public static void tw(String tag, String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.WARN, tag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void tw(String tag, String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.WARN, tag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	public static void w(String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.WARN, mTag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void w(Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.WARN, mTag, "", tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void w(String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.WARN, mTag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	// debug
	public static void td(String tag, String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.DEBUG, tag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void td(String tag, String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.DEBUG, tag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	public static void d(String msg, Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.DEBUG, mTag, msg, tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void d(Throwable tr) {
		if (mIsEnableLog) {
			try {
				taglog(Log.DEBUG, mTag, "", tr);
			} catch (Throwable e) {
			}
		}
	}

	public static void d(String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				taglog(Log.DEBUG, mTag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	public static void debugTagLog(int level, String tag, String fmt,
			Object... args) {
		if (mIsEnableLog) {
			try {
				printLineInfo(tag);
				taglog(level, tag, fmt, args);
			} catch (Throwable e) {
			}
		}
	}

	public static void taglog(int level, String tag, String fmt, Object... args) {
		if (mIsEnableLog) {
			try {
				String msg = String.format(fmt, args);
				switch (level) {
				case Log.DEBUG:
					Log.d(tag, msg);
					break;
				case Log.ERROR:
					Log.e(tag, msg);
					break;
				case Log.INFO:
					Log.i(tag, msg);
					break;
				case Log.WARN:
					Log.w(tag, msg);
					break;
				default:
					Log.v(tag, msg);
					break;
				}
			} catch (Throwable e) {
			}
		}
	}

	public static void debugTagLog(int level, String tag, String msg,
			Throwable err) {
		if (mIsEnableLog) {
			try {
				printLineInfo(tag);
				taglog(level, tag, msg, err);
			} catch (Exception e) {
			}
		}
	}

	public static void taglog(int level, String tag, String msg, Throwable err) {
		if (mIsEnableLog) {
			try {

				switch (level) {
				case Log.DEBUG:
					Log.d(tag, msg, err);
					break;
				case Log.ERROR:
					Log.e(tag, msg, err);
					break;
				case Log.INFO:
					Log.i(tag, msg, err);
					break;
				case Log.WARN:
					Log.w(tag, msg, err);
					break;
				default:
					Log.v(tag, msg, err);
					break;
				}
			} catch (Throwable e) {
			}
		}
	}
}
