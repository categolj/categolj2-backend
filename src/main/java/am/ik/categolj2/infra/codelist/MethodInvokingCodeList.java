package am.ik.categolj2.infra.codelist;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.MethodInvoker;
import org.terasoluna.gfw.common.codelist.AbstractReloadableCodeList;

public class MethodInvokingCodeList extends AbstractReloadableCodeList {

	private String valueField;

	private String labelField;

	private MethodInvoker methodInvoker;

	private ConversionService conversionService = new DefaultFormattingConversionService();

	public void setValueField(String valueField) {
		this.valueField = valueField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	public void setMethodInvoker(MethodInvoker methodInvoker) {
		this.methodInvoker = methodInvoker;
	}

	@Override
	protected Map<String, String> retrieveMap() {
		try {
			methodInvoker.prepare();
			List<?> targetList = (List<?>) methodInvoker.invoke();
			Map<String, String> map = new LinkedHashMap<>();
			for (Object target : targetList) {
				BeanWrapper beanWrapper = PropertyAccessorFactory
						.forBeanPropertyAccess(target);
				Object valueObject = beanWrapper.getPropertyValue(valueField);
				Object labelObject = beanWrapper.getPropertyValue(labelField);
				String value = conversionService.convert(valueObject,
						String.class);
				String label = conversionService.convert(labelObject,
						String.class);
				map.put(value, label);
			}
			return map;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
