package icu.easyj.core.util.model;

/**
 * 测试用的不存在默认构造函数的类
 *
 * @author wangliang181230
 */
public class NotExistsNoArgsConstructorTestClass extends TestSuperClass {

	private String f1;

	NotExistsNoArgsConstructorTestClass(String f1) {
		this.f1 = f1;
	}

	public String getF1() {
		return f1;
	}

	public void setF1(String f1) {
		this.f1 = f1;
	}
}
