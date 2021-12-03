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
package icu.easyj.core.json.impls;

import java.util.List;

import com.alibaba.fastjson.JSON;
import icu.easyj.core.json.IJSONService;
import icu.easyj.core.json.JSONParseException;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static icu.easyj.core.loader.ServiceProviders.FASTJSON;

/**
 * 基于 Alibaba-FastJSON 实现的JSON服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = FASTJSON, order = 10)
@DependsOnClass(JSON.class)
class AlibabaFastJSONServiceImpl implements IJSONService {

	@NonNull
	@Override
	public <T> T toBean(@NonNull String text, @NonNull Class<T> targetClazz) throws JSONParseException {
		try {
			return JSON.parseObject(text, targetClazz);
		} catch (Exception e) {
			throw new JSONParseException("JSON字符串转Bean失败", e);
		}
	}

	@NonNull
	@Override
	public <T> List<T> toList(@NonNull String text, @NonNull Class<T> targetClazz) throws JSONParseException {
		try {
			return JSON.parseArray(text, targetClazz);
		} catch (Exception e) {
			throw new JSONParseException("JSON字符串转List失败", e);
		}
	}

	@NonNull
	@Override
	public String toJSONString(@Nullable Object obj) throws JSONParseException {
		try {
			return JSON.toJSONString(obj);
		} catch (Exception e) {
			throw new JSONParseException("obj转JSON字符串失败", e);
		}
	}


	@NonNull
	@Override
	public String getName() {
		return FASTJSON;
	}
}
