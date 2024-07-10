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

import cn.hutool.system.JavaInfo;
import cn.hutool.system.SystemUtil;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;
import org.springframework.core.Ordered;

/**
 * JAVA JDK的JAR所属组名加载器
 *
 * @author wangliang181230
 */
@LoadLevel(name = "java-jdk", order = Ordered.LOWEST_PRECEDENCE - 100)
public class JavaJdkJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		switch (jarContext.getName()) {
			case "rt":
			case "access-bridge-64":
			case "charsets":
			case "cldrdata":
			case "dnsns":
			case "jaccess":
			case "jce":
			case "jfr":
			case "jsse":
			case "jvmci-services":
			case "localedata":
			case "management-agent":
			case "nashorn":
			case "resources":
			case "sunec":
			case "sunjce_provider":
			case "sunmscapi":
			case "sunpkcs11":
			case "zipfs":
				if (jarContext.getVersionInfo().isUnknownVersion()) {
					JavaInfo javaInfo = SystemUtil.getJavaInfo();
					jarContext.setVersion(javaInfo.getVersion());
				}
				return "java";
			default:
				break;
		}
		return null;
	}
}
