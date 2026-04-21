package arvores;

public class Parente {
    private No avo, pai, atual, tio, sobrinhoPerto, sobrinhoLonge;

    public Parente(){
        this.avo = null;
        this.pai = null;
        this.atual = null;
        this.tio = null;
        this.sobrinhoLonge = null;
        this.sobrinhoPerto = null;
    }

    public No getPai(){
        return pai;
    }

    public void setPai(No pai){
        this.pai = pai;
    }

    public No getAvo(){
        return avo;
    }

    public void setAvo(No avo){
        this.avo = avo;
    }

    public No getTio(){
        return tio;
    }

    public void setTio(No tio){
        this.tio = tio;
    }

    public No getSobrinhoPerto(){
        return sobrinhoPerto;
    }

    public void setSobrinhoPerto(No sobrinhoPerto){
        this.sobrinhoPerto = sobrinhoPerto;
    }

    public No getSobrinhoLonge(){
        return sobrinhoLonge;
    }

    public void setSobrinhoLonge(No sobrinhoLonge){
        this.sobrinhoLonge = sobrinhoLonge;
    }

    public No getAtual(){
        return atual;
    }

    public void setAtual(No atual){
        this.atual = atual;
    }

}
