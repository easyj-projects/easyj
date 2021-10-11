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
package icu.easyj.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import cn.hutool.core.io.IORuntimeException;

import static org.springframework.util.SocketUtils.PORT_RANGE_MAX;
import static org.springframework.util.SocketUtils.PORT_RANGE_MIN;

/**
 * 网络工具类
 *
 * @author wangliang181230
 */
public abstract class NetUtils {

	//region IP

	/**
	 * 获取所有网卡IP列表
	 *
	 * @return 所有网卡IP列表
	 */
	public static List<String> getIpList() {
		List<String> result = new ArrayList<>();

		// 获取所有网卡，并依次获取IP
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> addresses = ni.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = addresses.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(':') == -1 && !"127.0.0.1".equals(ip.getHostAddress())) {
						result.add(ip.getHostAddress());
					}
				}
			}
		} catch (Exception ignore) {
			// do nothing
		}

		// 如果没有获取到，则获取默认的IP
		if (result.isEmpty()) {
			String ip = getIp();
			result.add(ip);
		}

		return result;
	}

	/**
	 * 获取当前主机的 IP地址（多个网卡时，自动取第一个）
	 * 注：该方法有时候会获取到127.0.0.1，不建议使用，建议使用getInnerIp();
	 *
	 * @return 本机IP
	 */
	public static String getIp() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress();
		} catch (UnknownHostException e) {
			throw new IORuntimeException("获取本机IP失败", e);
		}
	}

	//endregion


	//region Port

	//region 随机端口号（Random Port）

	/**
	 * 端口号范围字符串
	 */
	public static final String PORT_RANGE = PORT_RANGE_MIN + "," + PORT_RANGE_MAX;

	/**
	 * 随机生成端口号
	 *
	 * @param startPort 起始端口号
	 * @param endPort   截止端口号
	 * @return 随机端口号
	 */
	public static int randomPort(int startPort, int endPort) {
		if (endPort > PORT_RANGE_MAX || endPort < PORT_RANGE_MIN) {
			endPort = PORT_RANGE_MAX;
		}
		if (startPort < PORT_RANGE_MIN) {
			startPort = PORT_RANGE_MIN;
		}

		return org.apache.commons.lang3.RandomUtils.nextInt(startPort, endPort);
	}

	/**
	 * 随机生成端口号
	 *
	 * @param startPort 起始端口号
	 * @return 随机端口号
	 */
	public static int randomPort(int startPort) {
		return randomPort(startPort, PORT_RANGE_MAX);
	}

	/**
	 * 随机生成端口号
	 *
	 * @return 随机端口号
	 */
	public static int randomPort() {
		return randomPort(PORT_RANGE_MIN, PORT_RANGE_MAX);
	}

	//endregion

	//endregion
}
