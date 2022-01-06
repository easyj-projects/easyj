/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.sdk.tencent.cloud.common.config;

/**
 * 腾讯云通用配置工具类
 *
 * @author wangliang181230
 */
public abstract class TencentCloudCommonConfigUtils {

	/**
	 * 将全局配置设置到目标配置为空的字段中
	 *
	 * @param targetConfig 目标功能配置
	 * @param globalConfig 全局配置
	 */
	public static void merge(TencentCloudCommonConfig targetConfig, TencentCloudCommonConfig globalConfig) {
		if (targetConfig.getSecretId() == null) {
			targetConfig.setSecretId(globalConfig.getSecretId());
		}
		if (targetConfig.getSecretKey() == null) {
			targetConfig.setSecretKey(globalConfig.getSecretKey());
		}
		if (targetConfig.getRegion() == null) {
			targetConfig.setRegion(globalConfig.getRegion());
		}
		if (targetConfig.getConnTimeout() == null) {
			targetConfig.setConnTimeout(globalConfig.getConnTimeout());
		}
		if (targetConfig.getWriteTimeout() == null) {
			targetConfig.setWriteTimeout(globalConfig.getWriteTimeout());
		}
		if (targetConfig.getReadTimeout() == null) {
			targetConfig.setReadTimeout(globalConfig.getReadTimeout());
		}
		if (targetConfig.getLanguage() == null) {
			targetConfig.setLanguage(globalConfig.getLanguage());
		}
		if (targetConfig.getDebug() == null) {
			targetConfig.setDebug(globalConfig.getDebug());
		}
	}
}
