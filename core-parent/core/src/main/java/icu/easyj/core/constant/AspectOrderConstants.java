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
package icu.easyj.core.constant;

import org.springframework.core.Ordered;

/**
 * AOP的Order值常量
 *
 * @author wangliang181230
 */
public interface AspectOrderConstants {

	int CACHE304 = Ordered.HIGHEST_PRECEDENCE + 100;

	int LOGIN_VALIDATOR = Ordered.HIGHEST_PRECEDENCE + 200;

	int EXCEL_EXPORT = Ordered.HIGHEST_PRECEDENCE + 300;
}
