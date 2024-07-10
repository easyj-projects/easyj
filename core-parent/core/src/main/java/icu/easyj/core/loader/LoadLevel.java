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
package icu.easyj.core.loader;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import icu.easyj.core.loader.factory.IServiceFactory;
import icu.easyj.core.loader.factory.impls.DefaultServiceFactory;
import org.springframework.core.Ordered;

/**
 * The interface Load level.
 * 注：从阿里的seata项目中复制过来的。
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
	int order() default Ordered.LOWEST_PRECEDENCE;

	/**
	 * Scope enum.
	 *
	 * @return the scope
	 */
	Scope scope() default Scope.SINGLETON;

	/**
	 * Validator classes
	 *
	 * @return the validator classes
	 */
	Class<? extends IServiceLoaderValidator>[] validators() default {};

	/**
	 * Factory class
	 *
	 * @return the factory class
	 */
	Class<? extends IServiceFactory> factory() default DefaultServiceFactory.class;
}
