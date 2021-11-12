/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.core.logging;

import java.util.function.Supplier;

import icu.easyj.core.loader.EnhancedServiceLoader;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.springframework.lang.NonNull;

/**
 * 增强日志记录器
 *
 * @author wangliang181230
 */
public class EnhancedLogger extends LoggerAdapter {

	public EnhancedLogger(Logger logger) {
		super(logger);
	}


	//region trace

	@Override
	public void trace(String format, Object arg) {
		super.trace(format, wrapArg(arg));
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		super.trace(format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void trace(String format, Object... arguments) {
		super.trace(format, wrapArguments(arguments));
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		super.trace(marker, format, wrapArg(arg));
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		super.trace(marker, format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		super.trace(marker, format, wrapArguments(argArray));
	}

	//endregion


	//region debug

	@Override
	public void debug(String format, Object arg) {
		super.debug(format, wrapArg(arg));
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		super.debug(format, arg1, wrapArg(arg2));
	}

	@Override
	public void debug(String format, Object... arguments) {
		super.debug(format, wrapArguments(arguments));
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		super.debug(marker, format, wrapArg(arg));
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		super.debug(marker, format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void debug(Marker marker, String format, Object... arguments) {
		super.debug(marker, format, wrapArguments(arguments));
	}

	//endregion


	//region info

	@Override
	public void info(String format, Object arg) {
		super.info(format, wrapArg(arg));
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		super.info(format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void info(String format, Object... arguments) {
		super.info(format, wrapArguments(arguments));
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		super.info(marker, format, wrapArg(arg));
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		super.info(marker, format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void info(Marker marker, String format, Object... arguments) {
		super.info(marker, format, wrapArguments(arguments));
	}

	//endregion


	//region warn

	@Override
	public void warn(String format, Object arg) {
		super.warn(format, wrapArg(arg));
	}

	@Override
	public void warn(String format, Object... arguments) {
		super.warn(format, wrapArguments(arguments));
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		super.warn(format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		super.warn(marker, format, wrapArg(arg));
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		super.warn(marker, format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void warn(Marker marker, String format, Object... arguments) {
		super.warn(marker, format, wrapArguments(arguments));
	}

	//endregion


	//region error

	@Override
	public void error(String format, Object arg) {
		super.error(format, wrapArg(arg));
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		super.error(format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void error(String format, Object... arguments) {
		super.error(format, wrapArguments(arguments));
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		super.error(marker, format, wrapArg(arg));
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		super.error(marker, format, wrapArg(arg1), wrapArg(arg2));
	}

	@Override
	public void error(Marker marker, String format, Object... arguments) {
		super.error(marker, format, wrapArguments(arguments));
	}

	//endregion


	//region 包装日志

	/**
	 * 包装需要包装的日志参数
	 *
	 * @param arguments 日志参数数组
	 */
	private static Object[] wrapArguments(@NonNull Object[] arguments) {
		for (int i = 0; i < arguments.length; ++i) {
			arguments[i] = wrapArg(arguments[i]);
		}
		return arguments;
	}

	/**
	 * 包装需要包装的日志参数
	 *
	 * @param arg 日志参数
	 */
	private static Object wrapArg(@NonNull Object arg) {
		if (arg instanceof Supplier<?>) {
			return LogParamWrapper.wrap((Supplier<?>)arg);
		} else {
			return arg;
		}
	}

	/**
	 * 日志参数包装类
	 */
	private static class LogParamWrapper {

		private static final IToStringService TO_STRING_SERVICE = EnhancedServiceLoader.load(IToStringService.class);


		private final Supplier<?> supplier;


		public LogParamWrapper(Supplier<?> supplier) {
			this.supplier = supplier;
		}


		@Override
		public String toString() {
			if (supplier != null) {
				Object logParam = supplier.get();
				if (logParam instanceof CharSequence) {
					return logParam.toString();
				} else {
					return TO_STRING_SERVICE.toString(logParam);
				}
			} else {
				return null;
			}
		}


		/**
		 * 包装日志参数，最终使用toString方法输出字符串
		 * <p>
		 * 作用：减少同步执行的性能损耗
		 *
		 * @param logParamSupplier 日志参数提供者
		 * @return 日志参数包装对象
		 */
		public static LogParamWrapper wrap(Supplier<?> logParamSupplier) {
			return new LogParamWrapper(logParamSupplier);
		}
	}

	//endregion
}
