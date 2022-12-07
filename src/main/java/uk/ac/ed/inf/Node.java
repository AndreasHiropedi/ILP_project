package uk.ac.ed.inf;

import java.util.ArrayList;

class Node implements Comparable<Node>
{

    public final LngLat value;
    public double g_scores;
    public double h_scores;
    public double f_scores = 0;
    public ArrayList<Node> adjacencies = new ArrayList<>();
    public Node parent;
    public Node destinationNode;

    public Node(LngLat val, Node destination)
    {
        value = val;
        destinationNode = destination;
        h_scores = val.distanceTo(destinationNode.value);
    }

    public void setAdjacencies()
    {
        for (CompassLocation direction : CompassLocation.values()) {
            LngLat nextPos = value.nextPosition(direction);
            Node newNode = new Node(nextPos, destinationNode);
            newNode.parent = this;
            adjacencies.add(newNode);
        }
    }

    //override compare method
    @Override
    public int compareTo(Node i)
    {
        return Double.compare(this.f_scores, i.f_scores);
    }

}
