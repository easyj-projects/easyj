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
package icu.easyj.middleware.dwz.server.core.domain.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import icu.easyj.middleware.dwz.server.core.domain.enums.DwzLogStatus;
import org.springframework.lang.NonNull;

/**
 * 短链接记录 实体
 * 对应表：easyj_dwz_log
 *
 * @author wangliang181230
 */
public class DwzLogEntity {

	private Long id;

	private String shortUrlCode;

	private String longUrl;

	/**
	 * 有效期至（为空表示永久有效）
	 */
	private Date termOfValidity;

	/**
	 * 状态：0=无效,1=有效,2=已过期（根据策略不同，过期的数据可能会直接被删除）
	 */
	private Integer status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date createTime;

	private Date updateTime;

	private Integer version;


	public DwzLogEntity() {
	}

	public DwzLogEntity(Long id, String shortUrlCode, String longUrl, Date termOfValidity, Integer status, Date createTime, Date updateTime, Integer version) {
		this.id = id;
		this.shortUrlCode = shortUrlCode;
		this.longUrl = longUrl;
		this.termOfValidity = termOfValidity;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.version = version;
	}

	public DwzLogEntity(Long id, String shortUrlCode, String longUrl, Date termOfValidity, DwzLogStatus statusEnum, Date createTime, Date updateTime, Integer version) {
		this.id = id;
		this.shortUrlCode = shortUrlCode;
		this.longUrl = longUrl;
		this.termOfValidity = termOfValidity;
		this.status = statusEnum.getStatus();
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.version = version;
	}


	//region Getter、Setter

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortUrlCode() {
		return shortUrlCode;
	}

	public void setShortUrlCode(String shortUrlCode) {
		this.shortUrlCode = shortUrlCode;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public Date getTermOfValidity() {
		return termOfValidity;
	}

	public void setTermOfValidity(Date termOfValidity) {
		this.termOfValidity = termOfValidity;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setStatus(@NonNull DwzLogStatus statusEnum) {
		this.status = statusEnum.getStatus();
	}

	public boolean isStatus(DwzLogStatus statusEnum) {
		return this.status == statusEnum.getStatus();
	}

	public boolean isNotStatus(DwzLogStatus statusEnum) {
		return !this.isStatus(statusEnum);
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	//endregion
}
