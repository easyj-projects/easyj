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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import icu.easyj.core.exception.MultipleFilesFoundException;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.util.MapUtils;
import icu.easyj.core.util.ObjectUtils;
import icu.easyj.core.util.ResourceUtils;
import icu.easyj.core.util.version.VersionInfo;
import icu.easyj.core.util.version.VersionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * JAR工具类
 *
 * @author wangliang181230
 */
public abstract class JarUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(JarUtils.class);

	/**
	 * JAR所属组名加载器列表
	 */
	private static final List<IJarGroupLoader> JAR_GROUP_LOADER_LIST = EnhancedServiceLoader.loadAll(IJarGroupLoader.class);


	//region The Constants Attributes.Name

	public static final Attributes.Name IMPLEMENTATION_VERSION = Attributes.Name.IMPLEMENTATION_VERSION;

	public static final Attributes.Name BUNDLE_SYMBOLIC_NAME = new Attributes.Name("Bundle-SymbolicName");
	public static final Attributes.Name BUNDLE_VERSION = new Attributes.Name("Bundle-Version");

	public static final Attributes.Name AUTOMATIC_MODULE_NAME = new Attributes.Name("Automatic-Module-Name");

	//endregion

	//region The Caches

	/**
	 * classLoader -> jarList
	 */
	private static final Map<ClassLoader, List<JarInfo>> CL_JAR_LIST_CACHE = new ConcurrentHashMap<>();

	/**
	 * classLoader -> jarGroup:jarName -> jarInfo
	 */
	private static final Map<ClassLoader, Map<String, JarInfo>> CL_JAR_MAP_CACHE = new ConcurrentHashMap<>();

	//endregion


	//region getJarList

	/**
	 * 获取JAR列表（使用缓存）
	 *
	 * @param classLoader 类加载器
	 * @return JAR列表
	 */
	@NonNull
	public static List<JarInfo> getJarList(@NonNull ClassLoader classLoader) {
		return MapUtils.computeIfAbsent(CL_JAR_LIST_CACHE, classLoader, JarUtils::loadJarList);
	}

	/**
	 * 获取当前类加载器中的JAR列表（使用缓存）
	 *
	 * @return JAR列表
	 */
	@NonNull
	public static List<JarInfo> getJarList() {
		return getJarList(Thread.currentThread().getContextClassLoader());
	}

	/**
	 * 将JAR信息列表转换为能够输出并方便查看的字符串
	 *
	 * @param jarList JAR信息列表
	 * @return 可观察字符串
	 */
	public static String convertToDescriptionStr(List<JarInfo> jarList) {
		int maxGroupLength = 0;
		int maxNameLength = 0;
		int maxVersionLength = 0;
		int maxLongVersionLength = 0;
		for (JarInfo jar : jarList) {
			if (maxGroupLength < jar.getGroup().length()) {
				maxGroupLength = jar.getGroup().length();
			}
			if (maxNameLength < jar.getName().length()) {
				maxNameLength = jar.getName().length();
			}
			if (maxVersionLength < jar.getVersion().length()) {
				maxVersionLength = jar.getVersion().length();
			}
			if (maxLongVersionLength < String.valueOf(jar.getVersionLong()).length()) {
				maxLongVersionLength = String.valueOf(jar.getVersionLong()).length();
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("\r\n----------------------------------------------------------\r\n");
		sb.append("当前项目所有依赖，总共 ").append(jarList.size()).append(" 个\r\n");
		sb.append("----------------------------------------------------------\r\n");
		for (JarInfo jar : jarList) {
			sb.append(StringUtils.rightPad(jar.getGroup(), maxGroupLength))
					.append(" : ").append(StringUtils.rightPad(jar.getName(), maxNameLength))
					.append(" : ").append(StringUtils.rightPad(ObjectUtils.defaultIfNull(jar.getVersion(), ""), maxVersionLength))
					.append(" : ").append(StringUtils.leftPad(String.valueOf(jar.getVersionLong()), maxLongVersionLength))
					.append("   ->   ").append(jar.getFilePath())
					.append("\r\n");
		}
		sb.append("----------------------------------------------------------\r\n\r\n");

		return sb.toString();
	}

	//endregion


	//region getJarMap

	/**
	 * 获取JAR集合
	 *
	 * @param classLoader 类加载器
	 * @return JAR集合
	 */
	@NonNull
	public static Map<String, JarInfo> getJarMap(@NonNull ClassLoader classLoader) {
		return MapUtils.computeIfAbsent(CL_JAR_MAP_CACHE, classLoader, cl -> {
			List<JarInfo> jarList = getJarList(classLoader);

			Map<String, JarInfo> jarMap = new HashMap<>(jarList.size());
			JarInfo previousJar;
			for (JarInfo jar : jarList) {
				previousJar = jarMap.put(jar.getFullName(), jar);
				if (previousJar != null && !previousJar.equals(jar)) {
					LOGGER.warn("存在重名的JAR，'{}:{}' 覆盖了 '{}:{}'",
							jar.getFilePath(), jar.getVersion(),
							previousJar.getFilePath(), previousJar.getVersion());
				}
			}
			return jarMap;
		});
	}

	/**
	 * 获取当前类加载器中的JAR集合（使用缓存）
	 *
	 * @return JAR集合
	 */
	@NonNull
	public static Map<String, JarInfo> getJarMap() {
		return getJarMap(Thread.currentThread().getContextClassLoader());
	}

	//endregion


	//region getJar

	/**
	 * 获取JAR信息
	 *
	 * @param jarName     JAR完整名称，包含所属组名及名称，用冒号分隔开来，格式如：icu.easyj:easyj-all
	 * @param classLoader 类加载器
	 * @return JAR信息
	 */
	@Nullable
	public static JarInfo getJar(@NonNull String jarName, @NonNull ClassLoader classLoader) {
		Assert.notNull(jarName, "'jarName' must be not null");
		jarName = jarName.toLowerCase();

		JarInfo jarInfo = null;

		if (StringUtils.contains(jarName, ':')) {
			Map<String, JarInfo> jarMap = getJarMap(classLoader);

			jarInfo = jarMap.get(jarName);

			// TODO: 由于组名加载器还不够完善，如果上面使用全名未获取到JAR信息，则通过 '<unknown>' 作为组名重新获取一遍，等组名加载器足够完善以后，此代码可以删除
			if (jarInfo == null) {
				String unknownGroupFullName = JarInfo.UNKNOWN_GROUP + (jarName.contains(":") ? jarName.substring(jarName.indexOf(':')) : ":" + jarName);
				jarInfo = jarMap.get(unknownGroupFullName);
			}
		} else {
			List<JarInfo> result = new ArrayList<>();

			List<JarInfo> jarList = getJarList(classLoader);
			for (JarInfo jar : jarList) {
				if (jar.getName().equals(jarName)) {
					result.add(jar);
				}
			}

			// 如果找到多个jar，则抛出异常
			if (result.size() > 1) {
				StringBuilder sb = new StringBuilder();
				for (JarInfo jar : result) {
					sb.append("\r\n - ").append(jar.getFullName());
				}
				throw new MultipleFilesFoundException("找到多个名为 '" + jarName + "' 的JAR信息，无法确定是哪一个，请明确JAR所属组名：" + sb);
			}

			jarInfo = result.get(0);
		}

		return jarInfo;
	}

	/**
	 * 获取JAR信息
	 *
	 * @param group       JAR所属组名
	 * @param name        JAR名称
	 * @param classLoader 类加载器
	 * @return JAR信息
	 */
	@Nullable
	public static JarInfo getJar(@NonNull String group, @NonNull String name, @NonNull ClassLoader classLoader) {
		Assert.notNull(group, "'group' must be not null");
		Assert.notNull(name, "'name' must be not null");

		return getJar(group + ":" + name, classLoader);
	}

	/**
	 * 获取当前类加载器中的JAR信息
	 *
	 * @param jarName JAR完整名称，包含所属组名及名称，用冒号分隔开来，格式如：icu.easyj:easyj-all
	 * @return JAR信息
	 */
	@Nullable
	public static JarInfo getJar(String jarName) {
		return getJar(jarName, Thread.currentThread().getContextClassLoader());
	}

	/**
	 * 获取JAR信息
	 *
	 * @param group JAR所属组名
	 * @param name  JAR名称
	 * @return JAR信息
	 */
	@Nullable
	public static JarInfo getJar(@NonNull String group, @NonNull String name) {
		return getJar(group, name, Thread.currentThread().getContextClassLoader());
	}

	//endregion


	//region Private

	/**
	 * 加载JAR列表（不使用缓存）
	 *
	 * @param classLoader 类加载器
	 * @return JAR列表
	 */
	@NonNull
	private static List<JarInfo> loadJarList(@NonNull ClassLoader classLoader) {
		List<JarInfo> result = new ArrayList<>();

		long startTime = System.nanoTime();
		try {
			Resource[] resources = ResourceUtils.getResources("classpath*:/META-INF/MANIFEST.MF");

			String jarFilePath;
			for (Resource resource : resources) {
				jarFilePath = null;
				try {
					jarFilePath = resource.getURL().toString();

					// 跳过不是 '*.jar' 的文件
					if (!jarFilePath.endsWith(".jar!/META-INF/MANIFEST.MF")) {
						continue;
					}

					Manifest manifest = new Manifest(resource.getInputStream());
					Attributes attributes = manifest.getMainAttributes();

					// 获取JAR版本号
					String version = attributes.getValue(IMPLEMENTATION_VERSION);
					if (StringUtils.isBlank(version)) {
						version = attributes.getValue(BUNDLE_VERSION);
					}

					// 获取JAR名称
					jarFilePath = jarFilePath.substring(0, jarFilePath.lastIndexOf(".jar!/META-INF/MANIFEST.MF"));
					String jarFileName = jarFilePath.substring(jarFilePath.lastIndexOf("/") + 1);
					String name = jarFileName.replaceAll("-\\d.*$", "");
					jarFilePath += ".jar";

					// 如果版本号为空，则尝试获取除模块名以外的内容作为版本号
					if (StringUtils.isBlank(version) && name.length() != jarFileName.length()) {
						version = jarFileName.substring(name.length());
						if (version.startsWith("-")) {
							version = version.substring(1);
						}
					}
					VersionInfo versionInfo = VersionUtils.parse(version);

					// 加载JAR所属组名
					JarContext context = new JarContext(jarFilePath, name, versionInfo, manifest, attributes);
					String group = loadGroup(context);
					if (StringUtils.isBlank(group)) {
//						LOGGER.warn("未加载到JAR '{}' 的所属组名，JAR文件路径：{}。请尝试添加能够加载到当前JAR所属组名的 '{}' 接口的实现。",
//								name, jarFilePath, IJarGroupLoader.class.getName());
						group = JarInfo.UNKNOWN_GROUP;
					}

					// 添加到列表中
					result.add(new JarInfo(jarFilePath, group, name, attributes, context.getVersionInfo()));
				} catch (IOException | RuntimeException e) {
					LOGGER.warn("加载JAR信息失败：{}", jarFilePath == null ? resource : jarFilePath, e);
				}
			}

			// 根据jarName排序
			result.sort((a, b) -> {
				if (a.getGroup().equals(b.getGroup())) {
					return a.getName().compareTo(b.getName());
				} else {
					return a.getGroup().compareTo(b.getGroup());
				}
			});
		} finally {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("共加载JAR信息 {} 个，耗时: {} ms, 类加载器为：{}", result.size(), (System.nanoTime() - startTime) / 1000000, classLoader);
			}
		}

		return result;
	}

	/**
	 * 加载JAR所属组名
	 *
	 * @param context JAR信息上下文
	 * @return group
	 */
	private static String loadGroup(JarContext context) {
		String group = null;
		for (IJarGroupLoader groupLoader : JAR_GROUP_LOADER_LIST) {
			group = groupLoader.load(context);
			if (StringUtils.isNotBlank(group)) {
				return group;
			}
		}

		return group;
	}

	//endregion
}
