package org.easyframework.web.util;

import javax.servlet.http.HttpServletRequest;

import static org.easyframework.web.constant.HttpConstant.GET_METHOD;
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
	 * @param request 请求对象
	 * @return isGetRequest 是否为GET请求
	 */
	public static boolean isGetRequest(HttpServletRequest request) {
		return GET_METHOD.equalsIgnoreCase(request.getMethod());
	}

	/**
	 * 判断是否为POST请求
	 *
	 * @param request 请求对象
	 * @return isPostRequest 是否为POST请求
	 */
	public static boolean isPostRequest(HttpServletRequest request) {
		return POST_METHOD.equalsIgnoreCase(request.getMethod());
	}
}
