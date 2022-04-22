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
package icu.easyj.core.context.impls;

import icu.easyj.core.context.ContextUtils;
import icu.easyj.core.context.IContextCleaner;
import icu.easyj.core.loader.LoadLevel;

/**
 * 上下文清理者
 *
 * @author wangliang181230
 */
@LoadLevel(name = "context", order = 0)
public class ContextCleaner implements IContextCleaner {

	@Override
	public void clear() {
		ContextUtils.clear();
	}
}
