package com.mitsioulis.socialplatform.testingP;

import java.time.LocalDate;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

public class MandatoryRestAfterShiftTestModelAggregator implements ArgumentsAggregator {

	@Override
	public MandatoryRestAfterShiftTestModel aggregateArguments(ArgumentsAccessor accessor, ParameterContext context)
			throws ArgumentsAggregationException {
		// TODO Auto-generated method stub
		return new MandatoryRestAfterShiftTestModel(accessor.getInteger(0), accessor.getString(1),
				accessor.get(2, LocalDate.class), accessor.get(3, LocalDate.class), accessor.get(4, LocalDate.class),
				accessor.getBoolean(5));
	}

}
