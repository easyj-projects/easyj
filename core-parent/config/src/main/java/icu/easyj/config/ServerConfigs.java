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
package icu.easyj.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import icu.easyj.core.util.NetUtils;
import icu.easyj.core.util.StringUtils;

/**
 * 服务端配置
 *
 * @author wangliang181230
 */
public class ServerConfigs {

	//region Fields

	/**
	 * 当前服务内网地址<br>
	 * 多网卡时，需要手动配置。
	 */
	private String host = NetUtils.getIp();

	/**
	 * 当前服务外网根地址
	 */
	private String domain;

	/**
	 * 当前服务工作ID
	 */
	private Long workerId;

	/**
	 * 当前服务所属数据中心ID
	 */
	private Long dataCenterId;

	/**
	 * 雪花算法
	 */
	private Snowflake snowflake;

	//endregion


	//region 全局配置持有者（设计模式-创建型模式-单例模式-枚举实现单例）

	private ServerConfigs() {
	}

	private enum SingletonHolder {
		// 单例
		INSTANCE;

		private final ServerConfigs configs = new ServerConfigs();

		public ServerConfigs getConfigs() {
			return configs;
		}
	}

	private static ServerConfigs getInstance() {
		return SingletonHolder.INSTANCE.getConfigs();
	}

	//endregion


	/**
	 * 初始化
	 */
	public static void init() {
		Long dataCenterId = getDataCenterId();
		Long workerId = getWorkerId();

		if (dataCenterId == null) {
			dataCenterId = IdUtil.getDataCenterId(~(-1L << 5));
			setDataCenterId(dataCenterId);
		}
		if (workerId == null) {
			workerId = IdUtil.getWorkerId(dataCenterId, ~(-1L << 5));
			setWorkerId(dataCenterId);
		}

		setSnowflake(new Snowflake(workerId, dataCenterId));
	}


	//region Getter、Setter

	public static String getHost() {
		return getInstance().host;
	}

	public static void setHost(String host) {
		getInstance().host = host;
	}

	public static String getDomain() {
		return getInstance().domain;
	}

	public static void setDomain(String domain) {
		if (domain != null) {
			domain = StringUtils.trimEnd(domain, '/');
		}
		getInstance().domain = domain;
	}

	/**
	 * 获取当前服务工作ID
	 *
	 * @return 当前服务工作ID
	 */
	public static Long getWorkerId() {
		return getInstance().workerId;
	}

	/**
	 * 设置当前服务工作ID
	 *
	 * @param workerId 当前服务工作ID
	 */
	public static void setWorkerId(Long workerId) {
		getInstance().workerId = workerId;
	}

	/**
	 * 获取当前服务所属数据中心ID
	 *
	 * @return 当前服务所属数据中心ID
	 */
	public static Long getDataCenterId() {
		return getInstance().dataCenterId;
	}

	/**
	 * 设置当前服务所属数据中心ID
	 *
	 * @param dataCenterId 当前服务所属数据中心ID
	 */
	public static void setDataCenterId(Long dataCenterId) {
		getInstance().dataCenterId = dataCenterId;
	}


	/**
	 * 获取雪花算法
	 *
	 * @return 雪花算法
	 */
	public static Snowflake getSnowflake() {
		return getInstance().snowflake;
	}

	/**
	 * 设置雪花算法
	 *
	 * @param snowflake 雪花算法
	 */
	public static void setSnowflake(Snowflake snowflake) {
		getInstance().snowflake = snowflake;
	}

	//endregion
}
