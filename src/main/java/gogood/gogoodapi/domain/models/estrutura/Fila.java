package gogood.gogoodapi.domain.models.estrutura;

public class Fila<T> {

    private T fila[];

    private int tamanho, inicio, fim;

    public Fila(int capacidade){
        fila = (T[]) new Object[capacidade];
        tamanho = 0;
        inicio = 0;
        fim = 0;
    }
    public Fila(T[] itens){
        fila = (T[]) new Object[itens.length];
        int i = 0;
        inicio = 0;
        fim = 0;
        while (!this.isFull()){
            this.insert(itens[i]);
            i++;
        }
    }


    public boolean isEmpty() {
        return tamanho == 0;
    }
    public boolean isFull() {
        return tamanho == fila.length;
    }

    public void insert(T info) {
        if (isFull()){

            throw new IllegalStateException("Fila já cheia");
        }
        tamanho++;

        fila[fim] = info;
        fim = (fim+1)%fila.length;
    }

    public T peek() {
        return fila[inicio];
    }
    public T poll() {
        if(isEmpty()){
            System.out.println("Nada a remover");
            return null;
        }
        T valorRemovido = fila[inicio];
        inicio = (inicio+1)%fila.length;
        tamanho--;
        return valorRemovido;
    }

    public T[] getFila(){
        T[]fila2 =(T[]) new Object[tamanho];
        int indice = inicio;
        for (int i = 0; i < tamanho; i++) {

            fila2[i]=fila[inicio];
            inicio = (inicio+1)%tamanho;
        }

        return fila2;
    }

}
