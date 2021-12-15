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
package icu.easyj.core.util.jar.impls;

import java.io.IOException;

import cn.hutool.core.io.IORuntimeException;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;
import org.springframework.core.io.Resource;

/**
 * 从JAR中的maven的xml文件中读取组名
 *
 * @author wangliang181230
 */
@LoadLevel(name = "maven", order = 0)
public class MavenJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		Resource resource = jarContext.getResource("META-INF/maven/*/" + jarContext.getName() + "/pom.xml");
		if (resource == null) {
			resource = jarContext.getResource("META-INF/maven/**/pom.xml");
		}

		if (resource != null) {
			String group;
			try {
				group = resource.getURL().toString();
			} catch (IOException e) {
				throw new IORuntimeException("读取 pom.xml 文件路径失败", e);
			}

			group = group.substring(group.indexOf("META-INF/maven/") + "META-INF/maven/".length());
			group = group.substring(0, group.indexOf('/'));
			return group;
		}

		return null;
	}
}
