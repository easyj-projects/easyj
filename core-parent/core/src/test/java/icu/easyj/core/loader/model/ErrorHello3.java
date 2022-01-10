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
package icu.easyj.core.loader.model;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnJarVersion;

/**
 * The type Error hello 3.
 *
 * @author wangliang181230
 */
@LoadLevel(name = "ErrorHello3")
@DependsOnJarVersion(name = "xxxx:not_exist_jar")
public class ErrorHello3 implements Hello {

	@Override
	public String say() {
		return "error hello3!";
	}
}
