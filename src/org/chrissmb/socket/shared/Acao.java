package org.chrissmb.socket.shared;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Utilizados nos métodos das classes de rotas para definir acões disponíveis.
 * @author Christopher Monteiro
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Acao {
	String value();
}
