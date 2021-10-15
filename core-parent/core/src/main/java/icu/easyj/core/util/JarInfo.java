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

import java.net.URL;
import java.util.Objects;

import cn.hutool.core.lang.Assert;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Jar信息
 *
 * @author wangliang181230
 */
public class JarInfo {

	/**
	 * 路径
	 */
	private final URL url;

	/**
	 * Jar名
	 */
	private final String name;

	/**
	 * Jar版本号
	 */
	private final String version;

	/**
	 * Jar版本号的long型值
	 */
	private final long longVersion;


	/**
	 * 构造函数
	 *
	 * @param url     路径
	 * @param name    Jar名
	 * @param version Jar版本号
	 */
	public JarInfo(@NonNull URL url, @NonNull String name, @Nullable String version) {
		Assert.notNull(url, "'url' must not be null");
		Assert.isTrue(StringUtils.isNotBlank(name), "'name' must not be null");

		this.url = url;
		this.name = name;
		if (StringUtils.isBlank(version)) {
			this.version = VersionUtils.UNKNOWN_VERSION;
			this.longVersion = 0L;
		} else {
			this.version = version;
			this.longVersion = VersionUtils.toLong(version);
		}
	}

	/**
	 * 比较版本号
	 *
	 * @param otherVersion 其他版本号
	 * @return 比较结果：1=比other高、0=相同、-1=比other低
	 */
	public int compareToVersion(String otherVersion) {
		if (Objects.equals(this.version, otherVersion)) {
			return 0;
		} else if (this.version == null) {
			return -1;
		}

		long otherLongVersion = VersionUtils.toLong(otherVersion);
		if (this.longVersion == otherLongVersion) {
			return this.version.compareTo(otherVersion);
		} else if (this.longVersion > otherLongVersion) {
			return 1;
		} else {
			return 0;
		}
	}


	//region Getter

	@NonNull
	public URL getUrl() {
		return url;
	}

	@NonNull
	public String getName() {
		return name;
	}

	@NonNull
	public String getVersion() {
		return version;
	}

	public long getLongVersion() {
		return longVersion;
	}

	//endregion
}
