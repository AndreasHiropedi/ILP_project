package uk.ac.ed.inf;

import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;

public class AStarSearch
{

    public static void AStarSearch(Node source, Node goal)
    {
        Set<Node> explored = new HashSet<>();
        //override compare method
        //override compare method
        PriorityQueue<Node> queue = new PriorityQueue<>((i, j) ->
        {
            if (i.f_scores > j.f_scores)
            {
                return 1;
            }
            else if (i.f_scores < j.f_scores)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        });

        //cost from start
        source.g_scores = 0;
        queue.add(source);
        boolean found = false;

        while ((!found) && (!queue.isEmpty()))
        {
            //the NodeTwo in having the lowest f_score value
            Node current = queue.poll();
            explored.add(current);
            // goal found
            if (current.value.closeTo(goal.value))
            {
                found = true;
            }
            //check every child of current NodeTwo
            current.setAdjacencies();
            for (Node adjacent : current.adjacencies)
            {
                //TODO: Detection of no fly zones and in central area.
                double cost = 0.00015;
                double temp_g_scores = current.g_scores + cost;
                double temp_f_scores = temp_g_scores + adjacent.h_scores * 1.0692;
                /*
                else if child NodeTwo is not in queue or newer f_score is lower
                */
                if ((!queue.contains(adjacent)) ||
                        (temp_f_scores < adjacent.f_scores))
                {
                    adjacent.parent = current;
                    adjacent.g_scores = temp_g_scores;
                    adjacent.f_scores = temp_f_scores;
                    queue.remove(adjacent);
                    queue.add(adjacent);
                }
            }
        }
    }
}
