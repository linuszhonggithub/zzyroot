package com.zzyboot.entity;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ZzyTable {
	String showright() default "";
	String editright() default "";
	String insertright() default "";
	String deleteright() default "";
	String label() default "";
}
