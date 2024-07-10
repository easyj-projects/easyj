/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.spring.boot.logging.logback;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.CoreConstants;

/**
 * 在堆栈信息前后加一个标记行，更加方便观察。<br>
 * 主要用于控制台日志附加器（console-appender）和文件日志附加器（file-appender / async-file-appender）
 *
 * @author wangliang181230
 */
public class EasyjExtendedWhitespaceThrowableProxyConverter extends ExtendedThrowableProxyConverter {

	@Override
	protected String throwableProxyToString(IThrowableProxy tp) {
		return "==>" + CoreConstants.LINE_SEPARATOR + super.throwableProxyToString(tp)
				+ "<==" + CoreConstants.LINE_SEPARATOR + CoreConstants.LINE_SEPARATOR;
	}
}
