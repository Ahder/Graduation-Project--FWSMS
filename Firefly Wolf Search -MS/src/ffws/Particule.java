/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

import java.util.ArrayList;

/**
 *
 * @author redha
 */
public class Particule {

    double coast_fct;
    ArrayList<Node> noeuds;

    public Particule(ArrayList<Node> noeuds) {
        this.noeuds = new ArrayList<>();
        this.noeuds = noeuds;
    }

    public Particule() {
        noeuds = new ArrayList<>();
    }

    /**
     * *******************************************************************************************************************
     */
    public double CalculDistance(int x1, int y1, int x2, int y2) {
        double dist = Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
        return dist;
    }

    /**
     * *******************************************************************************************************************
     */
    public void affichage_contenu() {
        System.out.println("La particule contient les noeuds : ");
        for (int i = 0; i < noeuds.size(); i++) {

            if (noeuds.get(i).isVivant()) {
                System.out.print(noeuds.get(i).getId() + " ");

            }

        }
    }
}
