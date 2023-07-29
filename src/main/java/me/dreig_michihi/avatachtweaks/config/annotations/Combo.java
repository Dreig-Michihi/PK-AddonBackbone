package me.dreig_michihi.avatachtweaks.config.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target({TYPE})
@Retention(RUNTIME)
public @interface Combo {
	String LEFT_CLICK = ":LEFT_CLICK";
	String RIGHT_CLICK = ":RIGHT_CLICK";
	String LEFT_CLICK_ENTITY = ":LEFT_CLICK_ENTITY";
	String RIGHT_CLICK_ENTITY = ":RIGHT_CLICK_ENTITY";
	String RIGHT_CLICK_BLOCK = ":RIGHT_CLICK_BLOCK";
	String SNEAK_DOWN = ":SHIFT_DOWN";
	String SNEAK_UP = ":SHIFT_UP";
	String OFFHAND_TRIGGER = ":OFFHAND_TRIGGER";
	String[] value();
}
