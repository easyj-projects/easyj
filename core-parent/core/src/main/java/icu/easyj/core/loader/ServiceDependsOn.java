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
package icu.easyj.core.loader;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务依赖配置的注解
 *
 * @author wangliang181230
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ServiceDependsOn {

	/**
	 * 依赖于哪些类
	 *
	 * @return the classes
	 */
	Class<?>[] classes() default {};

	/**
	 * 依赖的最低版本的Java，小于等于0时，表示不限制最低版本<br>
	 * 值域如：1.1* ~ 1.8*、9.** ~ 1*.**
	 *
	 * @return the min java version
	 */
	float minJavaVersion() default 0;

	/**
	 * 依赖的最高版本的Java，小于等于0时，表示不限制最高版本。<br>
	 * 注意：设置该值时，要注意小版本的设置。举例：设置为 17F 时，也许你想设置的是 17.99F，以包含所有Java17的小版本<br>
	 * 值域如：1.1* ~ 1.8*、9.** ~ 1*.**
	 *
	 * @return the max java version
	 */
	float maxJavaVersion() default 0;
}
