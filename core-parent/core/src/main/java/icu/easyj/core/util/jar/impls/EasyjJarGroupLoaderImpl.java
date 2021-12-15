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

import java.util.jar.Attributes;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.jar.IJarGroupLoader;
import icu.easyj.core.util.jar.JarContext;
import org.springframework.core.Ordered;

/**
 * easyj的依赖
 *
 * @author wangliang181230
 */
@LoadLevel(name = "easyj", order = Ordered.LOWEST_PRECEDENCE - 1000)
public class EasyjJarGroupLoaderImpl implements IJarGroupLoader {

	@Override
	public String load(JarContext jarContext) {
		// 注意：title信息是从pom.xml的<name>中复制过来的。务必将所有easyj的模块的name值拼接同样的格式
		String title = jarContext.getAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE);
		if (title != null && title.startsWith("icu.easyj")) {
			return title.substring(0, title.indexOf("::")).trim();
		}

		return null;
	}
}
