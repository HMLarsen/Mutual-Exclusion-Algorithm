package com.furb.br;

import java.util.concurrent.TimeUnit;

/**
 * Constantes para as tarefas
 * 
 * @author Lucas Vanderlinde
 *
 */
public interface AppConstants {

	public static final long FINALIZA_COORDENADOR_INTERVALO = TimeUnit.MINUTES.toMillis(1);
	public static final String FINALIZA_COORDENADOR_TEMPO = "FINALIZA_COORDENADOR_TEMPO";
	public static final String FINALIZA_COORDENADOR_METODO = "matarCoordenador";

	public static final long NOVO_PROCESSO_INTERVALO = TimeUnit.SECONDS.toMillis(40);
	public static final String NOVO_PROCESSO_TEMPO = "NOVO_PROCESSO_TEMPO";
	public static final String NOVO_PROCESSO_METODO = "novoProcesso";

	public static final short NOVO_NODE_INICIO = 10;
	public static final short NOVO_NODE_LIMITE = 26;

	public static final short TRAVAR_RECURSO_INICIO = 5000;
	public static final short TRAVAR_RECURSO_LIMITE = 15000;

}
