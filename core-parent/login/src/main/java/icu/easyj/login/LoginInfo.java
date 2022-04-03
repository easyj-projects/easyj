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

import icu.easyj.core.convert.ConvertUtils;
import org.springframework.lang.Nullable;

/**
 * 登录信息
 *
 * @author wangliang181230
 */
public class LoginInfo<AID extends Serializable, UID extends Serializable> implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 登录凭证
	 */
	private String token;

	/**
	 * 此次登录唯一标识（如：随机生成的UUID、JWT的ID...等等）
	 */
	private String loginId;

	/**
	 * 登录账号ID
	 */
	private AID accountId;

	/**
	 * 登录用户ID（部分系统账号与用户信息分开的）
	 */
	private UID userId;

	/**
	 * 登录用户姓名
	 */
	private String userName;

	/**
	 * 登录客户端
	 */
	private String loginClient;

	/**
	 * 主要角色
	 */
	private String mainRole;

	/**
	 * 角色列表
	 */
	private List<String> roles;

	/**
	 * 登录时间
	 */
	private Date loginTime;

	/**
	 * 登录过期时间，为空表示不过期（即：永久有效）
	 */
	private Date loginExpiredTime;

	/**
	 * 登录关联数据
	 */
	private Map<String, Object> data;


	//region Getter、Setter

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public AID getAccountId() {
		return accountId;
	}

	public void setAccountId(AID accountId) {
		this.accountId = accountId;
	}

	public void setAccountId(Object accountId) {
		this.accountId = (AID)accountId;
	}

	public UID getUserId() {
		return userId;
	}

	public void setUserId(UID userId) {
		this.userId = userId;
	}

	public void setUserId(Object userId) {
		this.userId = (UID)userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginClient() {
		return loginClient;
	}

	public void setLoginClient(String loginClient) {
		this.loginClient = loginClient;
	}

	public String getMainRole() {
		return mainRole;
	}

	public void setMainRole(String mainRole) {
		this.mainRole = mainRole;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLoginExpiredTime() {
		return loginExpiredTime;
	}

	public void setLoginExpiredTime(Date loginExpiredTime) {
		this.loginExpiredTime = loginExpiredTime;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Nullable
	public Object getData(String key) {
		if (this.data == null) {
			return null;
		}
		return this.data.get(key);
	}

	@Nullable
	public <T> T getData(String key, Class<T> targetClass) {
		if (this.data == null) {
			return null;
		}
		return ConvertUtils.convert(this.data.get(key), targetClass);
	}

	//endregion
}
