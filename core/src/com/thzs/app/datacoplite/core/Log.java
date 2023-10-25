package com.thzs.app.datacoplite.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;
import java.util.List;
//import com.rpsg.rpg.object.game.ConsoleFromType;

/**
 * GDX-RPG 日志工具类
 */
public class Log {
	public abstract static class LogListener{
		public abstract void newLog(LogLine log);
	}
	//it is used to prevent some situation that other objects want to record logs
	public static LogListener Listener;
	public static class LogLine{
		enum LogType {
			LogInfo,LogDebug,LogWarning,LogError
		}
		public LogType type;
		public Color color;
		public String message;
		public LogLine(LogType type, Color color, String message){
			this.type = type;
			this.color = color;
			this.message = message;
		}
		public LogLine(LogType type, String color, String message){
			this.type = type;
			this.color = Color.valueOf(color);
			this.message = message;
		}
		public LogLine(LogType type, String message){
			this(type,getDefaultColor(type),message);
		}
		public static Color getDefaultColor(LogType type){
			switch(type){
				case LogInfo: return Color.WHITE;
				case LogDebug: return Color.GOLD;
				case LogWarning: return Color.YELLOW;
				case LogError: return Color.RED;
				default : return Color.SKY;
			}
		}
	}
	public static List<LogLine> logs= new ArrayList<>();
	private static Logger ilogger = new Logger("RealControl[I]", Logger.INFO);
	private static Logger dlogger = new Logger("RealControl[D]", Logger.DEBUG);
	private static Logger elogger = new Logger("RealControl[E]", Logger.ERROR);

	public static void c(Object obj,Color color){
		ilogger.info(obj == null ? "[null]" : obj.toString());
		LogLine log=new LogLine(LogLine.LogType.LogInfo,color,obj == null ? "[null]" : obj.toString());
		logs.add(log);
		if(Listener!=null){
			Listener.newLog(log);
		}
	}
	/**添加一条 <b>消息</b> 等级的日志*/
	public static void i(Object obj){
		ilogger.info(obj == null ? "[null]" : obj.toString());
		LogLine log=new LogLine(LogLine.LogType.LogInfo,obj == null ? "[null]" : obj.toString());
		logs.add(log);
		if(Listener!=null){
			Listener.newLog(log);
		}
		//Console.send(ConsoleFromType.LogInfo, obj == null ? "[null]" : obj.toString(), "ffffff",false);
	}
	
	/**添加一条 <b>调试</b> 等级的日志*/
	public static void d(Object obj){
		dlogger.debug(obj == null ? "[null]" : obj.toString());
		LogLine log=new LogLine(LogLine.LogType.LogDebug,obj == null ? "[null]" : obj.toString());
		logs.add(log);
		if(Listener!=null){
			Listener.newLog(log);
		}
		//Console.send(ConsoleFromType.LogDebug, obj == null ? "[null]" : obj.toString(), "ffffff", false);
	}
	
	/**添加一条 <b>错误</b> 等级的日志*/
	public static void e(Object obj){
		elogger.error(obj == null ? "[null]" : obj.toString());
		LogLine log=new LogLine(LogLine.LogType.LogError,obj == null ? "[null]" : obj.toString());
		logs.add(log);
		if(Listener!=null){
			Listener.newLog(log);
		}
		//Console.send(ConsoleFromType.LogError, obj == null ? "[null]" : obj.toString(), "ff1818", false);
	}
	
	/**添加一条 <b>错误</b> 等级的日志*/
	public static void e(Object obj, Throwable error){
		elogger.error(obj == null ? "[null]" : obj.toString(), error);

		String text = obj == null ? "[null]" : obj.toString();
		text += "\n";
		text += "[#ff00ff]" + error.getLocalizedMessage() + "[]";
		for(StackTraceElement ele : error.getStackTrace())
			text += "\n      [#fc2929]at " + ele.getClassName() + "." + ele.getMethodName() + ":[][#8493ff]" + ele.getLineNumber() + "[]";

		LogLine log=new LogLine(LogLine.LogType.LogError,text);
		logs.add(log);
		if(Listener!=null){
			Listener.newLog(log);
		}
		//Console.send(ConsoleFromType.LogError, text, null, false);
	}
}
