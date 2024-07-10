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
package icu.easyj.spring.boot.autoconfigure.configs;

import icu.easyj.config.ServerConfigs;
import icu.easyj.core.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * EasyJ定义的服务端配置
 *
 * @author wangliang181230
 * @see ServerConfigs
 */
public class ServerProperties implements InitializingBean {

	/**
	 * 当前服务内网地址<br>
	 * 多网卡时，需要手动配置。
	 */
	private String host;

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


	@Override
	public void afterPropertiesSet() {
		if (StringUtils.isNotBlank(host)) {
			ServerConfigs.setHost(host);
		}
		if (StringUtils.isNotBlank(domain)) {
			ServerConfigs.setDomain(domain);
		}
		if (workerId != null) {
			ServerConfigs.setWorkerId(workerId);
		}
		if (dataCenterId != null) {
			ServerConfigs.setDataCenterId(dataCenterId);
		}

		// 初始化雪花算法
		ServerConfigs.init();
	}


	//region Getter、Setter

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	public Long getDataCenterId() {
		return dataCenterId;
	}

	public void setDataCenterId(Long dataCenterId) {
		this.dataCenterId = dataCenterId;
	}

	//endregion
}
