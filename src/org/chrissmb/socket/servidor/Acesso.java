package org.chrissmb.socket.servidor;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Anotação para definição de acesso. Podem ser utilizadas na classe de rota
 * ou no método (ação).
 * @author Christopher Monteiro
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Acesso {
	String value();
}
