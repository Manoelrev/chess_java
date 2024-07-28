package Engine;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class Engine extends JPanel {

    private Color corClara, corEscua;
    public static Boolean start = false;

    public Engine(Color corClara, Color corEscura){
        this.corClara = corClara;
        this.corEscua = corEscura;
        setLayout(null);
        setPreferredSize(new Dimension(600, 600));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        desenharTabela(g);

        if(start){
            for (int position : Dependencias.quadradosOcupados) {
                if(Dependencias.board[position] != null && Dependencias.board[position].selecionada){
                    for (int pontos : Dependencias.board[position].movimentosValidos()) {

                        if(Dependencias.board[position].tipo == 0 && Pieces.enPassant){
                            int passantPosition = (Dependencias.board[position].cor == 0 ? Dependencias.board[position].movimentosValidos().getLast() - 8 : Dependencias.board[position].movimentosValidos().getLast() + 8);
                            Dependencias.board[passantPosition].setVisible(false);
                            g.drawImage(Dependencias.board[passantPosition].pegarImageCapturada(), (passantPosition % 8) * 75, (passantPosition / 8) * 75, 75, 75, null);
                             
                        }
                    
                        if(Dependencias.board[position].tipo == 4 && Pieces.castling){
                            if(Dependencias.board[position - 4] != null){
                                Boolean te = true;
                                for(int x = position - 1; x > position - 3; x--){
                                    if(Dependencias.board[x] != null){
                                        te = false;
                                    }
                                }
                                if(te){
                                Dependencias.board[position - 4].setVisible(false);
                                g.drawImage(Dependencias.board[position - 4].ImageCastling(), ((position - 4) % 8) * 75, ((position - 4) / 8) * 75, 75, 75, null);
                                }
                            }
                            if (Dependencias.board[position + 3] != null){
                                Boolean te = true;
                                for(int x = position + 1; x < position + 2; x++){
                                    if(Dependencias.board[x] != null){
                                        te =false;
                                    }
                                }
                                if(te){
                                Dependencias.board[position + 3].setVisible(false);
                                g.drawImage(Dependencias.board[position + 3].ImageCastling(), ((position + 3) % 8) * 75, ((position + 3) / 8) * 75, 75, 75, null);
                                }
                            }
                            if(Dependencias.board[pontos] != null){
                                g.drawImage(Dependencias.board[pontos].pegarImageCapturada(), (pontos % 8) * 75, (pontos / 8) * 75, 75, 75, null);
                            } else {
                                g.drawImage(Dependencias.pegarImagePorLink("recursos/caminho_disponivel.png"), (pontos % 8) * 75, (pontos / 8) * 75, 75, 75, null);
                            }
                        } else if(Dependencias.board[pontos] != null){
                            g.drawImage(Dependencias.board[pontos].pegarImageCapturada(), (pontos % 8) * 75, (pontos / 8) * 75, 75, 75, null);
                        } else {
                        g.drawImage(Dependencias.pegarImagePorLink("recursos/caminho_disponivel.png"), (pontos % 8) * 75, (pontos / 8) * 75, 75, 75, null);
                        }
                    }
                }
            }
        }
        repaint();
    }

    private void desenharTabela(Graphics g){
        for(int quadradoX = 0; quadradoX < 8; quadradoX++){
            for(int quadradoY = 0; quadradoY < 8; quadradoY++){
                Color paintColor = ((quadradoX - quadradoY) % 2 != 0) ? corClara : corEscua;
                g.setColor(paintColor);
                g.fillRect(quadradoX * 75, quadradoY * 75 , 75, 75);
            }  
        }  
    }
}
