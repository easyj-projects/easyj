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
package icu.easyj.core.json;

import icu.easyj.core.loader.ServiceProviders;

/**
 * Easyj自己支持的JSON
 *
 * @author wangliang181230
 */
public interface EasyjSupportedJSON {

	String[] SUPPORTED = new String[]{
			ServiceProviders.FASTJSON,
			ServiceProviders.JACKSON,
			ServiceProviders.GSON,
			ServiceProviders.HUTOOL
	};
}
