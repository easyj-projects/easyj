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
package icu.easyj.core.code.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import icu.easyj.core.util.PatternUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 代码解析类
 *
 * @author wangliang181230
 */
public class CodeAnalysisUtils {

	//region 解析参数列表

	/**
	 * 解析参数列表
	 *
	 * @param parametersStr      含所有参数的串（不要包含其他代码内容）
	 * @param limitParameterSize 限制获取参数数量
	 * @return parameters 返回参数列表
	 */
	@Nullable
	public static Object[] analysisParameters(String parametersStr, int limitParameterSize) {
		if (!StringUtils.hasLength(parametersStr) || limitParameterSize <= 0) {
			return null;
		}

		Matcher mParameter = PatternUtils.P_CODE_DATA_VALUE.matcher(parametersStr);
		List<Object> parameterList = new ArrayList<>();
		String parameter;
		while (parameterList.size() < limitParameterSize && mParameter.find()) {
			parameter = mParameter.group();

			/*----- 获取 null、boolean 的值 -----*/
			switch (parameter) {
				case "null":
					parameterList.add(null);
					continue;
				case "true":
					parameterList.add(true);
					continue;
				case "false":
					parameterList.add(false);
					continue;
				default:
					break;
			}

			/*----- 获取 数字 的值 -----*/
			if (!parameter.startsWith("'") && !parameter.startsWith("\"")) {
				if (parameter.contains(".")) {
					parameterList.add(Double.valueOf(parameter));
				} else {
					try {
						parameterList.add(Integer.valueOf(parameter));
					} catch (NumberFormatException e) {
						parameterList.add(Long.valueOf(parameter));
					}
				}
				continue;
			}

			/*----- 获取 字符串 的值 -----*/
			// 将前后两个单引号去除掉
			String c = String.valueOf(parameter.charAt(0));
			parameter = parameter.substring(1, parameter.length() - 1);
			// 字符串内前面带有反斜杠的引号，把前面的反斜杠去掉
			if (parameter.contains("\\" + c)) {
				parameter = parameter.replace("\\" + c, c);
			}
			parameterList.add(parameter);
		}

		// 列表转换为数组
		if (parameterList.isEmpty()) {
			return null;
		} else {
			return parameterList.toArray(new Object[0]);
		}
	}

	// 重载方法
	@Nullable
	public static Object[] analysisParameters(String parametersStr) {
		return analysisParameters(parametersStr, Integer.MAX_VALUE);
	}

	//endregion


	//region 解析简单代码行

	/**
	 * 解析简单代码行
	 *
	 * @param code               简单代码行
	 * @param limitParameterSize 限制获取参数的数量
	 * @param isRigorous         是否为严谨的校验
	 * @return result 返回命令解析结果
	 */
	@Nullable
	public static CodeAnalysisResult analysisCode(String code, int limitParameterSize, boolean isRigorous) {
		if (code == null) {
			throw new IllegalArgumentException("code must be not null");
		}

		Matcher m = (isRigorous ? PatternUtils.P_CODE_LINE : PatternUtils.P_CODE_LINE2).matcher(code.trim());
		if (!m.matches()) {
			// 不匹配的串
			return null;
		}

		// 功能开发阶段，打印匹配信息出来观察一下
		//PatternUtils.printMatcherGroups(m);

		CodeAnalysisResult result = new CodeAnalysisResult();

		// 获取变量名
		String variableName = (m.groupCount() > 0 ? m.group(1) : null);
		result.setVariableName(variableName);

		// 获取属性名或方法名
		String fieldOrMethodName = (m.groupCount() >= 4 ? m.group(3) : null);
		if (code.contains("(")) {
			// 设置方法名
			result.setMethodName(fieldOrMethodName);

			// 获取方法参数
			String parametersStr = (m.groupCount() >= 6 ? m.group(5) : null);
			Object[] parameters = analysisParameters(parametersStr, limitParameterSize);
			result.setParameters(parameters);
		} else {
			// 设置属性名
			result.setFieldName(fieldOrMethodName);
		}

		return result;
	}

	// 重载方法
	public static CodeAnalysisResult analysisCode(String code, boolean isRigorous) {
		return analysisCode(code, Integer.MAX_VALUE, isRigorous);
	}

	// 重载方法
	public static CodeAnalysisResult analysisCode(String code, int limitParameterSize) {
		return analysisCode(code, limitParameterSize, false);
	}

	// 重载方法
	public static CodeAnalysisResult analysisCode(String code) {
		return analysisCode(code, Integer.MAX_VALUE, false);
	}

	//endregion
}
