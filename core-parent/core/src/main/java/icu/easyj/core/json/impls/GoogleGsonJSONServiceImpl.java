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
package icu.easyj.core.json.impls;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import icu.easyj.core.json.IJSONService;
import icu.easyj.core.json.JSONParseException;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static icu.easyj.core.loader.ServiceProviders.GSON;

/**
 * 基于 Google-Gson 实现的JSON服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = GSON, order = 30)
@DependsOnClass(Gson.class)
class GoogleGsonJSONServiceImpl implements IJSONService {

	@NonNull
	@Override
	public <T> T toBean(@NonNull String text, @NonNull Class<T> targetClazz) throws JSONParseException {
		try {
			return new Gson().fromJson(text, targetClazz);
		} catch (Exception e) {
			throw new JSONParseException("JSON字符串转Bean失败", e);
		}
	}

	@Override
	public <T> T toBean(@NonNull String text, @NonNull Type targetType) throws JSONParseException {
		try {
			return new Gson().fromJson(text, targetType);
		} catch (Exception e) {
			throw new JSONParseException("JSON字符串转Bean失败", e);
		}
	}

	@NonNull
	@Override
	public <T> List<T> toList(@NonNull String text, @NonNull Class<T> targetClazz) throws JSONParseException {
		try {
			// 为了兼容低版本Gson，使用下面的代码
			//TypeToken<?> typeToken = TypeToken.getParameterized(List.class, targetClazz);
			TypeToken<?> typeToken = TypeToken.get($Gson$Types.newParameterizedTypeWithOwner(null, List.class, targetClazz));

			return new Gson().fromJson(text, typeToken.getType());
		} catch (Exception e) {
			throw new JSONParseException("JSON字符串转List失败", e);
		}
	}

	@NonNull
	@Override
	public String toJSONString(@Nullable Object obj) throws JSONParseException {
		try {
			return new Gson().toJson(obj);
		} catch (Exception e) {
			throw new JSONParseException("obj转JSON字符串失败", e);
		}
	}


	@NonNull
	@Override
	public String getName() {
		return GSON;
	}
}
