package Engine;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Dependencias {

    public static int positionX = 0;
    public static int positionY = 0;
    
    public static int turno = 0;
    public static Pieces[] board = new Pieces[64];

    public static void fenChessGet(String url, JFrame frame){
        int position = 0;
        for (char t : url.toCharArray()) {
            switch (t) {
                case 'p':
                    board[position] = Pieces.peao(position, Pieces.BRANCO);
                    break;
                case 'r':
                    board[position] = Pieces.torre(position, Pieces.BRANCO);
                    break;
                case 'n':
                    board[position] = Pieces.cavalo(position, Pieces.BRANCO);
                    break;
                case 'b':
                    board[position] = Pieces.bispo(position, Pieces.BRANCO);
                    break;
                case 'q':
                    board[position] = Pieces.rainha(position, Pieces.BRANCO);
                    break;
                case 'k':
                    board[position] = Pieces.rei(position, Pieces.BRANCO);
                    break;
                case 'P':
                    board[position] = Pieces.peao(position, Pieces.PRETO);
                    break;
                case 'R':
                    board[position] = Pieces.torre(position, Pieces.PRETO);
                    break;
                case 'N':
                    board[position] = Pieces.cavalo(position, Pieces.PRETO);
                    break;
                case 'B':
                    board[position] = Pieces.bispo(position, Pieces.PRETO);
                    break;
                case 'Q':
                    board[position] = Pieces.rainha(position, Pieces.PRETO);
                    break;
                case 'K':
                    board[position] = Pieces.rei(position, Pieces.PRETO);
                    break;
                default:
                    break;
            }
            if(t != '/'){position++;}
            try {position += Integer.parseInt(String.valueOf(t)) -1;} catch (Exception e) {}
        }
        for (int pos : quadradosOcupados) {
            frame.add(Dependencias.board[pos]);
        }
        
    }

    public static ArrayList<Integer> quadradosOcupados = new ArrayList<Integer>();

    public static Image pegarImagePorLink(String url){
        try {
            return ImageIO.read(new File(url));
        } catch (IOException e) {
            return null;
        }
    }
}
