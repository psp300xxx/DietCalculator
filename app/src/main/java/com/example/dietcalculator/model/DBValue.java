package com.example.dietcalculator.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DBValue {

    String columnName() default "";




}
