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

import java.util.List;

import icu.easyj.core.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link JarUtils} 测试类
 *
 * @author wangliang181230
 */
public class JarUtilsTest {

	@Test
	public void testGetJarList() {
		List<JarInfo> jarList = JarUtils.getJarList();
		Assertions.assertNotNull(jarList);

		this.print(jarList);
	}


	//region Private

	/**
	 * 格式化打印，方便观察
	 *
	 * @param jarList jar列表
	 */
	private void print(List<JarInfo> jarList) {
		int maxNameLength = 0;
		int maxVersionLength = 0;
		int maxLongVersionLength = 0;
		for (JarInfo jar : jarList) {
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

		System.out.println("-----------------------------");
		System.out.println("size: " + jarList.size());
		System.out.println("-----------------------------");
		for (JarInfo jar : jarList) {
			System.out.println(StringUtils.leftPad(jar.getName(), maxNameLength)
					+ " : " + StringUtils.rightPad(ObjectUtils.defaultIfNull(jar.getVersion(), ""), maxVersionLength)
					+ " : " + StringUtils.rightPad(String.valueOf(jar.getVersionLong()), maxLongVersionLength)
					+ "   ->   " + jar.getFilePath());
		}
		System.out.println("-----------------------------");
	}

	//endregion
}
