package am.ik.categolj2.infra.dozer;

import java.util.List;

import org.dozer.DozerConverter;

import am.ik.categolj2.domain.model.Categories;

@SuppressWarnings("rawtypes")
public class StringToCategoryListConverter extends DozerConverter<String, List> {

	public StringToCategoryListConverter() {
		super(String.class, List.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String convertFrom(List source, String destination) {
		return Categories.toString(source);
	}

	@Override
	public List convertTo(String source, List destination) {
		return Categories.fromCategory(source).getCategories();
	}
}
