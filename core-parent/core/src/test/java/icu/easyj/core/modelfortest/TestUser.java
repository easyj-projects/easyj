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

import cn.hutool.core.annotation.Alias;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import icu.easyj.core.util.DateUtils;

/**
 * 测试用户类
 *
 * @author wangliang181230
 */
public class TestUser {

	@JSONField(name = "Name") // fastjson键别名
	@JsonProperty("Name") // jackson键别名
	@SerializedName("Name") // gson键别名
	@Alias("Name") // hutool键别名
	private String name;

	@JSONField(name = "Age") // fastjson键别名
	@JsonProperty("Age") // jackson键别名
	@SerializedName("Age") // gson键别名
	@Alias("Age") // hutool键别名
	private Integer age;

	@JSONField(name = "Birthday") // fastjson键别名
	@JsonProperty("Birthday") // jackson键别名
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@SerializedName("Birthday") // gson键别名
	@Alias("Birthday") // hutool键别名
	private Date birthday;


	public TestUser() {
	}

	public TestUser(String name, Integer age, Date birthday) {
		this.name = name;
		this.age = age;
		this.birthday = birthday;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TestUser testUser = (TestUser)o;
		return Objects.equals(name, testUser.name)
				&& Objects.equals(age, testUser.age)
				&& Objects.equals(birthday, testUser.birthday);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, age, birthday);
	}

	@Override
	public String toString() {
		return "TestUser{" +
				"name='" + name + '\'' +
				", age=" + age +
				", birthday=" + DateUtils.toMilliseconds(birthday) +
				'}';
	}
}
