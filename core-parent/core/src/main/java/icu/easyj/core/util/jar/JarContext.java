/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License; Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing; software
 * distributed under the License is distributed on an "AS IS" BASIS;
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND; either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.core.util.jar;

import java.io.File;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import cn.hutool.core.io.IORuntimeException;
import icu.easyj.core.exception.MultipleFilesFoundException;
import icu.easyj.core.util.version.VersionInfo;
import icu.easyj.core.util.version.VersionUtils;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

/**
 * 加载JAR信息时的上下文
 *
 * @author wangliang181230
 */
public class JarContext {

	/**
	 * JAR文件路径
	 */
	private final String jarFilePath;

	/**
	 * JAR文件对象
	 */
	private JarFile jarFile;

	/**
	 * JAR名称
	 */
	private final String name;

	/**
	 * JAR版本号
	 */
	private VersionInfo versionInfo;

	/**
	 * JAR描述信息
	 */
	private final Manifest manifest;

	/**
	 * JAR属性
	 */
	private final Attributes attributes;


	public JarContext(String jarFilePath, String name, VersionInfo versionInfo, Manifest manifest, Attributes attributes) {
		this.jarFilePath = jarFilePath;
		this.name = name;
		this.versionInfo = versionInfo;
		this.manifest = manifest;
		this.attributes = attributes;
	}


	public String getJarFilePath() {
		return jarFilePath;
	}

	public String getName() {
		return name;
	}

	public VersionInfo getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	public void setVersion(String version) {
		setVersionInfo(VersionUtils.parse(version));
	}

	public Manifest getManifest() {
		return manifest;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public JarFile getJarFile() {
		if (jarFile == null) {
			try {
				File file = ResourceUtils.getFile(this.jarFilePath);
				jarFile = new JarFile(file);
			} catch (IOException ioe) {
				throw new IORuntimeException("获取JAR文件失败：" + jarFilePath, ioe);
			}
		}
		return jarFile;
	}

	/**
	 * 在JAR包中检索资源
	 *
	 * @param locationPattern 资源路径匹配串
	 * @return resources 资源列表
	 */
	@NonNull
	public Resource[] getResources(String locationPattern) {
		if (!locationPattern.startsWith("!")) {
			if (!locationPattern.startsWith("/")) {
				locationPattern = "!/" + locationPattern;
			} else {
				locationPattern = "!" + locationPattern;
			}
		}

		locationPattern = jarFilePath + locationPattern;
		return icu.easyj.core.util.ResourceUtils.getResources(locationPattern);
	}

	/**
	 * 在JAR包中检索单个资源
	 *
	 * @param locationPattern 资源路径匹配串
	 * @return resource 目标资源
	 */
	@Nullable
	public Resource getResource(String locationPattern) {
		Resource[] resources = getResources(locationPattern);
		if (resources.length == 0) {
			return null;
		} else if (resources.length == 1) {
			return resources[0];
		} else {
			StringBuilder sb = new StringBuilder();
			for (Resource resource : resources) {
				sb.append("\r\n - ");
				try {
					sb.append(resource.getURL());
				} catch (IOException ignore) {
				}
			}
			throw new MultipleFilesFoundException("通过资源路径匹配串 '" + locationPattern + "' 找到多个资源文件：" + sb);
		}
	}
}
