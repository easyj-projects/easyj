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
package icu.easyj.core.util.json.impls;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.util.IJSONService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 基于 jackson 实现的JSON服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = "jackson", order = 20)
@DependsOnClass(ObjectMapper.class)
class JacksonJSONServiceImpl implements IJSONService {

	private final ObjectMapper mapper = new ObjectMapper();

	@NonNull
	@Override
	public String getName() {
		return "jackson";
	}

	@NonNull
	@Override
	public <T> T toBean(@NonNull String text, @NonNull Class<T> targetClazz) throws JsonProcessingException {
		return mapper.readValue(text, targetClazz);
	}

	@NonNull
	@Override
	public <T> List<T> toList(@NonNull String text, @NonNull Class<T> targetClazz) throws JsonProcessingException {
		return mapper.readerForListOf(targetClazz).readValue(text);
	}

	@NonNull
	@Override
	public String toJSONString(@Nullable Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}
}
