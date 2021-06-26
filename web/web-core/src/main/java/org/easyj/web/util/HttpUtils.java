package org.easyj.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easyj.web.exception.RequestContextNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.easyj.web.constant.HttpConstant.GET_METHOD;
import static org.easyj.web.constant.HttpConstant.POST_METHOD;
import static org.easyj.web.constant.HttpHeaderConstant.NO_CACHE;
import static org.easyj.web.constant.HttpHeaderConstant.NO_STORE;

/**
 * Http工具类
 *
 * @author wangliang181230
 */
public abstract class HttpUtils {

	//region 获取`HttpServletRequest`和`HttpServletResponse`

	/**
	 * 获取 HttpServletRequest
	 *
	 * @return request 请求实例
	 * @throws RequestContextNotFoundException HTTP请求上下文不存在的异常
	 */
	@NonNull
	public static HttpServletRequest getRequest() throws RequestContextNotFoundException {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			throw new RequestContextNotFoundException();
		}

		// 上下文存在时，request肯定存在，无需判断空
		return requestAttributes.getRequest();
	}

	/**
	 * 获取 HttpServletResponse
	 *
	 * @return response 响应实例
	 * @throws RequestContextNotFoundException HTTP请求上下文不存在的异常
	 */
	@NonNull
	public static HttpServletResponse getResponse() throws RequestContextNotFoundException {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			throw new RequestContextNotFoundException();
		}

		// 上下文存在时，response不一定已经存在，判断一下空值。
		HttpServletResponse response = requestAttributes.getResponse();
		if (response == null) {
			throw new RequestContextNotFoundException();
		}

		return response;
	}

	//endregion


	//region 校验请求信息

	/**
	 * 判断是否GET请求
	 *
	 * @param request 请求实例
	 * @return isGetRequest 是否为GET请求
	 */
	public static boolean isGetRequest(HttpServletRequest request) {
		return GET_METHOD.equalsIgnoreCase(request.getMethod());
	}

	/**
	 * 判断是否不是GET请求
	 *
	 * @param request 请求实例
	 * @return isGetRequest 是否为GET请求
	 */
	public static boolean isNotGetRequest(HttpServletRequest request) {
		return !isGetRequest(request);
	}

	/**
	 * 判断是否POST请求
	 *
	 * @param request 请求实例
	 * @return isPostRequest 是否为POST请求
	 */
	public static boolean isPostRequest(HttpServletRequest request) {
		return POST_METHOD.equalsIgnoreCase(request.getMethod());
	}

	/**
	 * 判断是否不是POST请求
	 *
	 * @param request 请求实例
	 * @return isPostRequest 是否为POST请求
	 */
	public static boolean isNotPostRequest(HttpServletRequest request) {
		return !isPostRequest(request);
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

	//endregion

	//region 设置响应信息

	/**
	 * 设置响应状态为304
	 *
	 * @param response 响应实例
	 */
	public static void setResponseStatus304(HttpServletResponse response) {
		response.setStatus(HttpStatus.NOT_MODIFIED.value());
	}

	//endregion
}
