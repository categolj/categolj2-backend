package am.ik.categolj2.infra.dozer;

import org.dozer.DozerConverter;
import org.joda.time.DateTime;

public class DateTimeConverter extends DozerConverter<DateTime, DateTime> {

	public DateTimeConverter() {
		super(DateTime.class, DateTime.class);
	}

	@Override
	public DateTime convertFrom(DateTime source, DateTime destination) {
		return source;
	}

	@Override
	public DateTime convertTo(DateTime source, DateTime destination) {
		return source;
	}
}
