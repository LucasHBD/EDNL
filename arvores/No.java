package arvores;

public class No {
    private int elemento;
    private No filhoEsquerdo, filhoDireito, pai;
    private String cor;

    public No(No pai, int elemento){
        this.elemento = elemento;
        this.pai = pai;
        this.filhoDireito = null;
        this.filhoEsquerdo = null;

        this.setCor("Vermelho");
    }

    public No getFilhoEsquerdo(){
        return filhoEsquerdo;
    }

    public void setFilhoEsquerdo(No filhoEsquerdo){
        this.filhoEsquerdo = filhoEsquerdo;
    }

    public No getFilhoDireito(){
        return filhoDireito;
    }

    public void setFilhoDireito(No filhoDireito){
        this.filhoDireito = filhoDireito;
    }

    public No getPai(){
        return pai;
    }

    public void setPai(No pai){
        this.pai = pai;
    }

    public String getCor(){
        return cor;
    }

    public void setCor(String cor){
        this.cor = cor;
    }

    public int getElemento(){
        return elemento;
    }

    public void setElemento(int elemento){
        this.elemento = elemento;
    }

}
