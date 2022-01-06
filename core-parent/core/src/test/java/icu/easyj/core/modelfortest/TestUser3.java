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
package icu.easyj.core.modelfortest;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 测试用户类2
 *
 * @author wangliang181230
 */
public class TestUser3 {

	private String userName;

	private Integer userAge;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date userBirthday;


	public TestUser3() {
	}

	public TestUser3(String userName, Integer userAge, Date userBirthday) {
		this.userName = userName;
		this.userAge = userAge;
		this.userBirthday = userBirthday;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserAge() {
		return userAge;
	}

	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}

	public Date getUserBirthday() {
		return userBirthday;
	}

	public void setUserBirthday(Date userBirthday) {
		this.userBirthday = userBirthday;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TestUser3 testUser = (TestUser3)o;
		return Objects.equals(userName, testUser.userName)
				&& Objects.equals(userAge, testUser.userAge)
				&& Objects.equals(userBirthday, testUser.userBirthday);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userName, userAge, userBirthday);
	}
}
