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
