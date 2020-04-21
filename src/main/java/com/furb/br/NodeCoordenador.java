package com.furb.br;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = { "node" })
public class NodeCoordenador {

	private final Coordenador coordenador = Coordenador.getInstance();
	private Queue<Node> fila = new LinkedList<>();
	private Node node;

	public NodeCoordenador(Node node) {
		this.node = node;
	}

	public void travarRecurso(Node n) {
		new Thread(() -> {
			coordenador.novoConsumoRecurso(n);
			try {
				Thread.sleep(ThreadLocalRandom.current().nextInt(AppConstants.TRAVAR_RECURSO_INICIO,
						AppConstants.TRAVAR_RECURSO_LIMITE));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			coordenador.pararConsumoRecurso(n);

			NodeCoordenador nodeCoordenador = coordenador.getCoordenador();
			if (nodeCoordenador != null) {
				Node next = nodeCoordenador.getFila().poll();
				if (next != null) {
					travarRecurso(next);
				}
			}
		}).run();
	}

}
