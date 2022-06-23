package heuristics;

public class Edge {
    private final int weight;
    private final int sourceNode;
    private final int destinationNode;

    public Edge(int weight, int sourceNode, int destinationNode) {
        this.weight = weight;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
    }

    @Override
    public String toString() {
        return "(" + sourceNode + " -- " + destinationNode + " == " + weight + ")";
    }

    public int getWeight() {
        return weight;
    }

    public int getSourceNode() {
        return sourceNode;
    }

    public int getDestinationNode() {
        return destinationNode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + destinationNode;
        result = prime * result + sourceNode;
        result = prime * result + weight;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge other = (Edge) obj;
        if (sourceNode != other.getSourceNode() || sourceNode != other.getDestinationNode())
            return false;
        if (destinationNode != other.getDestinationNode() || destinationNode != other.getSourceNode())
            return false;
        if (weight != other.weight)
            return false;
        return true;
    }

}