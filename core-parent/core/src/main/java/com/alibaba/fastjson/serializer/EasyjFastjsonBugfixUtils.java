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
package com.alibaba.fastjson.serializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.regex.Pattern;

import icu.easyj.core.util.version.VersionInfo;
import icu.easyj.core.util.version.VersionUtils;

/**
 * Fastjson BUG修复工具
 *
 * @author wangliang181230
 */
public abstract class EasyjFastjsonBugfixUtils {

	/**
	 * 创建 {@link SerializeConfig} 实例，并修复BUG
	 *
	 * @return 修复BUG后的 {@link SerializeConfig} 实例
	 */
	public static SerializeConfig newSerializeConfig() {
		SerializeConfig serializeConfig = new SerializeConfig();
		handleSerializeConfig(serializeConfig);
		return serializeConfig;
	}

	/**
	 * 处理serializeConfig实例，修复BUG
	 * <p>
	 * fastjson自带的 PrimitiveArraySerializer、ListSerializer、CollectionCodec，<br>
	 * 没有对Long数据进行转字符串处理，待BUG修复前临时处理一下。
	 *
	 * @param serializeConfig 序列化配置
	 */
	public static void handleSerializeConfig(SerializeConfig serializeConfig) {
		serializeConfig.put(long[].class, EasyjPrimitiveArraySerializer.INSTANCE);
		serializeConfig.put(ArrayList.class, EasyjListSerializer.INSTANCE);
		serializeConfig.put(LinkedList.class, EasyjListSerializer.INSTANCE);
		serializeConfig.put(HashSet.class, EasyjCollectionCodec.INSTANCE);
		serializeConfig.put(LinkedHashSet.class, EasyjCollectionCodec.INSTANCE);
	}

	/**
	 * 恢复serializeConfig实例
	 * <p>
	 * 和 {@link #handleSerializeConfig(SerializeConfig)} 方法相反
	 *
	 * @param serializeConfig 序列化配置
	 */
	public static void recoverySerializeConfig(SerializeConfig serializeConfig) {
		serializeConfig.put(long[].class, PrimitiveArraySerializer.instance);
		serializeConfig.put(ArrayList.class, ListSerializer.instance);
		serializeConfig.put(LinkedList.class, ListSerializer.instance);
		serializeConfig.put(HashSet.class, CollectionCodec.instance);
		serializeConfig.put(LinkedHashSet.class, CollectionCodec.instance);
	}

	/**
	 * 判断是否为存在漏洞的版本
	 *
	 * @param version 版本号
	 * @return 是否为存在漏洞的版本
	 */
	public static boolean isLoopholeVersion(String version) {
		return isLoopholeVersion(VersionUtils.parse(version));
	}

	/**
	 * 判断是否为存在漏洞的版本
	 *
	 * @param versionInfo 版本信息
	 * @return 是否为存在漏洞的版本
	 */
	public static boolean isLoopholeVersion(VersionInfo versionInfo) {
		// 小于等于1.2.68版本，且不是最新的漏洞修复版本
		return versionInfo.compareTo("1.2.68") <= 0 && !isLatestSecVersion(versionInfo.getVersion());
	}

	/**
	 * 判断是否为最新的漏洞修复版本
	 *
	 * @param version 版本号
	 * @return 是否为漏洞修复版本
	 */
	public static boolean isLatestSecVersion(String version) {
		// 版本号格式如：1.2.68.sec10
		// 目前最新的漏洞修复版本（即sec后面的数字）为 10，所以正则里判断的是大于等于10的漏洞修复版本
		final Pattern pattern = Pattern.compile("^\\d+\\.\\d+\\.\\d+[\\._]sec[1-9]\\d+$");
		return pattern.matcher(version).matches();
	}
}
