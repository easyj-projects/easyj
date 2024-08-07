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
package icu.easyj.web.util;

import javax.servlet.ServletContext;

import org.springframework.lang.NonNull;
import org.springframework.web.context.WebApplicationContext;

/**
 * HTTP相关配置持有者
 *
 * @author wangliang181230
 */
public abstract class HttpConfigs {

	/**
	 * 二级目录
	 */
	private static String contextPath;


	/**
	 * 从WebApplicationContext中读取全局配置
	 *
	 * @param webApplicationContext Web应用内容
	 */
	public static void loadFromWebApplicationContext(@NonNull WebApplicationContext webApplicationContext) {
		ServletContext servletContext = webApplicationContext.getServletContext();
		if (servletContext != null) {
			setContextPath(servletContext.getContextPath());
		}
	}


	//region Getter、Setter

	public static String getContextPath() {
		return contextPath;
	}

	public static void setContextPath(String contextPath) {
		HttpConfigs.contextPath = contextPath;
	}

	//endregion
}
