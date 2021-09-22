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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import icu.easyj.core.code.analysis.CodeAnalysisResult;
import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.NetUtils;
import icu.easyj.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PatternMatchUtils;

/**
 * 本地IP配置工具类
 *
 * @author wangliang181230
 */
public abstract class LocalIpPropertyUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocalIpPropertyUtils.class);

	public static final String[] DEFAULT_PATTERN = new String[]{"192.168.*.*"};

	/**
	 * 根据配置命令解析结果获取配置值
	 *
	 * @param name   配置键
	 * @param result 配置命令解析结果
	 * @return value 配置值
	 */
	public static String getProperty(String name, CodeAnalysisResult result) {
		if (StringUtils.isBlank(result.getMethodName())) {
			String localIp = getFirstIp();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("函数式配置`${" + name + "}`的值: " + localIp);
			}
			return localIp;
		}

		String localIp = null;
		try {
			// 执行方法对应的逻辑
			Object[] parameters = result.getParameters();
			switch (result.getMethodName()) {
				case "pattern":
					// 获取匹配串
					String[] patterns = new String[parameters.length];
					if (patterns.length == 0) {
						patterns = DEFAULT_PATTERN;
					} else {
						Object param;
						for (int i = 0, l = parameters.length; i < l; ++i) {
							param = parameters[i];
							if (param != null) {
								patterns[i] = param.toString();
							}
						}
					}

					localIp = getLocalIpByPattern(patterns);
					return localIp;
				default:
					localIp = getFirstIp();
					return localIp;
			}
		} finally {
			if (localIp != null && LOGGER.isInfoEnabled()) {
				LOGGER.info("函数式配置`${" + name + "}`的值: " + localIp);
			}
		}
	}


	//**********************************************************************************************/

	/**
	 * 获取第一张网卡IP
	 *
	 * @return localIp
	 */
	public static String getFirstIp() {
		List<String> ipList = NetUtils.getIpList();

		if (CollectionUtils.isEmpty(ipList)) {
			String ip = "127.0.0.1";
			LOGGER.warn("网卡IP列表为空，使用本地IP：{}", ip);
			return ip;
		}

		return ipList.get(0);
	}

	/**
	 * 匹配本地IP
	 *
	 * @param patterns 匹配串列表
	 * @return localIp
	 */
	public static String getLocalIpByPattern(String[] patterns) {
		List<String> ipList = NetUtils.getIpList();

		String ip;
		if (CollectionUtils.isEmpty(ipList)) {
			ip = "127.0.0.1";
			LOGGER.warn("网卡IP列表为空，使用本地IP：{}", ip);
			return ip;
		}

		// 先根据入参获取配置
		for (String pattern : patterns) {
			if (StringUtils.isNotEmpty(pattern)) {
				ip = matchIp(ipList, pattern);
				if (StringUtils.isNotBlank(ip)) {
					return ip;
				}
			}
		}

		// 使用 192.* 匹配
		String pattern = "192.*";
		ip = matchIp(ipList, pattern);
		if (StringUtils.isNotBlank(ip)) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("根据匹配串 {} 未匹配到任何网卡IP，现使用匹配串 [{}] 进行匹配，并匹配到了IP：{}",
						icu.easyj.core.util.StringUtils.toString(patterns), pattern, ip);
			}
			return ip;
		} else {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("根据匹配串 {} 未匹配到任何网卡IP，现使用匹配串 [{}] 进行匹配。",
						icu.easyj.core.util.StringUtils.toString(patterns), pattern);
			}
		}

		// 都未匹配到，返回第一个IP地址
		ip = ipList.get(0);
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn("根据匹配串 [{}] 也未匹配到任何网卡IP，直接获取第一张网卡IP：{}", pattern, ip);
		}
		return ip;
	}

	/**
	 * 从IP列表中匹配一个IP
	 *
	 * @param ipList  IP列表
	 * @param pattern 匹配串
	 * @return ip 返回匹配到的IP地址
	 */
	public static String matchIp(List<String> ipList, String pattern) {
		// 正则匹配
		if (pattern.startsWith("^")) {
			Pattern p = Pattern.compile(pattern, Pattern.UNICODE_CASE);
			Matcher matcher;
			for (String ip : ipList) {
				matcher = p.matcher(ip);
				if (matcher.matches()) {
					return ip;
				}
			}
		}
		// 通配符（包括：*, ?）匹配
		else {
			for (String ip : ipList) {
				if (PatternMatchUtils.simpleMatch(pattern, ip)) {
					return ip;
				}
			}
		}

		return null;
	}
}
