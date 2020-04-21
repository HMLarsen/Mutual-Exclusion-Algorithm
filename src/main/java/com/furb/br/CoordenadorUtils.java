package com.furb.br;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe utilitaria para {@link Coordenador} singleton.
 * 
 * @author Lucas Vanderlinde
 */
public class CoordenadorUtils {

	private static final Coordenador coordenador = Coordenador.getInstance();

	public static List<Node> listaOrdenada() {
		List<Node> listaCoordenador = new ArrayList<>(coordenador.getNodes());
		listaCoordenador.sort(Comparator.comparing(Node::getId));

		return listaCoordenador;
	}
}
