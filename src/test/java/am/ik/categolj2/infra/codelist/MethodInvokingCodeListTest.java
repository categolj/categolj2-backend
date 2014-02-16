/*
 * Copyright (C) 2014 Toshiaki Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
