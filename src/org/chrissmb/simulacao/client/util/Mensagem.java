package org.chrissmb.simulacao.client.util;

import javax.swing.JOptionPane;

public class Mensagem {
	public static void alert(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Alerta", JOptionPane.INFORMATION_MESSAGE);
	}
}
