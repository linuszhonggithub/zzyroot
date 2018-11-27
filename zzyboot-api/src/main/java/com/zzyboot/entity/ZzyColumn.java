package com.zzyboot.entity;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ZzyColumn {
	boolean isprimkey() default false;
	boolean iskeyword() default false;
	boolean isfrozen() default false;
	String name() default "";
	String label() default "";
	String fieldtype() default "text";
	int minlength() default 0;
	int seq() default 100;
	boolean ispassword() default false;
	String showright() default "";
	String editright() default "";
}
