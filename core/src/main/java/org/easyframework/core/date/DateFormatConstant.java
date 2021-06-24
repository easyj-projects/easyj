package org.easyframework.core.date;

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

	String MM_UNSIGN = "yyyyMM"; // 精确到月，长度：6
	String DD_UNSIGN = "yyyyMMdd"; // 精确到日，长度：8
	String MI_UNSIGN = "yyyyMMddHHmm"; // 精确到分，长度：12
	String SS_UNSIGN = "yyyyMMddHHmmss"; // 精确到秒，长度：14
	String SSS_UNSIGN = "yyyyMMddHHmmssSSS"; // 完整格式，精确到毫秒，长度：17

	String TIME_MM = "HH:mm";
	String TIME_SS = "HH:mm:ss";
}
