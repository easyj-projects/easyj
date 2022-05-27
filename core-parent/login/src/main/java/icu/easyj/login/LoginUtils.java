/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.login;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import icu.easyj.core.context.ContextUtils;
import icu.easyj.core.convert.ConvertUtils;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 登录工具类
 *
 * @author wangliang181230
 */
public abstract class LoginUtils {

	private static final String CONTEXT_KEY = "loginInfo";


	//region 登录对象

	/**
	 * 获取登录对象
	 *
	 * @return loginInfo 登录信息，未登录时，返回空
	 */
	@Nullable
	public static LoginInfo<?, ?> getLoginInfo() {
		return ContextUtils.get(CONTEXT_KEY);
	}

	/**
	 * 设置登录对象
	 *
	 * @param loginInfo 登录信息
	 */
	public static void setLoginInfo(LoginInfo<?, ?> loginInfo) {
		Assert.notNull(loginInfo, "'loginInfo' must be not null");
		Assert.notNull(loginInfo.getLoginId(), "'loginId' must be not null");
		Assert.notNull(loginInfo.getUserId(), "'userId' must be not null");
		Assert.notNull(loginInfo.getLoginTime(), "'loginTime' must be not null");

		ContextUtils.put(CONTEXT_KEY, loginInfo);
	}

	/**
	 * 从上下文移除登录信息
	 */
	public static void clear() {
		ContextUtils.remove(CONTEXT_KEY);
	}

	/**
	 * 判断是否已登录
	 *
	 * @return isLogined true=已登录 | false=未登录或已过期
	 */
	public static boolean isLogined() {
		return getLoginInfo() != null;
	}

	//endregion


	//region 获取指定登录信息

	/**
	 * 获取单个登录信息
	 *
	 * @param function
	 * @param <R>      返回数据的类
	 * @return 返回登录信息
	 */
	private static <R> R get(Function<LoginInfo<?, ?>, R> function) {
		LoginInfo<?, ?> loginInfo = getLoginInfo();
		if (loginInfo != null) {
			return function.apply(loginInfo);
		}

		return null;
	}

	/**
	 * 获取登录凭证
	 *
	 * @return 返回登录凭证
	 */
	public static String getToken() {
		return get(LoginInfo::getToken);
	}

	/**
	 * 获取登录唯一标识
	 *
	 * @return 返回登录唯一标识
	 */
	public static String getLoginId() {
		return get(LoginInfo::getLoginId);
	}

	/**
	 * 获取登录账号ID
	 *
	 * @param <AID> 登录账号ID数据类
	 * @return 返回登录账号ID
	 */
	public static <AID extends Serializable> AID getAccountId() {
		return (AID)get(LoginInfo::getAccountId);
	}

	/**
	 * 获取登录用户ID
	 *
	 * @param <UID> 用户ID数据类
	 * @return 返回登录用户ID
	 */
	public static <UID extends Serializable> UID getUserId() {
		return (UID)get(LoginInfo::getUserId);
	}

	/**
	 * 获取登录用户姓名
	 *
	 * @return 返回登录用户姓名
	 */
	public static String getUserName() {
		return get(LoginInfo::getUserName);
	}

	/**
	 * 获取主要角色
	 *
	 * @return 返回主要角色
	 */
	public static String getMainRole() {
		return get(LoginInfo::getMainRole);
	}

	/**
	 * 获取角色列表
	 *
	 * @return 返回角色列表
	 */
	public static List<String> getRoles() {
		return get(LoginInfo::getRoles);
	}

	/**
	 * 获取登录时间
	 *
	 * @return 返回登录时间
	 */
	public static Date getLoginTime() {
		return get(LoginInfo::getLoginTime);
	}

	/**
	 * 获取登录时间
	 *
	 * @return 返回登录时间
	 */
	public static Date getLoginExpiredTime() {
		return get(LoginInfo::getLoginExpiredTime);
	}

	/**
	 * 获取登录关联数据
	 *
	 * @return 返回登录关联数据
	 */
	public static Map<String, Object> getData() {
		return get(LoginInfo::getData);
	}

	/**
	 * 获取登录关联数据<br>
	 * 建议使用 {@link #getData(String, Class)} 方法
	 *
	 * @param key 键
	 * @param <D> 数据类型
	 * @return 返回登录关联数据
	 */
	@Nullable
	public static <D> D getData(String key) {
		Map<String, Object> map = get(LoginInfo::getData);
		if (MapUtils.isEmpty(map)) {
			return null;
		}

		return (D)map.get(key);
	}

	/**
	 * 获取登录关联数据
	 *
	 * @param key   键
	 * @param clazz 数据类
	 * @param <D>   数据类型
	 * @return 返回登录关联数据
	 */
	@Nullable
	public static <D> D getData(String key, Class<D> clazz) {
		Map<String, Object> map = get(LoginInfo::getData);
		if (MapUtils.isEmpty(map)) {
			return null;
		}

		Object data = map.get(key);
		if (data == null) {
			return null;
		}

		return ConvertUtils.convert(data, clazz);
	}

	//endregion
}
