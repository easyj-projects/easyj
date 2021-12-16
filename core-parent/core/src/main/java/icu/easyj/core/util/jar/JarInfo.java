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

import java.util.Objects;
import java.util.jar.Attributes;

import icu.easyj.core.util.StringUtils;
import icu.easyj.core.util.version.VersionInfo;
import icu.easyj.core.util.version.VersionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * JAR信息
 *
 * @author wangliang181230
 */
public class JarInfo {

	public static final String UNKNOWN_GROUP = "<unknown>";


	/**
	 * JAR文件路径
	 */
	private final String filePath;

	/**
	 * JAR所属组
	 */
	private final String group;

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
	 * @param filePath           JAR文件路径
	 * @param group              JAR所属组名
	 * @param name               JAR名称
	 * @param manifestAttributes META-INF/MANIFEST.MF文件的属性集合
	 * @param versionInfo        JAR版本信息
	 */
	public JarInfo(@NonNull String filePath, @NonNull String group, @NonNull String name,
				   @NonNull Attributes manifestAttributes, @Nullable VersionInfo versionInfo) {
		Assert.notNull(filePath, "'filePath' must not be null");
		Assert.isTrue(StringUtils.isNotBlank(group), "'group' must not be null");
		Assert.isTrue(StringUtils.isNotBlank(name), "'name' must not be null");

		this.filePath = filePath;
		this.group = group;
		this.name = name.toLowerCase();
		this.versionInfo = versionInfo;
		this.manifestAttributes = manifestAttributes;
	}

	public JarInfo(@NonNull String filePath, @NonNull String group, @NonNull String name,
				   @NonNull Attributes manifestAttributes, @Nullable String version) {
		this(filePath, group, name, manifestAttributes, VersionUtils.parse(version));
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

	/**
	 * 是否介于两个版本之间，即：{@code version >= startVersion && version <= endVersion}
	 *
	 * @param startVersion 起始版本
	 * @param toVersion    截止版本
	 * @return true=介于 | false=不介于
	 */
	public boolean betweenVersion(String startVersion, String toVersion) {
		return versionInfo.between(startVersion, toVersion);
	}

	public boolean notBetweenVersion(String startVersion, String toVersion) {
		return versionInfo.notBetween(startVersion, toVersion);
	}


	//region Getter

	@NonNull
	public String getFilePath() {
		return filePath;
	}

	@NonNull
	public String getGroup() {
		return group;
	}

	@NonNull
	public String getName() {
		return name;
	}

	public String getFullName() {
		return group + ":" + name;
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


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JarInfo jarInfo = (JarInfo)o;
		return Objects.equals(filePath, jarInfo.filePath)
				&& Objects.equals(group, jarInfo.group)
				&& Objects.equals(name, jarInfo.name)
				&& Objects.equals(versionInfo, jarInfo.versionInfo)
				&& Objects.equals(manifestAttributes, jarInfo.manifestAttributes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(filePath, group, name, versionInfo, manifestAttributes);
	}
}
