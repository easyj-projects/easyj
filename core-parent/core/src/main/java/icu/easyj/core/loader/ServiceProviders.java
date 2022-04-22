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
package icu.easyj.core.loader;

/**
 * 服务提供方名称
 *
 * @author wangliang181230
 */
public interface ServiceProviders {

	/**
	 * 提供的服务有：JSON服务
	 * 开发者：阿里巴巴（Alibaba）
	 */
	String FASTJSON2 = "fastjson2";

	/**
	 * 提供的服务有：JSON服务
	 * 开发者：阿里巴巴（Alibaba）
	 */
	String FASTJSON = "fastjson";

	/**
	 * 提供的服务有：JSON服务
	 */
	String JACKSON = "jackson";

	/**
	 * 提供的服务有：JSON服务
	 * 开发者：谷歌（Google）
	 */
	String GSON = "gson";

	/**
	 * 提供的服务有：JSON、加密算法
	 */
	String HUTOOL = "hutool";
}
