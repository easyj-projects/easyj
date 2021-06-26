package org.easyj.core.constant;

/**
 * 时间格式化相关常量
 *
 * @author wangliang181230
 */
public interface DateFormatConstant {

	String MM = "yyyy-MM"; // 精确到月，长度：7
	String DD = "yyyy-MM-dd"; // 精确到日，长度：10
	String MI = "yyyy-MM-dd HH:mm"; // 精确到分，长度：16
	String SS = "yyyy-MM-dd HH:mm:ss"; // 精确到秒，长度：19
	String SSS = "yyyy-MM-dd HH:mm:ss.SSS"; // 完整格式，精确到毫秒，长度：23

	String MM2 = "yyyy/MM"; // 精确到月，长度：7
	String DD2 = "yyyy/MM/dd"; // 精确到日，长度：10
	String MI2 = "yyyy/MM/dd HH:mm"; // 精确到分，长度：16
	String SS2 = "yyyy/MM/dd HH:mm:ss"; // 精确到秒，长度：19
	String SSS2 = "yyyy/MM/dd HH:mm:ss.SSS"; // 完整格式，精确到毫秒，长度：23

	String MM_UNSIGNED = "yyyyMM"; // 精确到月，长度：6
	String DD_UNSIGNED = "yyyyMMdd"; // 精确到日，长度：8
	String MI_UNSIGNED = "yyyyMMddHHmm"; // 精确到分，长度：12
	String SS_UNSIGNED = "yyyyMMddHHmmss"; // 精确到秒，长度：14
	String SSS_UNSIGNED = "yyyyMMddHHmmssSSS"; // 完整格式，精确到毫秒，长度：17

	String TIME_MM = "HH:mm"; // 无日期，精确到分钟，长度：5
	String TIME_SS = "HH:mm:ss"; // 无日期，精确到秒，长度：8
}
