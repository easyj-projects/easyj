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
package icu.easyj.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.StrPool;
import icu.easyj.core.constant.DateFormatConstants;
import icu.easyj.web.constant.HttpConstants;
import icu.easyj.web.constant.HttpHeaderConstants;
import icu.easyj.web.exception.RequestContextNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static icu.easyj.web.constant.HttpConstants.GET_METHOD;
import static icu.easyj.web.constant.HttpConstants.POST_METHOD;
import static icu.easyj.web.constant.HttpHeaderConstants.NO_CACHE;
import static icu.easyj.web.constant.HttpHeaderConstants.NO_STORE;

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

	// 重载方法
	public static boolean isGetRequest() {
		return isGetRequest(getRequest());
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

	// 重载方法
	public static boolean isNotGetRequest() {
		return !isGetRequest();
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

	// 重载方法
	public static boolean isPostRequest() {
		return isPostRequest(getRequest());
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

	// 重载方法
	public static boolean isNotPostRequest() {
		return !isPostRequest();
	}

	/**
	 * 判断是否为无缓存请求
	 *
	 * @param request 请求实例
	 * @return isNoCacheRequest 是否为无缓存请求
	 */
	public static boolean isNoCacheRequest(HttpServletRequest request) {
		String cacheControl = request.getHeader(HttpHeaders.CACHE_CONTROL);
		if (StringUtils.hasLength(cacheControl)) {
			cacheControl = cacheControl.toLowerCase();
			return cacheControl.contains(NO_CACHE) || cacheControl.contains(NO_STORE);
		}
		return false;
	}

	// 重载方法
	public static boolean isNoCacheRequest() {
		return isNoCacheRequest(getRequest());
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

	// 重载方法
	public static void setResponseStatus304() {
		setResponseStatus304(getResponse());
	}

	/**
	 * 设置响应信息，使该请求不允许被缓存
	 *
	 * @param response 响应实例
	 */
	public static void setResponseNotAllowCache(HttpServletResponse response) {
		response.setHeader(HttpHeaders.CACHE_CONTROL, HttpHeaderConstants.NO_CACHE);
		response.setDateHeader(HttpHeaders.EXPIRES, 0);
	}


	//endregion


	//region 文件下载相关

	/**
	 * 判断是否为导出文件请求
	 *
	 * @param request 请求实例
	 * @return isDoExportRequest 是否为导出文件请求
	 */
	public static boolean isDoExportRequest(HttpServletRequest request) {
		return HttpConstants.DO_EXPORT_PARAM_VALUE.equalsIgnoreCase(request.getParameter(HttpConstants.DO_EXPORT_PARAM_NAME));
	}

	// 重载方法
	public static boolean isDoExportRequest() {
		return isDoExportRequest(getRequest());
	}

	/**
	 * 生成导出文件的文件名
	 *
	 * @param fileNamePre 文件名前缀
	 * @param fileSuffix  文件后续名
	 * @return fileName 导出的文件名
	 */
	public static String buildExportFileName(String fileNamePre, String fileSuffix) {
		if (!fileNamePre.endsWith(StrPool.UNDERLINE)) {
			fileNamePre += StrPool.UNDERLINE;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormatConstants.SSS_UNSIGNED);
		return fileNamePre + sdf.format(new Date()) + (fileSuffix.charAt(0) != CharPool.DOT ? CharPool.DOT : "") + fileSuffix;
	}

	//endregion
}
