package arvores;

public class ArvoreRN {

    private No raiz;
    private int tamanho;
    
    public ArvoreRN(){
        this.raiz = null;
        this.tamanho = 0;
    }

    public Boolean isEmpty(){
        return tamanho == 0;
    }

    public No raiz(){
        return raiz;
    }

    public Boolean hasLeft(No atual){
        return (atual.getFilhoEsquerdo() != null);
    }

    public Boolean hasRight(No atual){
        return (atual.getFilhoDireito() != null);
    }

    public int altura(No no){
        if(no == null){
            return -1;
        }

        int hEsq = altura(no.getFilhoEsquerdo());
        int hDir = altura(no.getFilhoDireito());

        return 1 + Math.max(hEsq, hDir);
    }

    public int profundidade(No no){
        if(no == raiz){
            return 0;
        }

        if(no == null){
            return -1;
        }

        return 1 + profundidade(no.getPai());
    }

    private void inOrderPrint(No no, String[][] matriz, int[] atualColuna){
        if(no == null){
            return;
        }

        if(hasLeft(no)){
            inOrderPrint(no.getFilhoEsquerdo(), matriz, atualColuna);
        }

        int linha = profundidade(no);
        int coluna = atualColuna[0]++;
        String corTexto = no.getCor().equalsIgnoreCase("vermelho") ? "\u001B[31m" : "\u001B[30m";
        String reset = "\u001B[0m";
        matriz[linha][coluna] = String.format("%s%d%s[%s]", corTexto, no.getElemento(), reset, no.getCor());

        if(hasRight(no)){
            inOrderPrint(no.getFilhoDireito(), matriz, atualColuna);
        }
    }

    public void printNo(No no){
        if(no == null){
            System.out.println("Nó nulo");
        }

        No pai = no.getPai();
        No filhoEsquerdo = no.getFilhoEsquerdo();
        No filhoDireito = no.getFilhoDireito();

        System.out.println("==== Nó ====");
        System.out.println("Chave: " + no.getElemento() + "| Cor: " + cor(no));
        System.out.println("Pai: " + (pai != null ? pai.getElemento() + "| Cor: " + cor(pai) : "null"));
        System.out.println("Filho Esquerdo: " + (filhoEsquerdo != null ? filhoEsquerdo.getElemento() + "| Cor: " + cor(filhoEsquerdo) : "null"));
        System.out.println("Filho Direito: " + (filhoDireito != null ? filhoDireito.getElemento() + "| Cor: " + cor(filhoDireito) : "null"));
        System.out.println("============");
    }

    public No buscar(No no, int elemento){
        if(no == null){
            return null;
        }
        if(elemento == no.getElemento()){
            return no;
        }
        if((elemento < no.getElemento() && no.getFilhoEsquerdo() == null) || (elemento > no.getElemento() && no.getFilhoDireito() == null)){
            return no;
        }
        else if (elemento > no.getElemento()) {
            return buscar(no.getFilhoDireito(), elemento);
        } else {
            return buscar(no.getFilhoEsquerdo(), elemento);
        }
    }

    public void print(){
        if(this.isEmpty()){
            System.out.println("Árvore Vazia.");
        }

        int altura = this.altura(raiz);
        int largura = (int) Math.pow(2, altura +1) * 3;
        String[][] matriz = new String[altura*2 + 1][largura];

        for(int i = 0; i < matriz.length; i++){
            for(int j = 0; j < matriz[i].length; j++){
                matriz[i][j] = " ";
            }
        }

        preencherMatriz(raiz, matriz, 0, largura / 2, largura / 4);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Árvore Rubro-Negra (Altura: " + altura + ", Tamanho: " + tamanho + ")");
        System.out.println("\n" + "=".repeat(60));
        
        for(int i = 0; i < matriz.length; i++){
            StringBuilder linha = new StringBuilder();
            for(int j = 0; j < matriz[i].length; j++){
                linha.append(matriz[i][j]);
            }
            System.out.println(linha.toString().replaceAll("\\s+$", ""));
        }
        System.out.println("=".repeat(60));
        System.out.println("Raiz: " + raiz.getElemento() + "[" + raiz.getCor() + "]");
        System.out.println("Altura Negra: " + calcularAlturaNegra(raiz));
        System.out.println("=".repeat(60));
    }

