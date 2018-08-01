package quantum;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;

public class Semaforo extends Agent {
        
     protected void setup(){         
         //capturar os argumentos enviandos como parâmetros 
         Object[] args = getArguments();
       
         
         //se tiver argumento então recebe
         if(args != null && args.length>0){
             String circulo = (String) args[0];
             String road = (String) args[1]; //RUA DO SEMAFORO
                                          
            //CHAMANDO O CONTROLE DE ACTION DO SEMAFORO
            addBehaviour(new Controle(this, circulo, road)); 
         }
     }     
}
