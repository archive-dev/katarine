package org.katarine.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks inheritor of GameObject class as not spawnable and inheritor of Component class as not addable through GUI.
 *<p>
 *    Combination with {@link EditorObject} annotation will mark class as editor only object.
 *</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotInstantiatable {}