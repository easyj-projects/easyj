/*
 * Copyright 2021-2022 the original author or authors.
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
import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

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

		System.out.println(JarUtils.convertToDescriptionStr(jarList));
	}

	@Test
	public void testGetJarMap() {
		Map<String, JarInfo> jarMap = JarUtils.getJarMap();
		Assertions.assertNotNull(jarMap);

		JarInfo jarInfo1 = JarUtils.getJar("org.springframework", "spring-core");
		Assertions.assertNotNull(jarInfo1);

		// TODO: 由于组名加载器还不够完善，如果上面使用全名未获取到JAR信息，则通过 '<unknown>' 作为组名重新获取一遍，等组名加载器足够完善以后，此代码可以删除
		JarInfo jarInfo2 = JarUtils.getJar("spring-core");
		Assertions.assertNotNull(jarInfo2);
		Assertions.assertEquals(jarInfo1, jarInfo2);
	}
}
