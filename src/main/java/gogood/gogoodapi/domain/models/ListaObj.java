package gogood.gogoodapi.domain.models;


public class ListaObj<T> {

    private T[] vetor;
    private int nroElem;

    public ListaObj(int tamanho) {
        nroElem = 0;
        vetor = (T[]) new Object[tamanho];
    }

    public ListaObj(T itens[]){
        vetor = (T[]) new Object[itens.length];
        nroElem = 0;
        int i = 0;
        while (!this.isFull()){
            this.adiciona(itens[i]);
            i++;
        }
    }
    public boolean isEmpty(){
        return nroElem == 0;
    }
    public boolean isFull(){
        return nroElem >= vetor.length;
    }
    public void adiciona(T elemento) {
        if(nroElem < vetor.length){
            vetor[nroElem] = elemento;
            nroElem++;
        }else{
            throw new IllegalStateException();
        }

    }
    public void setElemento(int indice, T elemento){
       vetor[indice] = elemento;
    }

    public int busca(T elementoBuscado) {
        int indice = -1;
        for (int i = 0; i < nroElem; i++) {
            if(vetor[i].equals(elementoBuscado)) indice = i;
        }
        return indice;
    }


    public boolean removePeloIndice(int indice) {
        if(indice > nroElem || indice <0 ){
            return false;
        }
        vetor[indice] = null;
        moveList(indice);
        nroElem--;
        return true;
    }

    public boolean removeElemento(T elementoARemover) {
        return removePeloIndice(busca(elementoARemover));
    }

    public int getTamanho() {
        return nroElem;
    }


    public T getElemento(int indice) {
        if(indice > -1 && indice < nroElem){
            return vetor[indice];
        }
        return null;
    }


    public void limpa() {
        for (int i = 0; i < nroElem; i++) {
            vetor[i] = null;
        }
        nroElem = 0;
    }

    public void exibe() {


        for (int i = 0; i < getTamanho(); i++) {
            System.out.print(vetor[i].toString());
        }


    }

    public T[] getVetor() {
        return vetor;
    }

    private void moveList(int indice){
        for (int i = indice; i < vetor.length-1; i++) {
            if (vetor[i] == null){
                vetor[i] = vetor[i+1];
                vetor[i+1] = null;
            }
        }
    }
}
