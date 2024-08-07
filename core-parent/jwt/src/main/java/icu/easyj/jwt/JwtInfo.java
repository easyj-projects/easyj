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
package icu.easyj.jwt;

import icu.easyj.core.convert.ConvertUtils;
import icu.easyj.core.util.MapUtils;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * JWT信息
 *
 * @author wangliang181230
 */
public class JwtInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String jwtId;
	private Map<String, Object> claims;
	private Date issuedAt;
	private Date expiresAt;


	public JwtInfo() {
	}

	public JwtInfo(String jwtId, Map<String, Object> claims, Date issuedAt, Date expiresAt) {
		this.jwtId = jwtId;
		this.claims = claims;
		this.issuedAt = issuedAt;
		this.expiresAt = expiresAt;
	}

	@Nullable
	public Object getClaims(String key) {
		if (MapUtils.isEmpty(this.claims)) {
			return null;
		}
		return this.claims.get(key);
	}


	@Nullable
	public <T> T getClaims(String key, Class<T> targetClass) {
		if (MapUtils.isEmpty(this.claims)) {
			return null;
		}
		return ConvertUtils.convert(this.claims.get(key), targetClass);
	}


	//region Getter、Setter

	public String getJwtId() {
		return jwtId;
	}

	public void setJwtId(String jwtId) {
		this.jwtId = jwtId;
	}

	public Map<String, Object> getClaims() {
		return claims;
	}

	public void setClaims(Map<String, Object> claims) {
		this.claims = claims;
	}

	public Date getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	//endregion
}
