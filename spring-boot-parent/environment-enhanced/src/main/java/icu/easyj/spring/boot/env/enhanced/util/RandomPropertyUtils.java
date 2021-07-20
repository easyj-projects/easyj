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
package icu.easyj.spring.boot.env.enhanced.util;

import java.util.concurrent.ThreadLocalRandom;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.NetUtil;
import icu.easyj.core.code.analysis.CodeAnalysisResult;
import icu.easyj.core.exception.ConfigurationException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import static org.springframework.util.SocketUtils.PORT_RANGE_MAX;
import static org.springframework.util.SocketUtils.PORT_RANGE_MIN;

/**
 * 随机内容配置工具类
 *
 * @author wangliang181230
 */
public abstract class RandomPropertyUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomPropertyUtils.class);

	/**
	 * 根据配置命令解析结果获取配置值
	 *
	 * @param result 配置命令解析结果
	 * @return value 配置值
	 */
	public static Object getProperty(String name, CodeAnalysisResult result) {
		Object value = null;

		try {
			if (result.getMethodName() == null && result.getFieldName() != null) {
				result.setMethodName(result.getFieldName());
				result.setFieldName(null);
			}

			// 如果方法名不存在，则返回32位UUID
			if (!StringUtils.hasLength(result.getMethodName())) {
				value = UUID.fastUUID().toString(true); // 32位 不带'-'字符 的UUID
				return value;
			}

			// 执行方法对应的逻辑
			Object[] parameters = result.getParameters();
			switch (result.getMethodName()) {
				// 随机字符串：UUID
				case "uuid32":
					value = UUID.fastUUID().toString(true); // 32位 不带'-'字符 的UUID
					break;
				case "uuid":
					value = UUID.fastUUID().toString(false); // 完整的 带'-'字符 的UUID
					break;

				// 随机端口
				case "port":
					Range<Integer> range = getRange(parameters, PORT_RANGE_MIN, PORT_RANGE_MAX);
					value = randomPort(range, name);
					break;

				// 随机数字
				case "short":
					Range<Integer> shortRange = getRange(parameters, 0, (int)Short.MAX_VALUE);
					value = (short)randomInt(shortRange);
					break;
				case "int":
					Range<Integer> intRange = getRange(parameters, 0, Integer.MAX_VALUE);
					value = randomInt(intRange);
					break;
				case "long":
					Range<Long> longRange = getRange(parameters, 0L, Long.MAX_VALUE);
					value = randomLong(longRange);
					break;

				// 随机选取参数中的一个值
				case "choose":
					if (ArrayUtils.isEmpty(result.getParameters())) {
						throw new ConfigurationException("随机选取函数的可选参数不能为空：${" + name + "}");
					}
					int length = result.getParameters().length;
					int index = ThreadLocalRandom.current().nextInt(length);
					return result.getParameters()[index];

				// 未知
				default:
					throw new ConfigurationException("不支持当前随机内容的生成，当前的配置：${" + name + "}");
			}

			return value;
		} finally {
			// 随机数比较特殊，在这里打印一下信息
			if (value != null && LOGGER.isInfoEnabled()) {
				LOGGER.info("函数式配置`${" + name + "}`的值: " + value);
			}
		}
	}

	private static <T extends Comparable<T>> Range<T> getRange(Object[] parameters, T defaultStart, T defaultEnd) {
		T start = (parameters != null && parameters.length > 0 ? (T)parameters[0] : defaultStart);
		if (start == null) {
			start = defaultStart;
		}

		T end = (parameters != null && parameters.length > 1 ? (T)parameters[1] : defaultEnd);
		if (end == null) {
			end = defaultEnd;
		}

		return Range.between(start, end);
	}

	/**
	 * 随机获取未被占用的端口号
	 *
	 * @param range 范围
	 * @return value
	 */
	public static int randomPort(Range<Integer> range, String name) {
		int minPort = range.getMinimum();
		int maxPort = range.getMaximum();

		try {
			return NetUtil.getUsableLocalPort(minPort, maxPort);
		} catch (UtilException e) {
			int tryCount = maxPort - minPort;
			throw new ConfigurationException("尝试了" + tryCount + "次从(" + minPort + "~" + maxPort + ")范围内获取随机端口号，但都被占用。" +
					"请检查本机端口使用情况，并修改随机端口范围配置！当前的配置：${" + name + "}");
		}
	}

	/**
	 * 随机 int
	 *
	 * @param range 范围
	 * @return value
	 */
	public static int randomInt(Range<Integer> range) {
		return range.getMinimum() + ThreadLocalRandom.current().nextInt(range.getMaximum() - range.getMinimum() + 1);
	}

	/**
	 * 随机 long
	 *
	 * @param range 范围
	 * @return value
	 */
	public static long randomLong(Range<Long> range) {
		long minLong = Long.parseLong(range.getMinimum().toString());
		long maxLong = Long.parseLong(range.getMaximum().toString());
		return minLong + ThreadLocalRandom.current().nextLong(maxLong - minLong + 1);
	}
}
