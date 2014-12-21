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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.ReflectionUtils;
import org.terasoluna.gfw.common.codelist.AbstractCodeList;

public class EnumCodeList extends AbstractCodeList {
    private Class<? extends Enum<?>> enumClass;

    public void setEnumClass(Class<? extends Enum<?>> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Map<String, String> asMap() {
        Map<String, String> codeList = new LinkedHashMap<String, String>();
        Method method = ReflectionUtils.findMethod(enumClass, "values");

        Enum<?>[] result = (Enum<?>[]) ReflectionUtils.invokeMethod(method,
                enumClass);
        for (Enum<?> e : result) {
            CodeListItem item = (CodeListItem) e;
            codeList.put(item.getCodeValue(), item.getCodeLabel());
        }

        return Collections.unmodifiableMap(codeList);
    }

}
