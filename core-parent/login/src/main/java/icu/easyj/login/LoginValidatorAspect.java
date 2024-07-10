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
package icu.easyj.login;

import java.lang.reflect.Method;

import icu.easyj.core.constant.AspectOrderConstants;
import icu.easyj.core.util.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

/**
 * 人员角色校验AOP
 */
@Aspect
@Order(AspectOrderConstants.LOGIN_VALIDATOR)
public class LoginValidatorAspect {

	private final ILoginValidatorExceptionHandler loginValidatorExceptionHandler;


	public LoginValidatorAspect(ILoginValidatorExceptionHandler loginValidatorExceptionHandler) {
		this.loginValidatorExceptionHandler = loginValidatorExceptionHandler;
	}


	@Pointcut(value = "@within(icu.easyj.login.LoginValidator) || @annotation(icu.easyj.login.LoginValidator)")
	public static void pointCutLoginValidate() {
	}

	@Before("pointCutLoginValidate()")
	public void loginValidate(JoinPoint jp) throws Exception {
		MethodSignature signature = (MethodSignature)jp.getSignature();
		Method method = signature.getMethod();

		LoginValidator anno = method.getAnnotation(LoginValidator.class);
		if (anno == null) {
			anno = method.getDeclaringClass().getAnnotation(LoginValidator.class);
			if (anno == null) {
				return; // 注解为空，不需要校验
			}
		}

		// 判断功能是否生效
		if (!anno.exist()) {
			return;
		}

		// 判断当前登录状态是否允许调用
		boolean isLogined = LoginUtils.isLogined();
		if (isLogined) { // 已登录
			if (anno.allowState() == AllowLoginStatus.UN_LOGIN_ONLY) {
				this.loginValidatorExceptionHandler.handle("ONLY_ALLOW_UN_LOGIN", "该接口未登录状态下才可调用");
			}
		} else { // 未登录
			if (anno.allowState() == AllowLoginStatus.LOGIN_ONLY) {
				this.loginValidatorExceptionHandler.handle("NOT_LOGIN", "您还未登录或登录已过期");
			}
		}

		// 如果登录了，那么需要校验人员角色
		if (isLogined) {
			// 获取注解中配置的人员角色列表
			String[] roles = anno.value();
			if (ArrayUtils.isEmpty(roles)) {
				return; // 未配置，无需校验
			}

			// 获取登录用户角色
			String mainRole = LoginUtils.getMainRole();
			// 判断该角色是否允许访问
			for (String role : roles) {
				if (role.equals(mainRole)) {
					return; // 匹配到了角色，允许访问接口
				}
			}

			// 未匹配到任何角色，直接报错，不允许访问接口
			this.loginValidatorExceptionHandler.handle("NOT_ALLOW_ROLE", "不允许当前用户访问该接口");
		}
	}

}