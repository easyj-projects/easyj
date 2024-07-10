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
package icu.easyj.core.constant;

import icu.easyj.core.util.PatternUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Constants} 测试类
 *
 * @author wangliang181230
 */
public class ConstantsTest {

	/**
	 * 测试一下 {@link Constants#VERSION} 常量是否通过 'replace-java' 插件生成成功。
	 */
	@Test
	public void testVersion() {
		System.out.println("测试一下 'Constants.VERSION' 常量是否通过 'replace-java' 插件生成成功：" + Constants.VERSION);
		this.validateVersion(Constants.VERSION, true);

		this.validateVersion("1", true);
		this.validateVersion("1.2", true);
		this.validateVersion("1.2.3-SNAPSHOT", true);
		this.validateVersion("latest", true);
		this.validateVersion("latest-SNAPSHOT", true);
		this.validateVersion("xxaa", false);
	}

	@Test
	public void testVersionInfo() {
		System.out.println("测试一下 'Constants.VERSION_INFO' 常量是否通过 'replace-java' 插件生成成功：" + Constants.VERSION_INFO);
		Assertions.assertEquals(Constants.VERSION, Constants.VERSION_INFO.getVersion());
	}


	private void validateVersion(String version, boolean expected) {
		boolean result = PatternUtils.validate("^(\\d+(\\.\\d+)*|latest)(-\\w+)*(-SNAPSHOT)?$", version);
		if (expected) {
			Assertions.assertTrue(result, "version = " + version);
		} else {
			Assertions.assertFalse(result, "version = " + version);
		}
	}
}
