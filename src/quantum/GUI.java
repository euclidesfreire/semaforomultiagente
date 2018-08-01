package quantum;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;

public class GUI extends Agent {
    
    @Override
    protected void setup(){
        
        JFrame janela = new JFrame("SISTEMA QUANTUM");
        AgenteGUI jogo = new AgenteGUI();
        
        janela.add(jogo);
        janela.pack();
        janela.setVisible(true);
        janela.setResizable(false);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        
        jogo.start();
            
        addBehaviour(new CyclicBehaviour(this) {
            Random carros = new Random();
            Integer road = 0;
                                            
            public void action(){
                             
                ACLMessage msgA = myAgent.receive();
            
                if(msgA  != null){
                    String content = msgA .getContent();
                
                    if(content.equalsIgnoreCase("VERDE") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo1")){
                       jogo.imgV = jogo.verdeV;
                    } else if(content.equalsIgnoreCase("AMARELO") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo1")){
                       jogo.imgV = jogo.amareloV;
                    } else if(content.equalsIgnoreCase("VERMELHO") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo1")){
                        jogo.imgV = jogo.readV;
                    }
                    
                    if(content.equalsIgnoreCase("VERDE") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo2")){
                       jogo.imgH1 = jogo.verdeH;
                    } else if(content.equalsIgnoreCase("AMARELO") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo2")){
                       jogo.imgH1 = jogo.amareloH;
                    } else if(content.equalsIgnoreCase("VERMELHO") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo2")){
                       jogo.imgH1 = jogo.readH;
                    }
                    
                    if(content.equalsIgnoreCase("VERDE") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo3")){
                       jogo.imgH2 = jogo.verdeH;
                    } else if(content.equalsIgnoreCase("AMARELO") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo3")){
                       jogo.imgH2 = jogo.amareloH;
                    } else if(content.equalsIgnoreCase("VERMELHO") && msgA.getSender().getLocalName().equalsIgnoreCase("semaforo3")){
                       jogo.imgH2 = jogo.readH;
                    }
                    
                    if(msgA.getSender().getLocalName().equalsIgnoreCase("road1")){
                        jogo.road1 = content;
                    }
                    if(msgA.getSender().getLocalName().equalsIgnoreCase("road2")){
                        jogo.road2 = content;
                    }
                    if(msgA.getSender().getLocalName().equalsIgnoreCase("road3")){
                        jogo.road3 = content;
                    }
                    
                } else {
                    block();
                }
            }
            
        });
    }
    
    public class AgenteGUI extends Canvas implements Runnable{
        private static final int largura = 800;
        private static final int altura = 600;
    
        private boolean gameOn = false;
    
        private Thread thread;
    
        private int imgL = largura;
        private int imgA = altura;
    
        private Image img = new ImageIcon(getClass().getResource("road.jpg")).getImage();
        public Image verdeV = new ImageIcon(getClass().getResource("verdev.png")).getImage();
        public Image amareloV = new ImageIcon(getClass().getResource("amarelov.png")).getImage();
        public Image readV = new ImageIcon(getClass().getResource("readv.png")).getImage();
        public Image verdeH = new ImageIcon(getClass().getResource("verdeh.png")).getImage();
        public Image amareloH = new ImageIcon(getClass().getResource("amareloh.png")).getImage();
        public Image readH = new ImageIcon(getClass().getResource("readh.png")).getImage();
        public Image imgV = verdeV;
        public Image imgH1 = readH;
        public Image imgH2 = readH;
    
        //QUANTIDADES DE CARROS
        public String road1 = "0";
        public String road2 = "0";
        public String road3 = "0";
    
        public AgenteGUI(){
            setPreferredSize(new Dimension(largura,altura));
        }
    
        public void start(){
            gameOn = true;
            thread = new Thread(this);
            thread.start();
        }
    
        @Override
        public void run(){
            while(gameOn){
                update();
                draw();
            }
        }   
    
        public void draw(){
            BufferStrategy buffer = getBufferStrategy();
            if(buffer == null){
                createBufferStrategy(3);
                return;
            }
       
            Graphics g = buffer.getDrawGraphics();
       
            g.drawImage(img,0,0,imgL,imgA,0,0,1512,1134,null);
            g.drawImage(imgV,250,320,28,74,null);
            g.drawImage(imgV,522,206,28,74,null);
            g.drawImage(imgH1,289,402,74,28,null);
            g.drawImage(imgH1,437,402,74,28,null);
            g.drawImage(imgH2,290,170,74,28,null);
       
            g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            g.setColor(Color.WHITE);
            g.drawString(road1, 150, 340);
            g.drawString(road1, 630, 235);
            g.drawString(road2, 350, 530);
            g.drawString(road2, 480, 530);
            g.drawString(road3, 300, 90);
        
            g.dispose();
            buffer.show();
        }
    
        public void update(){
        }
   } 
}   
