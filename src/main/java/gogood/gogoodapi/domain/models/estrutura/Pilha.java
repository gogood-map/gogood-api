package gogood.gogoodapi.domain.models.estrutura;

public class Pilha<T> {
    T pilha[];
    int topo;

    public Pilha(int tamanho) {
        this.pilha = (T[]) new Object[tamanho];
        this.topo = -1;
    }
    public boolean isEmpty(){
        return topo == -1;
    }

    public boolean isFull(){
        return topo == pilha.length - 1;
    }


    public void push(T valor){
        if(isFull()){
            System.out.println("Pilha já cheia");
        }
        topo++;
        pilha[topo] = valor;
        System.out.println("Valor inserido na pilha");
    }
    public T pop(){
        if (isEmpty()) return null;

        T valorTopo = pilha[topo];
        topo--;
        return valorTopo;
    }

    public T peek(){
        if (isEmpty()) return null;

        return pilha[topo];
    }

    public void exibe(){
        if(isEmpty()){
            System.out.println("Está vazia");
        }
        for (int i = 0; i < topo; i++) {
            System.out.println(pilha[i]);
        }
    }

}
