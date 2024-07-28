
import java.awt.Color;

import javax.swing.JFrame;

import Engine.Dependencias;
import Engine.Engine;
public class App extends JFrame {

    Color corClara = new Color(230, 234, 215);
    Color corEscua = new Color(69, 77, 95);

    void start(){
        Engine a = new Engine(corClara, corEscua);
        setIconImage(Dependencias.pegarImagePorLink("recursos/rei_preto.png"));
        setTitle("Chess"); setResizable(false);  setLayout(null);
        setContentPane(a); setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack(); setVisible(true); 
        
        Dependencias.fenChessGet("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", this);
        
        Engine.start = true;
        while (true) {
            a.repaint();
            Dependencias.positionX = getX();
            Dependencias.positionY = getY();
            String titulo = (Dependencias.turno == 0) ? "Branco" : "Preto";
            setTitle("Chess - " + titulo);
        }
    }

    public static void main(String[] args) throws Exception {
        new App().start();
    }
}
