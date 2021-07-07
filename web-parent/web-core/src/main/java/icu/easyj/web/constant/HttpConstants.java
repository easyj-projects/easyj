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
package icu.easyj.web.constant;

/**
 * Http相关常量
 *
 * @author wangliang181230
 */
public interface HttpConstants {

	//region 请求方法常量

	/**
	 * GET常量
	 */
	String GET_METHOD = "GET";

	/**
	 * POST常量
	 */
	String POST_METHOD = "POST";

	//endregion


	//region 文件下载相关常量

	/**
	 * 执行导出的参数名
	 */
	String DO_EXPORT_PARAM_NAME = "doExport";

	/**
	 * 执行导出的匹配参数值
	 */
	String DO_EXPORT_PARAM_VALUE = "true";

	//endregion
}
