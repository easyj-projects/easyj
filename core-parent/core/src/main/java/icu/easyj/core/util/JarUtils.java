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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Jar工具类
 *
 * @author wangliang181230
 */
public abstract class JarUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JarUtils.class);


	//region The Constants Attributes.Name

	public static final Attributes.Name IMPLEMENTATION_VERSION = Attributes.Name.IMPLEMENTATION_VERSION;

	public static final Attributes.Name BUNDLE_VERSION = new Attributes.Name("Bundle-Version");

	//endregion

	//region The Caches

	/**
	 * cl -> jarList
	 */
	private static final Map<ClassLoader, List<JarInfo>> CL_JAR_LIST_CACHE = new ConcurrentHashMap<>();

	/**
	 * cl -> jarName -> jarInfo
	 */
	private static final Map<ClassLoader, Map<String, JarInfo>> CL_JAR_MAP_CACHE = new ConcurrentHashMap<>();

	//endregion


	//region getJarList

	/**
	 * 获取Jar列表（使用缓存）
	 *
	 * @param classLoader 类加载器
	 * @return Jar列表
	 */
	@NonNull
	public static List<JarInfo> getJarList(@NonNull ClassLoader classLoader) {
		return MapUtils.computeIfAbsent(CL_JAR_LIST_CACHE, classLoader, cl -> Collections.unmodifiableList(loadJarList(cl)));
	}

	/**
	 * 获取当前类加载器中的Jar列表（使用缓存）
	 *
	 * @return Jar列表
	 */
	@NonNull
	public static List<JarInfo> getJarList() {
		return getJarList(Thread.currentThread().getContextClassLoader());
	}

	//endregion


	//region getJarMap

	/**
	 * 获取Jar集合
	 *
	 * @param classLoader 类加载器
	 * @return Jar集合
	 */
	@NonNull
	public static Map<String, JarInfo> getJarMap(@NonNull ClassLoader classLoader) {
		return MapUtils.computeIfAbsent(CL_JAR_MAP_CACHE, classLoader, cl -> {
			List<JarInfo> jarList = getJarList(classLoader);

			Map<String, JarInfo> jarMap = new HashMap<>(jarList.size());
			JarInfo previousJar;
			for (JarInfo jar : jarList) {
				previousJar = jarMap.put(jar.getName(), jar);
				if (previousJar != null) {
					LOGGER.warn("存在重名的Jar，'{}:{}' 覆盖了 '{}:{}'", jar.getName(), jar.getVersion(), previousJar.getName(), previousJar.getVersion());
				}
			}
			return Collections.unmodifiableMap(jarMap);
		});
	}

	/**
	 * 获取当前类加载器中的Jar集合（使用缓存）
	 *
	 * @return Jar集合
	 */
	@NonNull
	public static Map<String, JarInfo> getJarMap() {
		return getJarMap(Thread.currentThread().getContextClassLoader());
	}

	//endregion


	//region getJar

	/**
	 * 获取Jar信息
	 *
	 * @param name        Jar名称
	 * @param classLoader 类加载器
	 * @return Jar信息
	 */
	@Nullable
	public static JarInfo getJar(String name, @NonNull ClassLoader classLoader) {
		if (name == null) {
			return null;
		}

		Map<String, JarInfo> jarMap = getJarMap(classLoader);
		return jarMap.get(name.toLowerCase());
	}

	/**
	 * 获取当前类加载器中的Jar信息
	 *
	 * @param name Jar名称
	 * @return Jar信息
	 */
	@Nullable
	public static JarInfo getJar(String name) {
		return getJar(name, Thread.currentThread().getContextClassLoader());
	}

	//endregion


	//region Private

	/**
	 * 加载Jar列表（不使用缓存）
	 *
	 * @param classLoader 类加载器
	 * @return Jar列表
	 */
	@NonNull
	private static List<JarInfo> loadJarList(@NonNull ClassLoader classLoader) {
		List<JarInfo> result = new ArrayList<>();

		Enumeration<URL> urls;
		try {
			urls = classLoader.getResources("META-INF/MANIFEST.MF");
		} catch (IOException e) {
			return result;
		}

		URL url;
		String jarFilePath;
		while (urls.hasMoreElements()) {
			url = urls.nextElement();
			jarFilePath = url.toString();
			try {
				// 跳过不是 '*.jar' 的文件
				if (!jarFilePath.endsWith(".jar!/META-INF/MANIFEST.MF")) {
					continue;
				}

				Manifest manifest = new Manifest(url.openStream());
				Attributes attributes = manifest.getMainAttributes();

				// 获取版本号
				String version = attributes.getValue(IMPLEMENTATION_VERSION);
				if (StringUtils.isBlank(version)) {
					version = attributes.getValue(BUNDLE_VERSION);
				}

				// 获取模块名
				jarFilePath = jarFilePath.substring(0, jarFilePath.lastIndexOf(".jar!/META-INF/MANIFEST.MF"));
				String jarFileName = jarFilePath.substring(jarFilePath.lastIndexOf("/") + 1);
				String name = jarFileName.replaceAll("-\\d.*$", "");

				// 如果版本号为空，则尝试获取除模块名以外的内容作为版本号
				if (StringUtils.isBlank(version) && name.length() != jarFileName.length()) {
					version = jarFileName.substring(name.length());
					if (version.startsWith("-")) {
						version = version.substring(1);
					}
				}

				result.add(new JarInfo(url, name, attributes, version));
			} catch (IOException | RuntimeException e) {
				LOGGER.warn("加载jar信息失败：{}", jarFilePath, e);
			}
		}

		// 根据jarName排序
		result.sort(Comparator.comparing(JarInfo::getName));

		return result;
	}

	//endregion
}
