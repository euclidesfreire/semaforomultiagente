package quantum;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Controle extends CyclicBehaviour{
   //MEMORIA
    long delay = 1000;
    long timeInicial = System.currentTimeMillis();
    String circulo, //COR DO CIRCULO DO SEMAFORO
           road; //VIA DO SEMAFORO
    
    int tempo = 5; //TEMPO DO SEMAFORO
    
    boolean passaVerde = false;
    boolean viewBool = true;
    //END MEMORIA
    
    Core core = new Core(); //BIBLIOTECA DE METODOS GERAIS  
        
    public Controle(Agent a, String circulo, String road){
        super(a); //ENVIANDO AGENTE PARA CLASSE SUPER
        this.circulo = circulo;
        this.road = road;
    }
        
    private void down(){
        tempo--;
    }
    
    private void up(){
        if(circulo.equalsIgnoreCase("AMARELO")){
            tempo = 2;            
        } else if(circulo.equalsIgnoreCase("VERMELHO")){
            boolean msgBool = true;
            
            core.messageSend(myAgent, "Brain", "TEMPO", "transito");
            
            while(msgBool){
                ACLMessage upTempo = core.messageReceive(myAgent);
                
                if(upTempo != null){
                   if(upTempo.getOntology().equalsIgnoreCase("TEMPO")){
                    this.tempo = Integer.valueOf(upTempo.getContent());
                    msgBool = false;
                   }
                }else{
                    block();
                }
            }
        }
    }
    
    private void run(){
        
        if(circulo.equalsIgnoreCase("VERDE"))
        {
            if(tempo == 0){
                circulo = "AMARELO";
                viewBool = true;
                up();
            } else {
                down();
                
                //ENVIAR MENSAGEM PARA VIA
                //DIMINUINDO QUANTIDADE DE CARROS
                core.messageSend(myAgent, road, "LIGADO", "transito");
            }
        } 
        else if(circulo.equalsIgnoreCase("AMARELO"))
        {
            if(tempo == 0){
                circulo = "VERMELHO";
                viewBool = true;
                passaVerde = true;                
            } else {
                down();
            }
        } 
        else if(circulo.equalsIgnoreCase("VERMELHO"))
        {
            ACLMessage msg = core.messageReceive(myAgent);
            
            if(msg != null){
                if(msg.getContent().equalsIgnoreCase("VERDE")){
                    up();
                    circulo = "VERDE";
                    viewBool = true;
                }
            } else {
                block();
            }
        }
    
    }
    
    private void send(){
        
        if(passaVerde){
            core.messageSend(myAgent, "Brain", "passaVerde", "transito");
            passaVerde = false;
        }
    
    }
    
    private void view(){
        if(viewBool)
        {
           System.out.println(myAgent.getLocalName() + ": " + circulo);
           System.out.println(myAgent.getLocalName() + " Tempo: " + tempo);
           core.messageSend(myAgent, "GUI", circulo, "transito");
           viewBool = false;
        }
    }
        
    //FUNÇÃO DE ACTION DO SEMAFORO 
    public void action(){
        view();
        send();
        run();
        
        block(delay);
    } 
}