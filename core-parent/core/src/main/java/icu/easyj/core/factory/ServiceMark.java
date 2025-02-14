/*
 * Copyright 2021-2025 the original author or authors.
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
package icu.easyj.core.factory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;

/**
 * 环境类型枚举
 *
 * @author wangliang181230
 * @since 0.8.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ServiceMark {

	/**
	 * 服务代码（同 code 属性，设置一个即可，两个都设置时，优先读取 code 属性）
	 * 注：大小写不敏感
	 *
	 * @return code 返回服务代码
	 */
	String value() default "";

	/**
	 * 服务代码（同 value 属性，设置一个即可，两个都设置时，优先读取 code 属性）
	 * 注：大小写不敏感
	 *
	 * @return code 返回服务代码
	 */
	String code() default "";

	/**
	 * 是否默认服务
	 *
	 * @return isDefault 返回是否默认服务
	 */
	boolean isDefault() default false;

	/**
	 * 服务排序编号
	 *
	 * @return order 返回服务排序编号
	 */
	int order() default Ordered.LOWEST_PRECEDENCE;

}
