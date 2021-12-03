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
package icu.easyj.middleware.dwz.server.core.store;

import java.util.Date;

import icu.easyj.core.exception.NotSupportedException;
import icu.easyj.data.store.StoreException;
import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;

/**
 * 短链接记录 存取接口
 *
 * @author wangliang181230
 */
public interface IDwzLogStore {

	/**
	 * 生成短链接记录ID的序列名
	 */
	String SEQ_NAME__DWZ_LOG_ID = "SEQ_DWZ_LOG_ID";


	/**
	 * 关联短链接码与长链接，并保存到store中
	 *
	 * @param longUrl        长链接
	 * @param termOfValidity 有效期截止时间（为空表示永久有效）
	 * @return 保存是否成功
	 * @throws StoreException 存储接口异常
	 */
	DwzLogEntity save(String longUrl, Date termOfValidity);

	/**
	 * 根据长链接，获取有效的短链接记录数据。主要用于避免同一长链接重复创建多条数据。
	 *
	 * @param longUrl 长链接
	 * @return 短链接记录
	 * @throws StoreException 存储接口异常
	 */
	DwzLogEntity getByLongUrlForUpdate(String longUrl);

	/**
	 * 更新成功
	 *
	 * @param dwzLog 短链接记录
	 * @throws StoreException 存储接口异常
	 */
	void update(DwzLogEntity dwzLog);

	/**
	 * 根据短链接码，获取长链接
	 *
	 * @param shortUrlCode 短链接码
	 * @return longUrl 长链接
	 * @throws StoreException 存储接口异常
	 */
	String getLongUrlByShortUrlCode(String shortUrlCode);

	/**
	 * 获取当前存储器中保存的短链接记录的最大ID值
	 *
	 * @return 最大ID值
	 * @throws NotSupportedException 如果当前存储器无法获取最大ID值，请抛出该异常
	 */
	Long getMaxId();

	/**
	 * 删除超时的短链接数据
	 *
	 * @return 此次删除的数据量
	 */
	int deleteOvertime();

	/**
	 * 更新超时的短链接数据状态
	 *
	 * @return 此次更新的数据量
	 */
	int updateOvertime();
}
