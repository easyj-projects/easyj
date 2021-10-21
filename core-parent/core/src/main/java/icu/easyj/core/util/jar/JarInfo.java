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
package icu.easyj.core.util.jar;

import java.net.URL;
import java.util.jar.Attributes;

import cn.hutool.core.lang.Assert;
import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.version.VersionInfo;
import icu.easyj.core.util.version.VersionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * JAR信息
 *
 * @author wangliang181230
 */
public class JarInfo {

	/**
	 * 路径
	 */
	private final URL url;

	/**
	 * JAR名称
	 */
	private final String name;

	/**
	 * JAR版本信息
	 */
	private final VersionInfo versionInfo;

	/**
	 * META-INF/MANIFEST.MF 文件中的属性
	 */
	private final Attributes manifestAttributes;


	/**
	 * 构造函数
	 *
	 * @param url                路径
	 * @param name               JAR名称
	 * @param manifestAttributes META-INF/MANIFEST.MF文件的属性集合
	 * @param version            JAR版本号
	 */
	public JarInfo(@NonNull URL url, @NonNull String name, @NonNull Attributes manifestAttributes, @Nullable String version) {
		Assert.notNull(url, "'url' must not be null");
		Assert.isTrue(StringUtils.isNotBlank(name), "'name' must not be null");

		this.url = url;
		this.name = name.toLowerCase();
		this.versionInfo = VersionUtils.parse(version);
		this.manifestAttributes = manifestAttributes;
	}

	/**
	 * 比较版本号
	 *
	 * @param otherVersion 其他版本号
	 * @return 比较结果：1=比other高、0=相同、-1=比other低
	 */
	public int compareToVersion(String otherVersion) {
		return versionInfo.compareTo(otherVersion);
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
	public VersionInfo getVersionInfo() {
		return versionInfo;
	}

	@NonNull
	public String getVersion() {
		return versionInfo.getVersion();
	}

	public long getVersionLong() {
		return versionInfo.getVersionLong();
	}

	@NonNull
	public Attributes getAttributes() {
		return manifestAttributes;
	}

	@Nullable
	public String getAttribute(Attributes.Name name) {
		return manifestAttributes.getValue(name);
	}

	@Nullable
	public String getAttribute(String name) {
		return manifestAttributes.getValue(name);
	}

	//endregion
}
