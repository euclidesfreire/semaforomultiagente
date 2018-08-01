package quantum;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class Simulator extends Agent {
        
    protected void setup(){
            
        addBehaviour(new CyclicBehaviour(this) {
            long delay = 1000;
            long timeInicial = System.currentTimeMillis();
            Random carros = new Random();
            Integer road = 0; //QUANTIDADE DE CARROS NA VIA
            int tempo = 1; //TEMPO PARA CHEGADA DE CARROS NA VIA
                        
            Core core = new Core(); //BIBLIOTECA DE METODOS GERAIS
            
            public void action(){
                
                if(tempo == 5){
                    this.road += carros.nextInt(5);
                    tempo = 1;
                } else {
                    tempo++;
                }
                               
                //System.out.println(road + "\n");
                //CONVERTENDO ROAD PARA STRING
                String sRoad = Integer.toString(this.road);
                
                //ENVIANDO A QUANTIDADE DE CARROS PARA O SENSOR
                core.messageSend(myAgent, "GUI", sRoad, "transito");
                
                //RECEBENDO SE O SEMAFOR ESTA VERDE on 
                //QUE SAIRAM DA VIA
                ACLMessage msg = myAgent.receive();
            
                if(msg != null){ 
                    String content = msg.getContent();
                     
                    //SE ESTA   LIGADO, SEMAFORO VERDE
                    //ENTÃO VER QUANTOS CARROAS ESTÃO SAINDO
                    if(content.equalsIgnoreCase("LIGADO")){                      
                        if(this.road > 3){
                            this.road -= 2;
                        } else if(this.road > 2){
                            this.road -= 1;
                        }else if(this.road > 0){
                            this.road -= 1;
                        }
                    } else if(content.equalsIgnoreCase("TEMPO")){
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setOntology("TEMPO");                                                     
                        reply.setContent(sRoad);
                        myAgent.send(reply); 
                    }
                } else {
                    block();
                }
                
                block(delay);
            }
            
        });
    }
}
