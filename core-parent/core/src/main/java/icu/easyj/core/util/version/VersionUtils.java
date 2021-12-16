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
package icu.easyj.core.util.version;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * 版本号工具类
 *
 * @author wangliang181230
 */
public abstract class VersionUtils {

	/**
	 * 未知版本号
	 */
	public static final String UNKNOWN_VERSION = "<unknown>";

	/**
	 * 未知版本号长整形值
	 */
	public static final long UNKNOWN_VERSION_LONG = 0L;

	/**
	 * 快照版本号后缀
	 */
	public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

	/**
	 * 最大支持几部分版本号
	 */
	private static final int DEFAULT_MAX_PART_SIZE = 5;

	/**
	 * 每部分版本号占位长度
	 */
	private static final int DEFAULT_ONE_PART_LENGTH = 3;


	/**
	 * 解析版本号，获取版本信息
	 *
	 * @param version 版本号
	 * @return 版本信息
	 */
	@NonNull
	public static VersionInfo parse(String version) {
		return new VersionInfo(version);
	}

	/**
	 * 将字符串版本号转换为long型版本号
	 *
	 * @param version       字符串版本号
	 * @param maxPartSize   最大部分数
	 * @param onePartLength 每部分长度
	 * @return long版本号
	 * @throws IncompatibleVersionException 不兼容的版本格式
	 */
	public static long toLong(String version, int maxPartSize, int onePartLength) throws IncompatibleVersionException {
		if (isUnknownVersion(version)) {
			return UNKNOWN_VERSION_LONG;
		}

		// 判断是否为快照版本
		boolean isSnapshotVersion = isSnapshotVersion(version);

		// 移除 '-' 及其后面的所有字符
		if (isSnapshotVersion || icu.easyj.core.util.StringUtils.contains(version, '-')) {
			version = version.substring(0, version.indexOf('-'));
		}

		// 获取所有部分
		String[] parts = StringUtils.split(version.replace('_', '.'), '.');
		if (parts.length > maxPartSize) {
			throw new IncompatibleVersionException("当前版本号部分数 [" + parts.length + "] 超过了最大值 [" + maxPartSize + "]，不兼容的版本号: " + version);
		}

		// 准备计算版本号长整形值
		long versionLong = 0L;
		int i = 1;
		for (String part : parts) {
			if (StringUtils.isNumeric(part)) {
				versionLong += calculatePartValue(part, i, maxPartSize, onePartLength);
			}
			i++;
		}

		// 发布版本个位数为1，快照版本个位数为0
		versionLong *= 10;
		if (!isSnapshotVersion) {
			versionLong++;
		}

		return versionLong;
	}

	/**
	 * 将字符串版本号转换为long型版本号
	 *
	 * @param version 字符串版本号
	 * @return long版本号
	 * @throws IncompatibleVersionException 不兼容的版本格式
	 */
	public static long toLong(String version) {
		return toLong(version, DEFAULT_MAX_PART_SIZE, DEFAULT_ONE_PART_LENGTH);
	}

	/**
	 * 判断是否为未知版本号
	 *
	 * @param version 版本号
	 * @return true=是 | false=否
	 */
	public static boolean isUnknownVersion(String version) {
		return StringUtils.isBlank(version) || version.toLowerCase().contains("unknown");
	}

	/**
	 * 比较版本号
	 *
	 * @param versionA 版本号A
	 * @param versionB 版本号B
	 * @return 小于0时，A小于B | 等于0时，A相等B | 大于0时，A大于B
	 */
	public static int compare(String versionA, String versionB) {
		VersionInfo versionInfo = parse(versionA);
		return versionInfo.compareTo(versionB);
	}

	/**
	 * 判断是否为快照版本
	 *
	 * @param version 版本号
	 * @return true=是 | false=否
	 */
	public static boolean isSnapshotVersion(String version) {
		return version != null && version.endsWith(SNAPSHOT_SUFFIX);
	}


	//region Private 计算版本号某部分的整形值

	/**
	 * 计算当前版本号部分的值
	 *
	 * @param partNumeric   当前部分的版本号字符串
	 * @param partIndex     当前部分的index
	 * @param maxPartSize   最大部分数
	 * @param onePartLength 每部分长度
	 * @return 当前版本号部分的值
	 */
	private static long calculatePartValue(String partNumeric, int partIndex, int maxPartSize, int onePartLength) {
		if (icu.easyj.core.util.StringUtils.isAllZero(partNumeric)) {
			return 0;
		} else {
			return Long.parseLong(partNumeric) * Double.valueOf(Math.pow(Math.pow(10, onePartLength), maxPartSize - partIndex)).longValue();
		}
	}

	//endregion
}
