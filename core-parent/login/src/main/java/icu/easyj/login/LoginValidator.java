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
package icu.easyj.login;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 允许访问当前操作的角色
 *
 * @author wangliang181230
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LoginValidator {

	/**
	 * 允许调用当前Action的人员角色列表
	 *
	 * @return 人员角色列表
	 */
	String[] value() default {};

	/**
	 * 允许调用当前Action的登录状态
	 *
	 * @return 允许的登录状态
	 */
	AllowLoginStatus allowState() default AllowLoginStatus.LOGIN_ONLY; // 默认：仅登录可调用

	/**
	 * 是否生效：此属性主要用于在类上添加了注解后，想要排除某个接口时使用
	 * <p>
	 * 值域：<br>
	 * true  = 生效（默认）：会校验登录用户角色。<br>
	 * false = 不生效：不会校验。使用场景：接口类上添加了当前注解，但想排除众多方法中的某个方法时使用。
	 *
	 * @return 是否生效
	 */
	boolean exist() default true; // 默认：生效
}
