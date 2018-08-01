package quantum;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;

public class Brain extends Agent {

    public void setup(){
        
        addBehaviour(new CyclicBehaviour(this){
            //MEMORIA
             List<String> semaforos = new ArrayList<String>(){   
                {
                    add("semaforo1");
                    add("semaforo2");
                    add("semaforo3");
                }
             }; //STRING COM TODOS OS SEMAFOROS
             
             List<String> sensores = new ArrayList<String>(){   
                {
                    add("sensor1");
                    add("sensor2");
                    add("sensor3");
                }
             }; //STRING COM TODOS OS SEMAFOROS
   
             String echoVerde = semaforos.get(0); //IDENTIFICAO DO SEMAFORO QUE TEM O VERDE
   
            int velocidade = 40, //VELOCIDADE MÉDIA DA VIA
                comprimento = 4, //COMPRIMENTO MÉDIO DO CRUZAMENTO EM METROS
                qtdCarros,
                tempo;
       
            double atraso = 4,
                   arranque = 1.5,
                   embalagem = 60,
                   cumprimentoVeiculo = 4;
            //END MEMORIA
            
            Core core = new Core(); //BIBLIOTECA DE METODOS GERAIS
          
            private String sensor(){
                if(echoVerde.equalsIgnoreCase(semaforos.get(0))){
                    return sensores.get(0);
                } else if(echoVerde.equalsIgnoreCase(semaforos.get(1))){
                   return sensores.get(1);
                } else if(echoVerde.equalsIgnoreCase(semaforos.get(2))){
                    return sensores.get(2);
                }
                
                return null;
            }
            
            private void decisao(){                
                if(echoVerde.equalsIgnoreCase(semaforos.get(0))){
                    echoVerde = semaforos.get(1);
                    core.messageSend(myAgent, echoVerde, "VERDE", "transito");
                } else if(echoVerde.equalsIgnoreCase(semaforos.get(1))){
                    echoVerde = semaforos.get(2);
                    core.messageSend(myAgent, echoVerde, "VERDE", "transito");
                } else if(echoVerde.equalsIgnoreCase(semaforos.get(2))){
                    echoVerde = semaforos.get(0);
                    core.messageSend(myAgent, echoVerde, "VERDE", "transito");
                }
            }
            
            private void calculo(){
                 tempo = (int) (this.atraso+
                         ((this.qtdCarros*this.arranque)+
                         (this.embalagem / (this.velocidade/2))+
                         (this.qtdCarros*(this.cumprimentoVeiculo+0.5))
                         +this.embalagem)/this.velocidade);
            }
            
            private void plano(){ 
                
                boolean msgBool = true;
            
                core.messageSend(myAgent, sensor(), "TEMPO", "transito");
            
                while(msgBool){
                    ACLMessage upTempo = core.messageReceive(myAgent);
                
                    if(upTempo != null){
                        if(upTempo.getOntology().equalsIgnoreCase("TEMPO")){
                            this.qtdCarros = Integer.valueOf(upTempo.getContent());
                            msgBool = false;
                        }
                    }else{
                        block();
                    }
                }
                
                calculo();
            }            
    
            public void run(){
                ACLMessage msg = core.messageReceive(myAgent);
                
                if(msg != null){
                   if(msg.getContent().equalsIgnoreCase("passaVerde")){
                       decisao();                    
                   } else if(msg.getContent().equalsIgnoreCase("TEMPO")){
                       plano();
                       ACLMessage reply = msg.createReply();
                       reply.setPerformative(ACLMessage.INFORM);
                       reply.setOntology("TEMPO");                                                     
                       reply.setContent(Integer.toString(this.tempo));
                       myAgent.send(reply);                       
                   }
                }else{
                    block();
                }
            }
            
            public void action(){
                run();
            }
        });
    }
}
