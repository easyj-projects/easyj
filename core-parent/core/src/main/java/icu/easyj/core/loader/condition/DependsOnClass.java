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
package icu.easyj.core.loader.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务依赖的类
 *
 * @author wangliang181230
 * @see DependsOnClassValidator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DependsOnClass {

	/**
	 * 依赖的类数组
	 *
	 * @return the classes
	 */
	Class<?>[] value() default {};

	/**
	 * 依赖的类名数组（无法引用到类时使用）
	 *
	 * @return the class names
	 */
	String[] name() default {};

	/**
	 * 校验策略
	 * <p>
	 * 注：只针对 {@link #name()} 有效，因为 {@link #value()} 中任意一个不存在时，直接抛出异常了，无法校验
	 *
	 * @return the strategy
	 */
	ValidateStrategy strategy() default ValidateStrategy.ALL;
}
