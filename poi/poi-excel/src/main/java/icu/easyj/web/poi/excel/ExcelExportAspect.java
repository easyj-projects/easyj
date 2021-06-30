package icu.easyj.web.poi.excel;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import icu.easyj.core.constant.FileTypeConstants;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.web.util.HttpUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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


	/**
	 * Excel导出器
	 */
	private final IExcelExporter excelExporter;

	/**
	 * 构造函数
	 *
	 * @param excelExporter Excel导出器
	 */
	public ExcelExportAspect(IExcelExporter excelExporter) {
		this.excelExporter = excelExporter;
	}


	@Pointcut("@annotation(icu.easyj.web.poi.excel.ExcelExport)")
	private void pointcutExcelExport() {
	}

	/**
	 * 执行查询，然后导出excel文件
	 *
	 * @param jp 连接点
	 * @return 返回查询结果
	 * @throws Throwable 异常
	 */
	@Around("pointcutExcelExport()")
	public Object doQueryAndExportExcel(ProceedingJoinPoint jp) throws Throwable {
		Object result = jp.proceed();

		if (Boolean.TRUE.equals(DISABLED_LOCAL.get())) {
			enable();
			return result;
		}

		if (HttpUtils.isDoExportRequest()) {
			MethodSignature ms = (MethodSignature)jp.getSignature();
			Method method = ms.getMethod();

			if (result == null || result instanceof List) {
				ExcelExport annotation = method.getAnnotation(ExcelExport.class);

				// 准备参数
				HttpServletResponse response = HttpUtils.getResponse();
				List dataList = result == null ? Collections.emptyList() : (List)result;
				Class dataType = annotation.dataType();
				String fileName = HttpUtils.buildExportFileName(annotation.fileNamePre(), FileTypeConstants.EXCEL_2007);

				// 转为excel并导出
				excelExporter.toExcelAndExport(response, dataList, dataType, fileName);

				// 文件导出，无需返回值
				return null;
			} else {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("返回数据不是列表数据，不支持Excel文件导出，请将注解`@{}`从方法`{}`上移除。",
							ExcelExport.class.getSimpleName(), ReflectionUtils.methodToString(method));
				}
			}
		}

		return result;
	}
}
