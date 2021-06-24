package org.easyframework.web.cache304.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.easyframework.web.cache304.Cache304Constant;
import org.easyframework.web.cache304.Cache304Utils;
import org.easyframework.web.cache304.config.Cache304Config;

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
	long value() default Cache304Constant.DEFAULT_CACHE_SECONDS;

	/**
	 * 缓存有效时间<br>
	 * 单位：秒<br>
	 * 说明：当 {@code value <= 0} 时，使用默认值：1天<br>
	 * 同义于：{@link #value()}<br>
	 *
	 * @return cacheSeconds 缓存秒数
	 */
	long cacheSeconds() default Cache304Constant.DEFAULT_CACHE_SECONDS;

	/**
	 * 缓存天数
	 * 说明：缓存有效天数，有效时间延长至24点失效，也就是该缓存是以天为单位做缓存，不跨天。<br>
	 * 例子：1=今天晚上24失效；2=明天晚上24点失效。<br>
	 * 优先级：该配置优先级比value高，{@code cacheDays > 0}时才生效<br>
	 *
	 * @return cacheDays 缓存天数
	 */
	int cacheDays() default Cache304Constant.DEFAULT_CACHE_DAYS;

	/**
	 * 响应头中是否使用“Cache-Control: max-age=xxx”<br>
	 * 值域：true=是|false=否<br>
	 * Cache-Control响应头是告诉客户端该缓存的有效时间，单位：秒<br>
	 * 在缓存有效时间内，客户端甚至不会发送已缓存的请求。所以，建议设置为true<br>
	 * 某些特殊请求，如果想要由服务器端控制其304缓存机制，才需要设置为false<br>
	 *
	 * @return useMaxAge 是否使用`Cache-Control:max-age`响应头
	 */
	boolean useMaxAge() default Cache304Constant.DEFAULT_USE_MAX_AGE;
}
