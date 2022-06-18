import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;



public class TSPBruteForce {
   private  int cidades;
   private  List<Coordinates> coordenadas;
   private int[] cidadesVisitadas; ;


   public TSPBruteForce(
        int cidades, 
        List<Coordinates> coordenadas) {
    this.cidades = cidades;
    this.coordenadas = coordenadas;
   }
 
   public int calcularDistancia(int xa, int ya, int xb, int yb){
    int distancia=(int)Math.sqrt(Math.pow((xb-xa), 2)+Math.pow((yb-ya), 2));
    return distancia;
   }


   public int[][] grafoDistancia(List<Coordinates> coordenadas){
     int[][] distancias = new int[coordenadas.size()][coordenadas.size()];
     for(int i=0;i<coordenadas.size();i++){
        for(int j=0;j<coordenadas.size();j++){
            distancias[i][j]=calcularDistancia(coordenadas.get(i).getX(), coordenadas.get(i).getY(), coordenadas.get(j).getX(), coordenadas.get(j).getY());
        }
     }
     return distancias;
   }

    public int travelingSailman(   
            int[][] grafo,
            boolean[] vetores,
            int comecoFim,
            int cidades,
            int contador, 
            int custo, 
            int minimo){
        
        if(contador==cidades && grafo[comecoFim][0]>0){
            minimo=Math.min(minimo, custo+grafo[comecoFim][0]);
            cidadesVisitadas[contador-1]=contador;
            return minimo;
        }
        for(int i=0;i<cidades;i++){
            if(vetores[i]== false && grafo[comecoFim][i]>0){
                vetores[i]=true;
                if(i!=0){
                    cidadesVisitadas[i]=comecoFim;
                }
                minimo = travelingSailman(grafo, vetores, i, cidades, contador+1, custo+grafo[comecoFim][i], minimo);
                vetores[i]=false;
            }
        }
        return minimo;
    } 

    public void escreverArquivo(int minimo){
      try {
        var writer = new BufferedWriter(new FileWriter("output.txt"));
        for(int i=0;i<cidadesVisitadas.length;i++)
        writer.write(cidadesVisitadas[i]);
        writer.newLine();
        writer.write(minimo);
        writer.close();
      } catch (Exception e) {
        //TODO: handle exception
      }  
    }
    public void start(List<Coordinates> coordenadas, int numCidades){
        cidadesVisitadas = new int[numCidades];
        cidadesVisitadas[0]=1;
        int[][] grafoDecusto = grafoDistancia(coordenadas);
        boolean[] vetores = new boolean[coordenadas.size()];
        vetores[0]=true;
        int minimo = Integer.MAX_VALUE;
        minimo= travelingSailman(grafoDecusto, vetores, 0, cidades, 1, 0, minimo);
        System.out.println(coordenadas);
        for(int i=0;i<coordenadas.size();i++){
            for(int j=0;j<coordenadas.size();j++){
                System.out.print(grafoDecusto[i][j]);
                System.out.print(" |");
            }
            System.out.println(" ");
         }
        System.out.print("Rota: ");
        for(int i=0;i<numCidades;i++)
        System.out.print(cidadesVisitadas[i] + "=>");
        System.out.println("1");
        System.out.println("O menor peso é: "+  minimo);
        escreverArquivo(minimo);
    }
}

   


