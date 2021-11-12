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
package icu.easyj.core.logging.impls;

import com.alibaba.fastjson.JSON;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.logging.IToStringService;

import static icu.easyj.core.loader.ServiceProviders.FASTJSON;

/**
 * 基于 Fastjson 方法实现的转字符串服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = FASTJSON, order = 10)
@DependsOnClass(JSON.class)
class AlibabaFastjsonToStringService implements IToStringService {

	@Override
	public String toString(Object obj) {
		return JSON.toJSONString(obj);
	}
}
