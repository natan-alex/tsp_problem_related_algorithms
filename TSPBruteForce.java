import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TSPBruteForce {
    private int cidades;
    private int [][] grafoDeDistancia; 
    private boolean[] visitados; 
    private int custoMinimo; 
    private Vertices ultimaCidade; 
    

    TSPBruteForce(int[][] grafo)
    {
        this.cidades = grafo.length;
        this.grafoDeDistancia = grafo;
        this.visitados = new boolean[cidades];
        this.custoMinimo = Integer.MAX_VALUE;
        this.ultimaCidade = null;

        // Mark first vertex as visited since we use it as the root
        visitados[0] = true;
    }

    int getCustoMinimo() {
        backTracking(new Vertices(0,null,0),  1);
        return custoMinimo;
    }

    private void backTracking(Vertices cidadeAtual, int cidadesVisitadas)
    {
        // If last vertex is reached and it has a link to the root vertex then
        // keep the minimum value out of the total cost
        // of traversal and "ans"
        // Returning to check for more possible values
        if (cidadesVisitadas == cidades && grafoDeDistancia[cidadeAtual.getId()][0] > 0)
        {
            if (custoMinimo <= cidadeAtual.getCustoDistancia() + grafoDeDistancia[cidadeAtual.getId()][0])
                return;

                // Better path found
            else
            {
                custoMinimo = cidadeAtual.getCustoDistancia() + grafoDeDistancia[cidadeAtual.getId()][0];
                ultimaCidade = cidadeAtual;
                return;
            }
        }

        // Loop to traverse the adjacency list of the current vertex and increasing the visited vertices
        // by 1, moving to the next vertex and increasing the new vertex cost by graph[currentVertex,i] value
        for (int i = 0; i < cidades; i++)
        {
            if (!visitados[i] && grafoDeDistancia[cidadeAtual.getId()][i] > 0)
            {
                // Mark as visited
                visitados[i] = true;

                Vertices nextVertex =  new Vertices(i, cidadeAtual, cidadeAtual.getCustoDistancia() + grafoDeDistancia[cidadeAtual.getId()][i]);
                backTracking(nextVertex, cidadesVisitadas + 1);

                // Mark ith node as unvisited after the recursion return
                visitados[i] = false;
            }
        }
    }


    List<Integer> getShortestPath() {

        List<Integer> shortestPath = new ArrayList<>();
        shortestPath.add(1);

        Vertices cidadeAtual  = ultimaCidade;

        while(cidadeAtual != null)
        {
            shortestPath.add(cidadeAtual.getId()+1);
            cidadeAtual = cidadeAtual.getPrev();
        }

        return shortestPath;
    }

    public void escreverArquivo(String nomeDoArquivo) {
        Exceptions.throwIfNullOrEmpty(nomeDoArquivo, "nome do arquivo");

        try (var writer = new BufferedWriter(new FileWriter(nomeDoArquivo))) {
            writer.write(getShortestPath().toString());
            writer.newLine();
            writer.write(Integer.toString(custoMinimo));
        } catch (Exception e) {
        }
    }



}
