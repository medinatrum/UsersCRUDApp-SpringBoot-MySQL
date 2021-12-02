package com.example.demo.utility;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class MapperUtility {
    public static <T> T map(Object source, Class<T> destination) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, destination);
    }

    public static <T> T mapStrict(Object source, Class<T> destination) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper.map(source, destination);
    }
}
