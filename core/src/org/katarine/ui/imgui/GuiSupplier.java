package org.katarine.ui.imgui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks method as ImGui gui supplier.
 * This annotation can be only used on non-static methods with no parameters. Otherwise {@code MethodNotFoundException} will be thrown.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiSupplier {}
