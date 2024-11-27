package com.ivanogor.t1metricsstarter.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PACKAGE)
public @interface DataSourceLogging {
    /**
     * Значение метрики.
     * По умолчанию пустая строка.
     *
     * @return Значение метрики.
     */
    String value() default "";
}
