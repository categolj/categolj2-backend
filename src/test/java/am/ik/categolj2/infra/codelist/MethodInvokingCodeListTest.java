package am.ik.categolj2.infra.codelist;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.MethodInvoker;

import com.google.common.collect.Maps;

public class MethodInvokingCodeListTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Data
	@AllArgsConstructor
	public static class Priority {
		private Integer priority;
		private String name;
	}

	public static class PriorityService {
		public List<Priority> findAll() {
			return Arrays.asList(new Priority(1, "high"), new Priority(2,
					"middle"), new Priority(3, "low"));
		}
	}

	@Test
	public void test() {
		MethodInvokingCodeList codeList = new MethodInvokingCodeList();
		codeList.setLabelField("name");
		codeList.setValueField("priority");
		PriorityService service = new PriorityService();
		MethodInvoker methodInvoker = new MethodInvoker();
		methodInvoker.setTargetMethod("findAll");
		methodInvoker.setTargetObject(service);
		codeList.setMethodInvoker(methodInvoker);

		codeList.afterPropertiesSet();
		Map<String, String> expected = Maps.newLinkedHashMap();
		expected.put("1", "high");
		expected.put("2", "middle");
		expected.put("3", "low");
		assertThat(codeList.asMap(), is(expected));
	}

}
