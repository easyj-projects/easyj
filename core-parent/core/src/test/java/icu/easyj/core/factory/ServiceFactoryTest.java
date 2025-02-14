package icu.easyj.core.factory;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceFactoryTest {

	@Test
	void test() {
		List<ITestService> serviceList = new ArrayList<>();

		ITestService a = new TestServiceImplA();
		ITestService b1 = new TestServiceImplB1();
		ITestService b2 = new TestServiceImplB2();

		serviceList.add(a);
		serviceList.add(b2);
		serviceList.add(b1);

		ServiceFactory<ITestService> serviceFactory = new ServiceFactory<>(ITestService.class, serviceList);

		// getList
		List<ITestService> aList = serviceFactory.getList("a");
		assertEquals(1, aList.size());
		List<ITestService> bList = serviceFactory.getList("b");
		assertEquals(2, bList.size());

		// default
		assertEquals(a, serviceFactory.getDefault());
		// A
		assertEquals(a, serviceFactory.get("a"));
		assertEquals(a, aList.get(0));
		// B1
		assertEquals(b1, serviceFactory.get("b"));
		assertEquals(b1, bList.get(0));
		// B2
		assertEquals(b2, bList.get(1));
	}

}
