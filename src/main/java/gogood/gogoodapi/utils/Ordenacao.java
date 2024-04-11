package gogood.gogoodapi.utils;

import gogood.gogoodapi.models.Rota;

import java.util.Arrays;
import java.util.List;

public class Ordenacao {
    public static List<Rota> ordenarRotaPorDuracao(Rota[] rotas){
        Rota[] rotasOrdenadas;
        for (int i = 0; i < rotas.length; i++) {
            int indiceMenor = i;

            for (int j = i+1; j < rotas.length; j++) {

                if(rotas[i].getDuracaoSegundos() > rotas[j].getDuracaoSegundos()){
                    indiceMenor = j;
                }
            }
            Rota aux = rotas[i];
            rotas[i] = rotas[indiceMenor];
            rotas[indiceMenor] = aux;
        }

        rotasOrdenadas = rotas;
        return  Arrays.stream(rotasOrdenadas).toList();
    }
}
