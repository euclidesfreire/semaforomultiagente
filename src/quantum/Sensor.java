package quantum;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;

public class Sensor extends Agent {
    
    protected void setup(){
            
        addBehaviour(new CyclicBehaviour(this) {
            //MEMORIA
            List<String> roads = new ArrayList<String>(){   
                {
                    add("road1");
                    add("road2");
                    add("road3");
                }
             }; //STRING COM TODOS OS SEMAFOROS
            int sTime, atime = 0; //TIME 
            long delay = 1000;
            long timeInicial = System.currentTimeMillis();
            
            Core core = new Core(); //BIBLIOTECA DE METODOS GERAIS
            
            private String road(){
                if(myAgent.getLocalName().equalsIgnoreCase("sensor1")){
                    return roads.get(0);
                } else if(myAgent.getLocalName().equalsIgnoreCase("sensor2")){
                   return roads.get(1);
                } else if(myAgent.getLocalName().equalsIgnoreCase("sensor3")){
                    return roads.get(2);
                }
                
                return null;
            }
            
            public void action(){
                
                ACLMessage msg = myAgent.receive();
            
                if(msg != null){                                      
                    if(msg.getContent().equalsIgnoreCase("TEMPO")){
                      boolean msgBool = true;
            
                      core.messageSend(myAgent, road(), "TEMPO", "transito");
            
                      while(msgBool){
                        ACLMessage qtCarros = core.messageReceive(myAgent);
                
                        if(qtCarros != null){
                            if(qtCarros.getOntology().equalsIgnoreCase("TEMPO")){
                                
                                ACLMessage reply = msg.createReply();
                                reply.setPerformative(ACLMessage.INFORM);
                                reply.setOntology("TEMPO");                                                     
                                reply.setContent(qtCarros.getContent());
                                myAgent.send(reply); 
                            
                                msgBool = false;
                            }
                        }else{
                            block();
                        }
                      }
                    }                                        
                } else {
                    block();
                }
                
                block(delay);
            }
            
        });
    }
}
