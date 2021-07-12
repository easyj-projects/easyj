package icu.easyj.web.constant;


/**
 * 各Filter的Order值
 * 值越大越晚执行
 *
 * @author wangliang181230
 */
public interface FilterOrderConstants {

	/**
	 * SpringMVC将Request存入ThreadLocal的过滤器的Order值。
	 * 自定义的Filter如果需要用到框架中的HttpUtil获取信息的话，则需要比此Order值要大，
	 * 否则RequestContextHolder.getRequestAttributes().getRequest()为空导致获取不到请求信息。
	 */
	int SPRING_MVC_REQUEST_SET_TO_THREAD_LOCAL = -105;

	/**
	 * 内部请求判断过滤器
	 */
	int INTERNAL_REQUEST = -100;

	/**
	 * 请求方式过滤
	 */
	int REQUEST_TYPE = -95;

	/**
	 * 无效请求拦截
	 */
	int INVALID_REQUEST = -90;

	/**
	 * 链路跟踪相关
	 */
	int TRACE = -85;

	/**
	 * 授权账号过滤器
	 */
	int APP_ACCOUNT = -80;

	/**
	 * 参数加密解密
	 */
	int PARAM_ENCRYPT = -75;
}