    private void preencherMatriz(No no, String[][] matriz, int linha, int coluna, int offset){
        if(no == null){
            return;
        }

        String cor = no.getCor();
        String corTexto = cor.equals("vermelho") ? "\u001B[31m" : "\u001B[30m";
        String reset = "\u001B[0m";

        String noStr = String.format("%s%d%s", corTexto, no.getElemento(), reset);
        matriz[linha][coluna] = noStr;

        if(hasLeft(no)){
            matriz[linha + 1][coluna - offset/2] = "/";
            preencherMatriz(no.getFilhoEsquerdo(), matriz, linha + 2, coluna - offset, offset/2);
        }

        if(hasRight(no)){
            matriz[linha + 1][coluna + offset/2] = "\\";
            preencherMatriz(no.getFilhoDireito(), matriz, linha + 2, coluna + offset, offset/2);
        }
    }

    private int calcularAlturaNegra(No no){
        if(no == null){
            return 0;
        }

        int alturaEsquerda = calcularAlturaNegra(no.getFilhoEsquerdo());
        int alturaDireita = calcularAlturaNegra(no.getFilhoDireito());
        int incremento = no.getCor().equals("preto") ? 1 : 0;

        return Math.max(alturaEsquerda, alturaDireita) + incremento;
    }

    public void inserir(int elemento){
        if(isEmpty()){
            No novo_no = new No(null, elemento);
            raiz = novo_no;
            mudarCor(novo_no, "preto");
            tamanho++;
        } else{
            No pai = buscar(raiz, elemento);
            if(pai.getElemento() == elemento){
                System.out.println("Não é permitido dois valores iguais");
            } else {
                tamanho++;
                No novo_no = new No(pai, elemento);

                if(elemento > pai.getElemento()){
                    pai.setFilhoDireito(novo_no);
                } else{
                    pai.setFilhoEsquerdo(novo_no);
                }
                balancearArvore(novo_no);
            }
        }
    }

    public void remover(int elemento){
        if(isEmpty()){
            System.out.println("Não é possível remover, árvore vazia");
        } else {
            No no_removido = buscar(raiz, elemento);
            if(no_removido.getElemento() != elemento){
                System.out.print("Nó não existe");
            } else {
                tamanho--;
                No no_sucessor;

                if(no_removido.getFilhoDireito() != null){
                    no_sucessor = sucessor(no_removido.getFilhoDireito());
                } else if(no_removido.getFilhoEsquerdo() != null) {
                    no_sucessor = no_removido.getFilhoEsquerdo();
                } else {
                    if(no_removido == raiz){
                        raiz = null;
                        return;
                    }
                    no_sucessor = no_removido; 
                }
                removerNos(no_removido, no_sucessor);
            } 
        }
    }

