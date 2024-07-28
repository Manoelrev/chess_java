package Engine;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;

public abstract class Pieces extends JLabel {

    public final static int BRANCO = 0;
    public final static int PRETO = 1;

    private final static int peao = 0;
    private final static int cavalo = 1;
    private final static int bispo = 2;
    private final static int rainha = 3;
    private final static int rei = 4;
    private final static int torre = 5;

    private int mouseInputX, 
                mouseInputY,
                position;
    
    public static int[] kingPosition = {0,0};
    public static Boolean enPassant = false;
    public static Boolean castling = false;
    int cor;
    int tipo;
    int status[] = {0, 0, 0};
    Image iconePeca;

    public boolean selecionada = false;

    private Pieces(int pos, int color, Image icone, int tipo){
        mouseInputX = pos % 8;
        mouseInputY = pos / 8;
        Dependencias.quadradosOcupados.add(pos);

        this.tipo = tipo;
        this.position = pos;
        this.cor = color;
        this.iconePeca = icone;

        if(tipo == rei){
            kingPosition[(cor == BRANCO) ? 0 : 1] = position;
        }
        
        setBounds (mouseInputX * 75,mouseInputY * 75,75,75);
        MouseAdapter mouse = new MouseAdapter () {
            @Override
            public void mouseDragged(MouseEvent e) { 
                if(Dependencias.turno == cor){
        
                    mouseInputX = (e.getXOnScreen() - Dependencias.positionX) - 40;
                    mouseInputY = (e.getYOnScreen() - Dependencias.positionY) - 60;

                    for (Integer pos : movimentosValidos()) {
                        if(Dependencias.board[pos] != null){
                            Dependencias.board[pos].setVisible(false);}
                    }

                    setLocation(mouseInputX, mouseInputY);
                    selecionada = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(Dependencias.turno == cor){
                Boolean movimentoFeito = false;
                mouseInputX = ((e.getXOnScreen() - Dependencias.positionX) / 75 > 7) ? 7 : (e.getXOnScreen() - Dependencias.positionX) / 75;
                mouseInputY = ((e.getYOnScreen() - Dependencias.positionY) / 75 > 7) ? 7 : (e.getYOnScreen() - Dependencias.positionY) / 75;

                int mouseLocation = mouseInputX + mouseInputY * 8;

                for (Integer pos : movimentosValidos()) {
                    if(pos == mouseLocation){
                        if(tipo == peao && enPassant && pos == peaoMath(9)){
                            Dependencias.quadradosOcupados.remove(Integer.valueOf((cor == BRANCO) ? position + 1 : position - 1));
                            Dependencias.board[Integer.valueOf((cor == BRANCO) ? position + 1 : position - 1)].setVisible(false);
                            Dependencias.board[Integer.valueOf((cor == BRANCO) ? position + 1 : position - 1)] = null;
                        } else if(tipo == peao && enPassant && pos == peaoMath(7)){
                            Dependencias.quadradosOcupados.remove(Integer.valueOf((cor == BRANCO) ? position - 1 : position + 1));
                            Dependencias.board[Integer.valueOf((cor == BRANCO) ? position - 1 : position + 1)].setVisible(false);
                            Dependencias.board[Integer.valueOf((cor == BRANCO) ? position - 1 : position + 1)] = null;
                            
                        }
                        
                        if(tipo == rei && castling && (pos == position - 2 || pos == position + 2)){
                            int positionCastInicio = (pos == position - 2) ? position - 4 : position + 3;
                            int positionCastFim = (pos == position - 2) ? position - 1 : position + 1;

                            Dependencias.quadradosOcupados.remove(Integer.valueOf(positionCastInicio));
                            Dependencias.quadradosOcupados.add(positionCastFim);

                            
                            Dependencias.board[Integer.valueOf(positionCastFim)] = Dependencias.board[positionCastInicio];
                            Dependencias.board[Integer.valueOf(positionCastFim)].setLocation((positionCastFim) % 8 * 75, ((positionCastFim) / 8) * 75);
                            Dependencias.board[Integer.valueOf(positionCastInicio)] = null;
                        }

                        Dependencias.quadradosOcupados.remove(Integer.valueOf(position));
                        Dependencias.quadradosOcupados.add(pos);

                        Dependencias.board[Integer.valueOf(position)] = null;
                        Dependencias.board[pos] = Pieces.this;

                        status[0]++;
                        enPassant = false;
                        castling = false;
                        position = pos;
                        movimentoFeito = true;
                        Dependencias.turno = (Dependencias.turno == 0) ? 1 : 0;
                        break;
                    }
                }
                for (Pieces pos : Dependencias.board) {
                    if(pos != null){
                        if(pos.status[1] > 0 && movimentoFeito){pos.status[1]++;}
                        if(pos.status[0] == 1 && movimentoFeito){pos.status[1]++;}
                        pos.setVisible(true);
                    }
                }

                setLocation((position % 8) * 75, (position / 8) * 75);
                if(tipo == rei){
                    kingPosition[(cor == BRANCO) ? 0 : 1] = position;
                }
                selecionada = false;
            }}
        };

        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        requestFocus(); 
        setVisible(true);
    }    

    public ArrayList<Integer> movimentosValidos(){
        ArrayList<Integer> movimentosLivres = new ArrayList<Integer>();
        
        int point = 0;
        if(tipo == rei){
            for(int x = -1; x < 2;x++){
                int mov = position + x;
                if(noTabuleiro(mov - 8, position / 8 - 1) && (Dependencias.board[mov - 8] == null || Dependencias.board[mov - 8].cor != cor)) movimentosLivres.add(mov - 8);
                if(noTabuleiro(mov, position / 8) && (Dependencias.board[mov] == null || Dependencias.board[mov].cor != cor)) movimentosLivres.add(mov); 
                if(noTabuleiro(mov + 8, position / 8 + 1) && (Dependencias.board[mov + 8] == null || Dependencias.board[mov + 8].cor != cor)) movimentosLivres.add(mov + 8); 
            }

            if(status[0] == 0 && Dependencias.board[position - 4] != null && Dependencias.board[position - 4].status[0] == 0){
                Boolean te = true;
                for(int x = position - 1; x > position - 3; x--){
                    if(Dependencias.board[x] != null){
                        te = false;
                    }
                }
                if(te){
                    movimentosLivres.add(position - 2);
                    castling  = true ;
                }
            }

            if(status[0] == 0 && Dependencias.board[position + 3] != null && Dependencias.board[position + 3].status[0] == 0){
                Boolean te = true;
                for(int x = position + 1; x < position + 3; x++){
                    if(Dependencias.board[x] != null){
                        te =false;
                    }
                }
                if(te){
                    movimentosLivres.add(position + 2);
                    castling  = true ;
                }
            }
        }

        if(tipo == peao){
            if(noTabuleiro(peaoMath(16)) && Dependencias.board[peaoMath(16)] == null && status[0] == 0 && Dependencias.board[peaoMath(8)] == null){
                movimentosLivres.add(peaoMath(8));
                movimentosLivres.add(peaoMath(16));
            } else if(noTabuleiro(peaoMath(8)) && Dependencias.board[peaoMath(8)] == null){
                movimentosLivres.add(peaoMath(8));
            }

            if(noTabuleiro(peaoMath(7),  (cor == BRANCO) ? position / 8 +1 : position / 8 -1) && Dependencias.board[peaoMath(7)] != null && Dependencias.board[peaoMath(7)].cor != cor){
                movimentosLivres.add(peaoMath(7));  
            }
        
            if(noTabuleiro(peaoMath(9),  (cor == BRANCO) ? position / 8 +1 : position / 8 -1) && Dependencias.board[peaoMath(9)] != null && Dependencias.board[peaoMath(9)].cor != cor){
                movimentosLivres.add(peaoMath(9));
            }
        
            if(noTabuleiro(position +1) && Dependencias.board[position +1] != null && Dependencias.board[position +1].tipo == peao && Dependencias.board[position +1].cor != cor && Dependencias.board[position +1].status[1] == 1){
                movimentosLivres.add((cor == BRANCO) ? peaoMath(9) : peaoMath(9 - 2)); enPassant = true;
            }
            if(noTabuleiro(position -1) && Dependencias.board[position -1] != null && Dependencias.board[position -1].tipo == peao && Dependencias.board[position -1].cor != cor && Dependencias.board[position -1].status[1] == 1){
                movimentosLivres.add((cor == BRANCO) ? peaoMath(7) : peaoMath(7 + 2)); enPassant = true;
            }
        }

        if(tipo == torre || tipo == rainha){
            for (int x = position % 8; x < 7; x++){
                point ++;
                if(Dependencias.board[(position + point)] != null){
                    if (Dependencias.board[position + point].cor != cor) {
                        movimentosLivres.add(position + point);
                    }
                    break;
                }
                movimentosLivres.add(position + point);
            }
            point = 0;
            for (int x = position % 8; x > 0; x--){
                point ++;
                if(Dependencias.board[(position - point)] != null){
                    if (Dependencias.board[position - point].cor != cor) {
                        movimentosLivres.add(position - point);
                    }
                    break;
                }
                movimentosLivres.add(position - point);
            }   
            point = 0;
            for (int y = position / 8; y < 7; y++){
                point +=8;
                if(Dependencias.board[(position + point)] != null){
                    if (Dependencias.board[position + point].cor != cor) {
                        movimentosLivres.add(position + point);
                    }
                    break;
                }
                movimentosLivres.add(position + point);
            }
            point = 0;
            for (int y = position / 8; y > 0; y--){
                point +=8;
                if(Dependencias.board[(position - point)] != null){
                    if (Dependencias.board[position - point].cor != cor) {
                        movimentosLivres.add(position - point);
                    }
                    break;
                }
                movimentosLivres.add(position - point);
            }
        }

        if(tipo == bispo || tipo == rainha){
            point = 0;
            for (int x = position % 8; x < 7; x++){
                point +=7;
                if(position - point < 64 && position - point > -1){
                if(Dependencias.board[(position - point)] != null){
                    if (Dependencias.board[position - point].cor != cor) {
                        movimentosLivres.add(position - point);
                    }
                    break;
                }
                movimentosLivres.add(position - point);}
            }
            point = 0;
            for (int x = position % 8; x > 0; x--){
                point +=7;
                if(position + point < 64 && position + point > -1){
                    if(Dependencias.board[(position + point)] != null){
                        if (Dependencias.board[position + point].cor != cor) {
                            movimentosLivres.add(position + point);
                        }
                        break;
                    }
                movimentosLivres.add(position + point);}
            }

            point = 0;
            for (int x = position / 8; x < 7; x++){

                point +=9;

                if(position + point < 64 && position + point > -1){
                    
                if((position + point) % 8 > position % 8){
                    if(Dependencias.board[(position + point)] != null){
                        if (Dependencias.board[position + point].cor != cor) {
                            movimentosLivres.add(position + point);
                        }
                        break;
                    }
                    movimentosLivres.add(position + point);}
                }
            }

            point = 0;
            for (int x = position / 8; x > 0; x--){
                point +=9;
                if(position - point < 64 && position - point > -1){
                if((position - point) % 8 < position % 8){
                    if(Dependencias.board[(position - point)] != null){
                        if (Dependencias.board[position - point].cor != cor) {
                            movimentosLivres.add(position - point);
                        }
                        break;
                    }
                    movimentosLivres.add(position - point);}
                }
            }
        }

        if(tipo == cavalo){
            int movimentoUm = position+15;
            int movimentoDois = position-15;
            int movimentoTres = position + 6;
            int movimentoQuatro = position - 6;

            if(noTabuleiro(movimentoUm, position / 8 + 2) && (Dependencias.board[movimentoUm] == null || Dependencias.board[movimentoUm].cor != cor)) movimentosLivres.add(movimentoUm);
            if(noTabuleiro(movimentoUm + 2, position / 8 + 2) && (Dependencias.board[movimentoUm + 2] == null || Dependencias.board[movimentoUm + 2].cor != cor)) movimentosLivres.add(movimentoUm+2);

            if(noTabuleiro(movimentoTres, position / 8 + 1) && (Dependencias.board[movimentoTres] == null || Dependencias.board[movimentoTres].cor != cor)) movimentosLivres.add(movimentoTres);
            if(noTabuleiro(movimentoTres + 4, position / 8 + 1) && (Dependencias.board[movimentoTres + 4] == null || Dependencias.board[movimentoTres + 4].cor != cor)) movimentosLivres.add(movimentoTres + 4);
            
            if(noTabuleiro(movimentoDois, position / 8 - 2) && (Dependencias.board[movimentoDois] == null || Dependencias.board[movimentoDois].cor != cor)) movimentosLivres.add(movimentoDois);
            if(noTabuleiro(movimentoDois - 2, position / 8 - 2) && (Dependencias.board[movimentoDois - 2] == null || Dependencias.board[movimentoDois - 2].cor != cor)) movimentosLivres.add(movimentoDois-2);

            if(noTabuleiro(movimentoQuatro, position / 8 - 1) && (Dependencias.board[movimentoQuatro] == null || Dependencias.board[movimentoQuatro].cor != cor)) movimentosLivres.add(movimentoQuatro);
            if(noTabuleiro(movimentoQuatro - 4, position / 8 - 1) && (Dependencias.board[movimentoQuatro-4] == null || Dependencias.board[movimentoQuatro-4].cor != cor)) movimentosLivres.add(movimentoQuatro - 4);
        }
        return movimentosLivres;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(iconePeca, 0, 0, 75, 75, null);
        
    }

    public static Pieces peao(int position, int cor){
        return new Pieces(position, cor, Dependencias.pegarImagePorLink("recursos/peao" + ((cor == BRANCO) ? "_branco" : "_preto") + ".png"), peao){};
    }
    public static Pieces bispo(int position, int cor){
        return new Pieces(position, cor, Dependencias.pegarImagePorLink("recursos/bispo" + ((cor == BRANCO) ? "_branco" : "_preto") + ".png"), bispo){};
    }
    public static Pieces cavalo(int position, int cor){
        return new Pieces(position, cor, Dependencias.pegarImagePorLink("recursos/cavalo" + ((cor == BRANCO) ? "_branco" : "_preto") + ".png"), cavalo){};
    }
    public static Pieces rainha(int position, int cor){
        return new Pieces(position, cor, Dependencias.pegarImagePorLink("recursos/rainha" + ((cor == BRANCO) ? "_branco" : "_preto") + ".png"), rainha){};
    }
    public static Pieces rei(int position, int cor){
        return new Pieces(position, cor, Dependencias.pegarImagePorLink("recursos/rei" + ((cor == BRANCO) ? "_branco" : "_preto") + ".png"), rei){};
    }
    public static Pieces torre(int position, int cor){
        return new Pieces(position, cor, Dependencias.pegarImagePorLink("recursos/torre" + ((cor == BRANCO) ? "_branco" : "_preto") + ".png"), torre){};
    }

    public Image pegarImageCapturada() {
        String texto = "";
        switch (tipo) {
            case 0:texto = "peao";break;
            case 1:texto = "cavalo";break;
            case 2:texto = "bispo";break;
            case 3:texto = "rainha";break;
            case 4:texto = "rei";break;
            case 5:texto = "torre";break;
        
            default:
                break;
        }
        return Dependencias.pegarImagePorLink("recursos/capturados/" + texto + "_capturado.png");
    }
    public Image ImageCastling() {
        return Dependencias.pegarImagePorLink("recursos/castling.png");
    }

    private Boolean noTabuleiro(int numero){
        return (numero < 64 && numero > -1);
    }

    private Boolean noTabuleiro(int numero, int pos){
        return (numero <= 63 && numero >= 0 && numero / 8 == pos);
    }

    private int peaoMath(int num){
        return (cor == BRANCO) ? position + num : position- num;
    }
}
