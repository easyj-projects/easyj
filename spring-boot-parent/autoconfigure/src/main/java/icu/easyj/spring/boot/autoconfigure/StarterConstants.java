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
package icu.easyj.spring.boot.autoconfigure;

import icu.easyj.core.constant.Constants;

/**
 * EasyJ-Starter相关的常量
 *
 * @author wangliang181230
 */
public interface StarterConstants {

	//region PREFIX

	// Root
	String PREFIX = Constants.PREFIX;


	// Global
	String GLOBAL_PREFIX = PREFIX + ".global";


	// Web
	String WEB_PREFIX = PREFIX + ".web";
	String WEB_CACHE304_PREFIX = WEB_PREFIX + ".cache304";

	// Web POI
	String WEB_POI_PREFIX = WEB_PREFIX + ".poi";
	// Web POI Excel
	String WEB_POI_EXCEL_PREFIX = WEB_POI_PREFIX + ".excel";
	String WEB_POI_EXCEL_EXPORT_PREFIX = WEB_POI_EXCEL_PREFIX + ".export";

	// Web Param Crypto
	String WEB_PARAM_CRYPTO_PREFIX = WEB_PREFIX + ".param.crypto";
	String WEB_PARAM_CRYPTO_FILTER_PREFIX = WEB_PARAM_CRYPTO_PREFIX + ".filter";
	String WEB_PARAM_CRYPTO_HANDLER_PREFIX = WEB_PREFIX + ".handler";

	//endregion

}
