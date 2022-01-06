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
package icu.easyj.core.util.jar.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;
import icu.easyj.core.util.version.VersionInfo;
import org.springframework.core.Ordered;

/**
 * HuTool的JAR组名
 *
 * @author wangliang181230
 */
@LoadLevel(name = "hutool", order = Ordered.LOWEST_PRECEDENCE - 500)
public class HutoolJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		String name = jarContext.getName();
		VersionInfo versionInfo = jarContext.getVersionInfo();

		// hutool，最低版本为4.0.0
		if (name.startsWith("hutool-") && versionInfo.compareTo("4.0.0-SNAPSHOT") >= 0) {
			return "cn.hutool";
		}

		return null;
	}
}
