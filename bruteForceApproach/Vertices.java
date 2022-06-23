package bruteForceApproach;

public class Vertices {
    private int id;
    private int custoDistancia;
    private Vertices prev;

    Vertices(int id, Vertices prev, int custoDistancia) {
        this.id = id;
        this.prev = prev;
        this.custoDistancia = custoDistancia;
    }

    int getId() {
        return id;
    }

    int getCustoDistancia() {
        return custoDistancia;
    }

    Vertices getPrev() {
        return prev;
    }
}
