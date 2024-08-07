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
package icu.easyj.core.util.jar.impls;

import java.io.IOException;
import java.util.Set;

import cn.hutool.core.io.IORuntimeException;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.ArrayUtils;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * 从JAR中的maven的xml文件中读取组名
 *
 * @author wangliang181230
 */
@LoadLevel(name = "maven", order = 0)
public class MavenJarGroupLoaderImpl implements IJarGroupLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(MavenJarGroupLoaderImpl.class);


	@Override
	public String load(JarContext jarContext) {
		// 如果存在指定目录的pom.xml文件，则直接解析该pom.xml的文件路径，获取group
		String locationPattern = "/META-INF/maven/*/" + jarContext.getName() + "/pom.xml";
		Resource[] resources = jarContext.getResources(locationPattern);
		if (resources.length == 1) {
			return this.parseGroup(resources[0]);
		} else if (resources.length > 1) {
			// 根据包名筛选一遍，如果筛选到了，则返回
			String group = this.findByPackage(jarContext, resources);
			if (group != null) {
				return group;
			}

			StringBuilder sb = new StringBuilder();
			for (Resource resource : resources) {
				sb.append("\r\n - ");
				try {
					sb.append(resource.getURL());
				} catch (IOException ignore) {
				}
			}
			LOGGER.warn("通过资源路径匹配串 '{}' 找到多个 'pom.xml' 文件，现从第一个文件路径中获取JAR所属组名，多个 'pom.xml' 文件路径如下：{}", locationPattern, sb);
			return this.parseGroup(resources[0]);
		}

		// 查找出所有pom.xml文件，通过两种策略解析
		resources = jarContext.getResources("/META-INF/maven/*/*/pom.xml");
		if (ArrayUtils.isNotEmpty(resources)) {
			// 策略1：查找包含 "/${name}" 的resource
			for (Resource res : resources) {
				if (res.toString().contains("/" + jarContext.getName())) {
					return this.parseGroup(res);
				}
			}

			// 策略2：获取所有的group如果全部一样，则返回，否则抛出异常
			String groupResult = null;
			boolean hasMultipleGroup = false;
			for (Resource res : resources) {
				String group = this.parseGroup(res);
				if (groupResult == null) {
					groupResult = group;
				} else if (!groupResult.equals(group)) {
					hasMultipleGroup = true;
					break;
				}
			}
			if (hasMultipleGroup) {
				// 根据包名筛选一遍，如果筛选到了，则返回
				String group = this.findByPackage(jarContext, resources);
				if (group != null) {
					return group;
				}

				// 记录警告日志
				StringBuilder sb = new StringBuilder();
				for (Resource res : resources) {
					sb.append("\r\n - ").append(res);
				}
				LOGGER.warn("JAR '{}' 中存在多个组名不统一的 'pom.xml' 文件，现直接返回第一个组名，多个 'pom.xml' 文件路径如下：{}", jarContext.getName(), sb);
			}
			return groupResult;
		}

		return null;
	}

	String parseGroup(Resource resource) {
		String resourceUrl;
		try {
			resourceUrl = resource.getURL().toString();
		} catch (IOException e) {
			throw new IORuntimeException("读取 pom.xml 文件路径失败", e);
		}

		return parseGroup(resourceUrl);
	}

	String parseGroup(String resourceUrl) {
		String group = resourceUrl.substring(resourceUrl.indexOf("/META-INF/maven/") + "/META-INF/maven/".length());
		group = group.substring(0, group.indexOf('/'));
		return group;
	}

	String findByPackage(JarContext jarContext, Resource[] resources) {
		Set<String> packages = jarContext.getClassPackages(2);

		for (String pkg : packages) {
			for (Resource resource : resources) {
				if (resource.toString().contains("/META-INF/maven/" + pkg)) {
					return this.parseGroup(resource);
				}
			}
		}
		return null;
	}
}
