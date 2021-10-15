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
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

/**
 * {@link VersionUtils} 测试类
 *
 * @author wangliang181230
 */
class VersionUtilsTest {

	/**
	 * TODO: 尝试性测试方法
	 *
	 * @throws IOException IO异常
	 */
	@Test
	void test() throws IOException {
		System.out.println("----------------------------------------------------");
		ClassLoader cl = this.getClass().getClassLoader();

		Enumeration<URL> urls = cl.getResources("META-INF/MANIFEST.MF");

		List<URL> urlList = new ArrayList<>();
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			urlList.add(url);
			System.out.println(url);
		}
		System.out.println("----------------------------------------------------");
		System.out.println(urlList.size());
		System.out.println("----------------------------------------------------");

		for (URL url : urlList) {
			try {
				Manifest manifest = new Manifest(url.openStream());
				Attributes attributes = manifest.getMainAttributes();
				System.out.println(url);

				String version = attributes.getValue("Implementation-Version");
				if (org.apache.commons.lang3.StringUtils.isBlank(version)) {
					version = attributes.getValue("Bundle-Version");
				}

				String moduleName;
				String jarFilePath = url.toString();
				jarFilePath = jarFilePath.substring(0, jarFilePath.lastIndexOf("!/META-INF/MANIFEST.MF"));
				String jarFileName = jarFilePath.substring(jarFilePath.lastIndexOf("/") + 1);
				moduleName = jarFileName.replaceAll("(-\\d.*)?\\.jar$", "");
				if (org.apache.commons.lang3.StringUtils.isBlank(version)) {
					version = jarFileName.substring(0, jarFileName.lastIndexOf(".jar")).substring(moduleName.length());
					if (version.startsWith("-")) {
						version = version.substring(1);
					}
				}

				System.out.println("moduleName: " + moduleName + "    " + jarFilePath.contains(moduleName));
				if (StringUtils.isNotBlank(version)) {
					System.out.println("version: " + version + "    " + jarFilePath.contains(version));
				} else {
					System.out.println("version: 无版本号     false");
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
			} finally {
				System.out.println("----------------------------------------------------");
			}
		}
	}
}
