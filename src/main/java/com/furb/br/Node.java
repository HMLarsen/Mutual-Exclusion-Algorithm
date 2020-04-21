package com.furb.br;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(of = { "id" })
@EqualsAndHashCode(of = { "id" })
public class Node {

	private final Coordenador coordenador = Coordenador.getInstance();
	private int id;
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private boolean ativo = true;

	public Node() {
		this.id = coordenador.proximoID();

		scheduler.schedule(run(),
				ThreadLocalRandom.current().nextInt(AppConstants.NOVO_NODE_INICIO, AppConstants.NOVO_NODE_LIMITE),
				TimeUnit.SECONDS);
	}

	private Runnable run() {
		return () -> {
			try {
				if (coordenador.getCoordenador() == null) {

					if (coordenador.getNodes().indexOf(this) == -1) {
						scheduler.shutdown();
						return;
					}

					elegir();
				}

				if (this.equals(coordenador.getCoordenador().getNode()) || coordenador.getNodes().indexOf(this) == -1) {
					scheduler.shutdown();
					return;
				}

				consumirRecurso();
				schedulerProximaExecucao();
			} catch (Exception e) {
				System.out.println(e);
			}
		};
	}

	private void elegir() {
		NodeCoordenador novoCoordenador = buscaNovoCoordenador();
		coordenador.setCoordenador(novoCoordenador);

		System.out.println(String.format("[%s] Processo de Elei��o finalizado. O novo coordenador � %s.",
				LocalDateTime.now(), novoCoordenador));
	}

	private NodeCoordenador buscaNovoCoordenador() {
		System.out.println(String.format("[%s] Processo de Elei��o iniciado pelo %s.", LocalDateTime.now(), this));
		coordenador.setNovaEleicao(true);
		return getCoordenador(new NodeCoordenador(this));
	}

	private void consumirRecurso() {
		System.out.println(String.format("[%s] Processo %s solicitou consumir um recurso.", LocalDateTime.now(), this));
		if (!coordenador.isUsandoRecurso()) {
			coordenador.getCoordenador().travarRecurso(this);
		} else {

			coordenador.getCoordenador().getFila().add(this);
			System.out.println(String.format("[%s] Node %s foi adicionado a fila.", LocalDateTime.now(), this));
		}
	}

	private void schedulerProximaExecucao() {
		int proximo = ThreadLocalRandom.current().nextInt(AppConstants.NOVO_NODE_INICIO, AppConstants.NOVO_NODE_LIMITE);

		System.out.println(
				String.format("[%s] Execu��o finalizada. Processo %s agendou a pr�xima execu��o. Daqui %s segundos.",
						LocalDateTime.now(), this, proximo));

		scheduler.schedule(run(), proximo, TimeUnit.SECONDS);
	}

	private NodeCoordenador getCoordenador(NodeCoordenador nodeAtual) {
		List<Node> listaCoordenador = CoordenadorUtils.listaOrdenada().stream()
				.filter(n -> n.id > nodeAtual.getNode().getId()).collect(Collectors.toList());

		List<NodeCoordenador> possivelCoordenador = listaCoordenador.stream().map(n -> {
			return new NodeCoordenador(n);
		}).collect(Collectors.toList());

		if (possivelCoordenador.isEmpty()) {
			return new NodeCoordenador(this);
		}
		return getCoordenador(possivelCoordenador.get(0));
	}

}
