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
package icu.easyj.spring.boot.autoconfigure.loopholecheck;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 漏洞检测相关配置
 *
 * @author wangliang181230
 */
@ConfigurationProperties("easyj.loophole-check")
public class LoopholeCheckProperties {

	/**
	 * 如果存在漏洞，是否抛出异常。
	 */
	private boolean needThrowIfExist = true;


	public boolean isNeedThrowIfExist() {
		return needThrowIfExist;
	}

	public LoopholeCheckProperties setNeedThrowIfExist(boolean needThrowIfExist) {
		this.needThrowIfExist = needThrowIfExist;
		return this;
	}
}