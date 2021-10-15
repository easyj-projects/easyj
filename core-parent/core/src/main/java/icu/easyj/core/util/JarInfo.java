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
	 * 构造函数
	 *
	 * @param url     路径
	 * @param name    Jar名
	 * @param version Jar版本号
	 */
	public JarInfo(@NonNull URL url, @NonNull String name, @Nullable String version) {
		Assert.notNull(url, "'url' must not be null");
		Assert.isTrue(StringUtils.isNotBlank(name), "'name' must not be null");
		if (version != null && StringUtils.isBlank(version)) {
			version = null;
		}

		this.url = url;
		this.name = name;
		this.version = version;
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

	@Nullable
	public String getVersion() {
		return version;
	}

	//endregion
}
