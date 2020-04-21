package com.furb.br;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Responsável por organizar todas as tarefas do ambiente.
 */
public class TaskScheduler {

	public void iniciaTarefas() {
		novaTarefa(AppConstants.FINALIZA_COORDENADOR_TEMPO, AppConstants.FINALIZA_COORDENADOR_INTERVALO,
				AppConstants.FINALIZA_COORDENADOR_METODO);
		novaTarefa(AppConstants.NOVO_PROCESSO_TEMPO, AppConstants.NOVO_PROCESSO_INTERVALO,
				AppConstants.NOVO_PROCESSO_METODO);
	}

	private void novaTarefa(String nomeTarefa, long intervalo, String nomeMetodo) {
		Timer timer = new Timer(nomeTarefa);
		TimerTask tarefa = new TimerTask() {
			public void run() {
				try {
					Coordenador coordenador = Coordenador.getInstance();
					Method metodo = coordenador.getClass().getMethod(nomeMetodo);
					metodo.invoke(coordenador);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Date inicio = getDataInicio(intervalo);
		timer.schedule(tarefa, inicio, intervalo);
	}

	private Date getDataInicio(long intervalo) {
		LocalDateTime data = LocalDateTime.now()
				.plusNanos(TimeUnit.NANOSECONDS.convert(intervalo, TimeUnit.MILLISECONDS));

		return Date.from(data.atZone(ZoneId.systemDefault()).toInstant());
	}

}