    private void balancearArvore(No atual){
        No pai = atual.getPai();

        if(pai == null || cor(pai) == "preto"){
            return;
        }

        Parente parentes = pegarParentes(pai);

        No avo = parentes.getAvo();
        No tio = parentes.getTio();
        No sobrinhoPerto = parentes.getSobrinhoPerto();
        No sobrinhoLonge = parentes.getSobrinhoLonge();

        //Caso 2

        if(cor(pai) == "vermelho" && cor(tio) == "vermelho" && cor(avo) == "preto"){
            mudarCor(pai, "preto");
            mudarCor(tio, "preto");
            mudarCor(avo, "vermelho");

            balancearArvore(avo);
        }

        //Caso3

        if(cor(pai) == "vermelho" && cor(avo) == "preto" && cor(tio) == "preto"){
            //Caso 3a
            if(!isFilhoDireito(pai) && !isFilhoDireito(atual)){
                Parente parente = simplesDireita(pai);
                pai = parente.getPai();
                avo = parente.getAvo();

                mudarCor(pai, "preto");
                mudarCor(avo, "vermelho");
            }
            //Caso 3b
            else if(isFilhoDireito(pai) && isFilhoDireito(atual)){
                Parente parente = simplesEsquerda(pai);
                pai = parente.getPai();
                avo = parente.getAvo();

                mudarCor(pai, "preto");
                mudarCor(avo, "vermelho");
            }
            //Caso 3c
            else if(isFilhoDireito(pai) && !isFilhoDireito(atual)){
                Parente parente = simplesDireita(atual);

                parente = simplesEsquerda(parente.getPai());

                pai = parente.getPai();
                avo = parente.getAvo();

                mudarCor(pai, "preto");
                mudarCor(avo, "vermelho");
            }
            //Caso 3d
            else if(!isFilhoDireito(pai) && isFilhoDireito(atual)){
                Parente parente = simplesEsquerda(atual);
                parente = simplesDireita(parente.getPai());

                pai = parente.getPai();
                avo = parente.getAvo();

                mudarCor(pai, "preto");
                mudarCor(avo, "vermelho");
            }
        }
    }

    private void removerNos(No removido, No sucessor){
        printNo(sucessor);
        if(cor(removido) == "vermelho" && cor(sucessor) == "vermelho"){
            trocarNos(removido, sucessor);
            removeNo(removido);
            return;
        }

        if(cor(removido) == "preto" && cor(sucessor) == "vermelho"){
            trocarNos(removido, sucessor);
            mudarCor(sucessor, "preto");
            removeNo(removido);
            return;
        }

        balancearArvoreRemocao(sucessor);
        trocarNos(removido, sucessor);
        removeNo(removido);
    }

    private void balancearArvoreRemocao(No sucessor){
        if(sucessor == raiz){
            return;
        }

        Parente parentes = pegarParentes(sucessor);
        No tio = parentes.getTio();
        No sobrinhoLonge = parentes.getSobrinhoLonge();
        No sobrinhoPerto = parentes.getSobrinhoPerto();
        No avo = parentes.getAvo();

        //Caso1
        if(cor(tio) == "vermelho"){
            remocaoCaso1(avo, tio);
        }

        //Caso2b
        if(cor(avo) == "vermelho"){
            mudarCor(avo, "preto");
            mudarCor(tio, "vermelho");
            return;
        } else {
            mudarCor(tio, "vermelho");
            balancearArvoreRemocao(avo);
        }

        //Caso3
        if(cor(sobrinhoPerto) == "vermelho"){
            remocaoCaso3(tio, sobrinhoPerto);

            parentes = pegarParentes(sucessor);
            tio = parentes.getTio();
            sobrinhoLonge = parentes.getSobrinhoLonge();
            sobrinhoPerto = parentes.getSobrinhoPerto();
            avo = parentes.getAvo();

            remocaoCaso4(avo, tio, sobrinhoLonge);
            return;
        }

        //Caso 4
        if(cor(sobrinhoLonge) == "vermelho"){
            remocaoCaso4(avo, tio, sobrinhoLonge);
        }
    }

    private void remocaoCaso1(No pai, No tio){
        if(isFilhoDireito(tio)){
            simplesEsquerda(tio);
        } else {
            simplesDireita(tio);
        }

        mudarCor(pai, "vermelho");
        mudarCor(tio, "preto");
    }

    private void remocaoCaso3(No tio, No sobrinhoPerto){
        if(isFilhoDireito(sobrinhoPerto)){
            simplesEsquerda(sobrinhoPerto);
        } else{
            simplesDireita(sobrinhoPerto);
        }

        mudarCor(tio, "vermelho");
        mudarCor(sobrinhoPerto, "preto");
    }

    private void remocaoCaso4(No pai, No tio, No sobrinhoLonge){
        if(isFilhoDireito(tio)){
            simplesEsquerda(tio);
        } else {
            simplesDireita(tio);
        }
        tio.setCor(cor(pai));
        mudarCor(pai, "preto");
        mudarCor(sobrinhoLonge, "preto");
    }

