package com.furb.br;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

public class Coordenador {

	private static Coordenador INSTANCE;

	@Getter
	private volatile List<Node> nodes = new CopyOnWriteArrayList<>();

	@Getter
	@Setter
	private volatile NodeCoordenador coordenador;
	@Getter
	@Setter
	private volatile boolean novaEleicao;
	private volatile boolean usandoRecurso = false;

	private Coordenador() {
	}

	public static Coordenador getInstance() {
		return INSTANCE == null ? INSTANCE = new Coordenador() : INSTANCE;
	}

	public void novoProcesso() {
		Node node = new Node();
		Boolean isCoordenadorAdicionado = adicionaCoordenador(node);

		nodes.add(node);
		System.out.println(String.format("[%s] Processo %s criado.", LocalDateTime.now(), node));

		if (isCoordenadorAdicionado) {
			System.out.println(String.format("[%s] Processo %s virou coordenador.", LocalDateTime.now(), node));
		}
	}

	private boolean adicionaCoordenador(Node node) {
		if (nodes.isEmpty()) {
			coordenador = new NodeCoordenador(node);
			return true;
		} else {
			return false;
		}

	}

	public void matarCoordenador() {
		if (coordenador == null) {
			return;
		}

		Node node = nodes.get(nodes.indexOf(coordenador.getNode()));
		nodes.remove(node);

		System.out.println(String.format("[%s] Coordenador %s morreu.", LocalDateTime.now(), node));

		coordenador = null;
		System.gc();
	}

	public int proximoID() {
		Boolean vazio = false;
		int novoID = 0;

		do {
			int tempID = new Random().nextInt(1000);
			novoID = tempID;

			vazio = nodes.parallelStream().filter(n -> {
				return n.getId() == tempID;
			}).collect(Collectors.toList()).isEmpty();

		} while (!vazio);
		return novoID;
	}

	public boolean isUsandoRecurso() {
		return usandoRecurso;
	}

	public void novoConsumoRecurso(Node n) {
		this.usandoRecurso = true;
		System.out.println(String.format("[%s] Processo %s est� consumindo o recurso.", LocalDateTime.now(), n));
	}

	public void pararConsumoRecurso(Node n) {
		this.usandoRecurso = false;
		System.out.println(String.format("[%s] Processo %s parou de consumir o recurso.", LocalDateTime.now(), n));
	}

}