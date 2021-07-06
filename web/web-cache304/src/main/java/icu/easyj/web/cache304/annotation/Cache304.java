/*
 *  Copyright 2021 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package icu.easyj.web.cache304.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import icu.easyj.web.cache304.Cache304Constants;
import icu.easyj.web.cache304.Cache304Utils;
import icu.easyj.web.cache304.config.Cache304Config;
import org.springframework.core.annotation.AliasFor;

/**
 * 304缓存注解
 *
 * @author wangliang181230
 * @see Cache304AnnotationParser
 * @see Cache304Config
 * @see Cache304Utils
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Cache304 {

	/**
	 * 缓存有效时间<br>
	 * 单位：秒<br>
	 * 说明：当 {@code value <= 0} 时，使用默认值：1天<br>
	 * 同义于：{@link #cacheSeconds()}<br>
	 *
	 * @return cacheSeconds 缓存秒数
	 */
	@AliasFor("cacheSeconds")
	long value() default Cache304Constants.DEFAULT_CACHE_SECONDS;

	/**
	 * 缓存有效时间<br>
	 * 单位：秒<br>
	 * 说明：当 {@code value <= 0} 时，使用默认值：1天<br>
	 * 同义于：{@link #value()}<br>
	 *
	 * @return cacheSeconds 缓存秒数
	 */
	@AliasFor("value")
	long cacheSeconds() default Cache304Constants.DEFAULT_CACHE_SECONDS;

	/**
	 * 缓存天数
	 * 说明：缓存有效天数，有效时间延长至24点失效，也就是该缓存是以天为单位做缓存，不跨天。<br>
	 * 例子：1=今天晚上24失效；2=明天晚上24点失效。<br>
	 * 优先级：该配置优先级比value高，{@code cacheDays > 0}时才生效<br>
	 *
	 * @return cacheDays 缓存天数
	 */
	int cacheDays() default Cache304Constants.DEFAULT_CACHE_DAYS;

	/**
	 * 响应头中是否使用“Cache-Control: max-age=xxx”<br>
	 * 值域：true=是|false=否<br>
	 * Cache-Control响应头是告诉客户端该缓存的有效时间，单位：秒<br>
	 * 在缓存有效时间内，客户端甚至不会发送已缓存的请求。所以，建议设置为true<br>
	 * 某些特殊请求，如果想要由服务器端控制其304缓存机制，才需要设置为false<br>
	 *
	 * @return useMaxAge 是否使用`Cache-Control:max-age`响应头
	 */
	boolean useMaxAge() default Cache304Constants.DEFAULT_USE_MAX_AGE;

	/**
	 * 限制`maxAge`的值，避免服务端因为设置错误，设置了一个非常大的值，导致客户端缓存过久，使得这些用户一直读取着错误的缓存。<br>
	 * 设置了上限值后，单个客户端每过半天会有一个请求发送到服务端，由服务端决定是否响应304，并继续延续半天客户端缓存。<br>
	 *
	 * @return limitMaxAge maxAge上限值
	 */
	long limitMaxAge() default Cache304Constants.DEFAULT_LIMIT_MAX_AGE; // 默认值为-1，表示不启用该功能
}