    private Parente simplesEsquerda(No pai){
        Parente parentes = new Parente();
        No avo = pai.getPai();
        No filhoEsquerdo = pai.getFilhoEsquerdo();

        No bisavo = avo.getPai();
        if(avo == raiz){
            raiz = pai;
        }
        if(bisavo != null){
            if(isFilhoDireito(avo)){
                bisavo.setFilhoDireito(pai);
            } else {
                bisavo.setFilhoEsquerdo(pai);
            }
        }

        pai.setPai(bisavo);
        pai.setFilhoEsquerdo(avo);
        avo.setPai(pai);
        avo.setFilhoDireito(filhoEsquerdo);

        parentes.setPai(pai);
        parentes.setAvo(avo);

        return parentes;
    }

    private Parente simplesDireita(No pai){
        Parente parentes = new Parente();
        No avo = pai.getPai();
        No filhoDireito = pai.getFilhoDireito();

        No bisavo = avo.getPai();
        if(avo == raiz){
            raiz = pai;
        }
        if(bisavo != null){
            if(isFilhoDireito(avo)){
                bisavo.setFilhoDireito(pai);
            } else {
                bisavo.setFilhoEsquerdo(pai);
            }
        }
        pai.setPai(bisavo);
        pai.setFilhoDireito(avo);
        avo.setPai(pai);
        avo.setFilhoEsquerdo(filhoDireito);

        parentes.setPai(pai);
        parentes.setAvo(avo);

        return parentes;
    }

    private Parente pegarParentes(No pai){
        Parente parentes = new Parente();

        if(pai == raiz){
            return parentes;
        } else {
            No avo = pai.getPai();
            No tio;
            No sobrinhoPerto;
            No sobrinhoLonge;
            if(avo.getFilhoDireito() == pai){
                tio = avo.getFilhoEsquerdo();
                if(tio == null){
                    sobrinhoLonge = null;
                    sobrinhoPerto = null;
                } else {
                    sobrinhoPerto = tio.getFilhoDireito();
                    sobrinhoLonge = tio.getFilhoEsquerdo();
                }
            } else {
                tio = avo.getFilhoDireito();
                if(tio == null){
                    sobrinhoLonge = null;
                    sobrinhoPerto = null;
                } else {
                    sobrinhoLonge = tio.getFilhoEsquerdo();
                    sobrinhoPerto = tio.getFilhoDireito();
                }
            }
            parentes.setAvo(avo);
            parentes.setTio(tio);
            parentes.setSobrinhoLonge(sobrinhoLonge);
            parentes.setSobrinhoPerto(sobrinhoPerto);

            return parentes;
        }
    }

    private void mudarCor(No no, String cor){
        if(no == null){
            return;
        }

        if(no == raiz){
            no.setCor("preto");
        } else {
            no.setCor(cor);
        }
    }

    private String cor(No no){
        if(no == null){
            return "preto";
        }
        return no.getCor();
    }

    private No pai(No no){
        return no.getPai();
    }

    private Boolean isFilhoDireito(No no){
        return no.getPai().getFilhoDireito() == no;
    }

    private No sucessor(No no){
        if(no.getFilhoEsquerdo() == null){
            return no;
        }
        return sucessor(no.getFilhoEsquerdo());
    }

