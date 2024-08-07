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
package icu.easyj.core.loader.model;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.loader.condition.DependsOnJarVersion;
import icu.easyj.core.loader.condition.DependsOnJavaVersion;

/**
 * The type Chinese hello.
 * 注：从阿里的seata项目中复制过来的。
 *
 * @author Otis.z
 */
@LoadLevel(name = "ChineseHello")
@DependsOnJavaVersion(min = 1, max = 999)
@DependsOnClass(value = Hello.class, name = "icu.easyj.core.loader.model.Hello")
@DependsOnJarVersion(name = "org.slf4j:slf4j-api", minVersion = "0", maxVersion = "99")
public class ChineseHello implements Hello {

	@Override
	public String say() {
		return "ni hao!";
	}
}
