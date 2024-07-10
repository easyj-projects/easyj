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
 * 服务依赖的JAR及其版本
 *
 * @author wangliang181230
 * @see DependsOnJarVersionValidator
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DependsOnJarVersion {

	/**
	 * 依赖的JAR包名称数组<br>
	 * 由于部分jar变更过名字，所以可设置多个。
	 * <p>
	 * JAR包名称格式包含组名和包名，用冒号分隔开来，例：icu.easyj:easyj-all、com.alibaba:fastjson
	 *
	 * @return the jar names
	 */
	String[] name();

	/**
	 * 依赖的JAR包最小版本号
	 *
	 * @return the min version
	 */
	String minVersion() default "";

	/**
	 * 依赖的JAR包最大版本号
	 *
	 * @return the min version
	 */
	String maxVersion() default "";
}
