package util.graph;

import util.graph.*;
import util.intset.*;
import java.util.*;
import java.io.*;

public class ColorGraph {

    public  Graph graph;
    public  int R;
    public  int K;
    private Stack<Integer> pile;
    public  IntSet enleves;
    public  IntSet deborde;
    public  int[] couleur;
    public  Node[] int2Node;
    static  int NOCOLOR = -1;

    public ColorGraph(graph G, int K, int[] phi){
        this.G       = G;
        this.K       = K;
        pile         = new Stack<Integer>();
        R            = G.nodeCount();
        couleur      = new int[R];
        enleves      = new IntSet(R);
        deborde       = new IntSet(R);
        int2Node     = G.nodeArray();
        for(int v=0; v < R; v++){
            int preColor = phi[v];
            if(preColor >= 0 && preColor < K)
                couleur[v] = phi[v];
            else
                couleur[v] = NOCOLOR;
        }
    }

    public void selection()
    {
        while (!pile.isEmpty()) {
            int sommet = pile.pop();
            couleur[sommet] = choisisCouleur(couleursVoisins(sommet));
        }
    }


    public IntSet couleursVoisins(int t)
    {
        IntSet colorSet = new IntSet(K);
        Node node = int2Node[t];
        NodeList voisins = node.succ();
        while( voisins!= null ){
            int voisin_key =voisins.head.mykey;
            colorSet.add( couleur[voisin_key] );

            voisins = voisins.tail;
        }
        return colorSet;
    }

    public int choisisCouleur(IntSet colorSet)
    {
        for (int i = 0; i < colorSet.getSize(); i++)
            if (!colorSet.isMember(i)) return i;
        return NOCOLOR;
    }


    public int nbVoisins(int t)
    {
        int nb = 0;
        NodeList voisins = int2Node[t].succ();
        while( voisins!= null ){
            Node voisin = voisins.head;
            if( !enleves.isMember(voisin.mykey) )
                nb++;
            voisins =voisins.tail;
        }
        return nb;
    }



    public int simplification()
    {
        boolean ajour = true;
        while (pile.size() != R && ajour) {
            ajour = false;
            for (Node node : int2Node) {
                if (enleves.isMember(node.mykey)) continue;
                if (nbVoisins(node.mykey) < K && couleur[node.mykey] == NOCOLOR) {
                    enleves.add(node.mykey);
                    pile.push(node.mykey);
                    ajour = true;
                }
            }
        }
        return 0 ;
    }


    public void debordement()
    {
        while (pile.size() != R) {
            int sommet = choisis_sommet();
            pile.push(sommet);
            enleves.add(sommet);
            deborde.add(sommet);
            simplification();
        }

    }

    private int choisis_sommet(){
        int s;
        for(s=0;  s< this.R; s++ ){
            if( !pile.contains(s) )
                return s;
        }
        return -1;
    }


    public void coloration()
    {
        this.simplification();
        this.debordement();
        this.selection();
    }

    void affiche()
    {
        System.out.println("vertex\tcolor");
        for(int i = 0; i < R; i++){
            System.out.println(i + "\t" + couleur[i]);
        }
    }



}
