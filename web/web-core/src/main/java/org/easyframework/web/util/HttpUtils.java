package org.easyframework.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import static org.easyframework.web.constant.HttpConstant.GET_METHOD;
import static org.easyframework.web.constant.HttpConstant.NO_CACHE;
import static org.easyframework.web.constant.HttpConstant.NO_STORE;
import static org.easyframework.web.constant.HttpConstant.POST_METHOD;

/**
 * Http工具类
 *
 * @author wangliang181230
 */
public class HttpUtils {

	/**
	 * 判断是否为GET请求
	 *
	 * @param request 请求实例
	 * @return isGetRequest 是否为GET请求
	 */
	public static boolean isGetRequest(HttpServletRequest request) {
		return GET_METHOD.equalsIgnoreCase(request.getMethod());
	}

	/**
	 * 判断是否为POST请求
	 *
	 * @param request 请求实例
	 * @return isPostRequest 是否为POST请求
	 */
	public static boolean isPostRequest(HttpServletRequest request) {
		return POST_METHOD.equalsIgnoreCase(request.getMethod());
	}

	/**
	 * 判断是否为无缓存请求
	 *
	 * @param request 请求实例
	 * @return isNoCacheRequest 是否为无缓存请求
	 */
	public static boolean isNoCacheRequest(HttpServletRequest request) {
		String cacheControl = request.getHeader(HttpHeaders.CACHE_CONTROL);
		if (!StringUtils.isEmpty(cacheControl)) {
			cacheControl = cacheControl.toLowerCase();
			return cacheControl.contains(NO_CACHE) || cacheControl.contains(NO_STORE);
		}
		return false;
	}

	/**
	 * 设置304响应状态
	 *
	 * @param response 响应实例
	 * @return null
	 */
	public static Object setCache304AndReturnNull(HttpServletResponse response) {
		response.setStatus(HttpStatus.NOT_MODIFIED.value());
		return null;
	}
}
