package gogood.gogoodapi.utils;

import gogood.gogoodapi.models.Rota;

public class Ordenacao {
    public static void ordenarRotaPorDuracao(Rota[] rotas){
        for (int i = 0; i < rotas.length; i++) {
            int indiceMenor = i;
            for (int j = i+1; j < rotas.length; j++) {

                if(rotas[j].getDuracaoNumerica() < rotas[indiceMenor].getDuracaoNumerica()){
                    indiceMenor = j;
                }
            }
            Rota aux = rotas[i];
            rotas[i] = rotas[indiceMenor];
            rotas[indiceMenor] = aux;
        }
    }
}
