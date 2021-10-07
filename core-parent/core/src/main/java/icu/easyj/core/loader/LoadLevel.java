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
 * The interface Load level.
 * 注：从阿里的seata项目中复制过来的，并扩展了`dependOnClasses`属性。
 *
 * @author slievrly
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LoadLevel {
	/**
	 * Name string.
	 *
	 * @return the string
	 */
	String name();

	/**
	 * Order int.
	 *
	 * @return the int
	 */
	int order() default 0;

	/**
	 * Scope enum.
	 *
	 * @return the scope
	 */
	Scope scope() default Scope.SINGLETON;

	/**
	 * Depend on the classes.
	 *
	 * @return the classes
	 */
	Class<?>[] dependOnClasses() default {};

	/**
	 * Depend on the min java version
	 *
	 * @return the min java version
	 */
	float dependOnMinJavaVersion() default 0;

	/**
	 * Depend on the max java version
	 *
	 * @return the max java version
	 */
	float dependOnMaxJavaVersion() default 0;
}
