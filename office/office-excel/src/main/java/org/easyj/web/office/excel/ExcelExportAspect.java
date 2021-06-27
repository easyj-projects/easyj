package org.easyj.web.office.excel;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.easyj.core.util.DateUtils;
import org.easyj.core.util.ReflectionUtils;
import org.easyj.office.excel.util.ExcelExportUtils;
import org.easyj.web.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Excel文件导出切面类
 *
 * @author wangliang181230
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExcelExportAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportAspect.class);

	//region 当前切面的启用状态

	private static final ThreadLocal<Boolean> DISABLED_LOCAL = new ThreadLocal<>();

	public static void disable() {
		DISABLED_LOCAL.set(true);
	}

	public static void enable() {
		DISABLED_LOCAL.remove();
	}

	//endregion


	@Pointcut("@annotation(org.easyj.web.office.excel.ExcelExportSupport)")
	private void pointcutExcelExportSupport() {
	}

	/**
	 * 执行查询，然后导出excel文件
	 *
	 * @param jp 连接点
	 * @return 返回查询结果
	 * @throws Throwable 异常
	 */
	@Around("pointcutExcelExportSupport()")
	public Object doQueryAndExportExcel(ProceedingJoinPoint jp) throws Throwable {
		Object result = jp.proceed();

		if (Boolean.TRUE.equals(DISABLED_LOCAL.get())) {
			enable();
			return result;
		}

		HttpServletRequest request = HttpUtils.getRequest();

		String paramName = "doExcelExport";
		String paramValue = "true";

		if (paramValue.equalsIgnoreCase(request.getParameter(paramName))) {
			MethodSignature ms = (MethodSignature)jp.getSignature();
			Method method = ms.getMethod();

			if (result == null || result instanceof List) {
				ExcelExportSupport annotation = method.getAnnotation(ExcelExportSupport.class);
				String fileNamePre = annotation.fileNamePre();
				if (!fileNamePre.endsWith("_")) {
					fileNamePre += "_";
				}

				// 准备参数
				HttpServletResponse response = HttpUtils.getResponse();
				List dataList = result == null ? Collections.emptyList() : (List)result;
				Class dataType = annotation.dataType();
				String fileName = fileNamePre + DateUtils.toString(new Date()).replace(':', '：') + ".xlsx";

				// 转为excel并导出
				ExcelExportUtils.toExcelAndExport(response, dataList, dataType, fileName);

				// 文件导出，无需返回值
				return null;
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("返回数据不是列表数据，不支持Excel文件导出，请将注解`@{}`从方法`{}`上移除。",
							ExcelExportSupport.class.getSimpleName(), ReflectionUtils.methodToString(method));
				}
			}
		}

		return result;
	}
}
