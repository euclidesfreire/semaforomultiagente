package quantum;
import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Core extends Agent {
    
    public void messageSend(Agent a, String agentReceiver, String content, String ontology){
        
        ACLMessage send = new ACLMessage(ACLMessage.INFORM);            
        send.addReceiver(new AID(agentReceiver,AID.ISLOCALNAME));      
        send.setLanguage("LT");
        send.setOntology(ontology);
        send.setContent(content);
        a.send(send);
        
    }
    
    public ACLMessage messageReceive(Agent a){
        
        ACLMessage receiver = a.receive();
        
        return receiver;
        
    }
    
}