    private void trocarNos(No no1, No no2){
        No paiNo1 = no1.getPai();
        No paiNo2 = no2.getPai();

        if(no1 == raiz){
            raiz = no2;
        } else {
            if(paiNo1 != null){
                if(isFilhoDireito(no2)){
                    paiNo1.setFilhoDireito(no2);
                } else {
                    paiNo1.setFilhoEsquerdo(no2);
                }
            }
        }
        boolean no2FilhoDireito = isFilhoDireito(no2);
        no2.setPai(paiNo1);

        boolean no2FilhoDireitoNo1 = no2 == no1.getFilhoDireito();
        boolean no2FilhoEsquerdoNo1 = no2 == no1.getFilhoEsquerdo();

        No filhoDireitoNo1 = no1.getFilhoDireito();
        No filhoEsquerdoNo1 = no1.getFilhoEsquerdo();
        No filhoDireitoNo2 = no2.getFilhoDireito();
        No filhoEsquerdoNo2 = no2.getFilhoEsquerdo();

        if(no2FilhoDireitoNo1){
            no2.setFilhoDireito(no1);
            no2.setFilhoEsquerdo(filhoEsquerdoNo1);

            if(filhoEsquerdoNo1 != null){
                filhoEsquerdoNo1.setPai(no2);
            }

            no1.setPai(no2);
            no1.setFilhoDireito(filhoDireitoNo2);
            no1.setFilhoEsquerdo(filhoEsquerdoNo2);

            if(filhoDireitoNo2 != null){
                filhoDireitoNo2.setPai(no1);
            }
            if (filhoEsquerdoNo2 != null){
                filhoEsquerdoNo2.setPai(no1);
            }
        }
        else if(no2FilhoEsquerdoNo1){
            no2.setFilhoEsquerdo(no1);
            no2.setFilhoDireito(filhoDireitoNo1);

            if(filhoDireitoNo1 != null){
                filhoDireitoNo1.setPai(no2);
            }

            no1.setPai(no2);
            no1.setFilhoDireito(filhoDireitoNo2);
            no1.setFilhoEsquerdo(filhoEsquerdoNo2);

            if(filhoDireitoNo2 != null){
                filhoDireitoNo2.setPai(no1);
            }
            if(filhoEsquerdoNo2 != null){
                filhoEsquerdoNo2.setPai(no1);
            }
        }
        else {
            if(paiNo2 != null){
                if(no2FilhoDireito){
                    paiNo2.setFilhoDireito(no1);
                } else {
                    paiNo2.setFilhoEsquerdo(no1);
                }
            }
            no1.setPai(paiNo2);

            no1.setFilhoEsquerdo(filhoEsquerdoNo2);
            no1.setFilhoDireito(filhoDireitoNo2);

            if(filhoEsquerdoNo2 != null){
                filhoEsquerdoNo2.setPai(no1);
            }
            if(filhoDireitoNo2 != null){
                filhoDireitoNo2.setPai(no1);
            }

            no2.setFilhoEsquerdo(filhoEsquerdoNo1);
            no2.setFilhoDireito(filhoDireitoNo1);

            if(filhoEsquerdoNo1 != null){
                filhoDireitoNo1.setPai(no2);
            }
            if(filhoEsquerdoNo1 != null){
                filhoEsquerdoNo1.setPai(no2);
            }
        }
    }

    private void removeNo(No no){
        if(no == raiz){
            raiz = null;
        } else {
            No pai = no.getPai();
            No filho = no.getFilhoDireito();
            if(isFilhoDireito(no)){
                pai.setFilhoDireito(filho);
            } else {
                pai.setFilhoEsquerdo(filho);
            }

            if(filho != null){
                filho.setPai(pai);
            }
            no.setPai(null);
        }
    }

    public static void main(String[] args) {
        ArvoreRN arvore = new ArvoreRN();

        System.out.println("=== INSERÇÃO ===");

        int[] valores = {10, 20, 30, 15, 5, 1, 25, 40, 50, 60, 2, 8};

        for (int v : valores) {
            System.out.println("\nInserindo: " + v);
            arvore.inserir(v);
            arvore.print();
        }

        System.out.println("\n=== BUSCA ===");
        int buscar = 25;
        No encontrado = arvore.buscar(arvore.raiz(), buscar);

        if (encontrado != null && encontrado.getElemento() == buscar) {
            System.out.println("Elemento " + buscar + " encontrado:");
            arvore.printNo(encontrado);
        } else {
            System.out.println("Elemento " + buscar + " não encontrado.");
        }

        System.out.println("\n=== REMOÇÃO ===");

        int[] remover = {1, 5, 20, 30};

        for (int r : remover) {
            System.out.println("\nRemovendo: " + r);
            arvore.remover(r);
            arvore.print();
        }

        System.out.println("\n=== ESTADO FINAL ===");
        arvore.print();
    }
}