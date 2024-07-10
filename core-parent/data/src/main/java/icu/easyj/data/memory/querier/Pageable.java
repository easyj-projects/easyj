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
package icu.easyj.data.memory.querier;

/**
 * 可分页的接口
 *
 * @author wangliang181230
 */
public interface Pageable {

	/**
	 * Gets page number
	 *
	 * @return the page number
	 */
	int getPageNumber();

	/**
	 * Sets page number
	 *
	 * @param pageNumber the page number
	 */
	void setPageNumber(int pageNumber);

	/**
	 * Gets page size
	 *
	 * @return the page size
	 */
	int getPageSize();

	/**
	 * Sets page size
	 *
	 * @param pageSize the page size
	 */
	void setPageSize(int pageSize);
}
