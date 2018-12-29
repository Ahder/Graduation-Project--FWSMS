/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

/**
 *
 * @author Gaming Station
 */
public class Données {

    private double t;
    private int nb_nodes;
    private double total_energie;
    private int données_bs;

    public Données(double t, int nb_nodes, double total_energie, int données_bs) {
        this.t = t;
        this.nb_nodes = nb_nodes;
        this.total_energie = total_energie;
        this.données_bs = données_bs;
    }

    /**
     * ************************ Setteurs *************************************
     */
    public void setNb_nodes(int nb_nodes) {
        this.nb_nodes = nb_nodes;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setTotal_energie(double total_energie) {
        this.total_energie = total_energie;
    }

    public void setDonnées_bs(int données_bs) {
        this.données_bs = données_bs;
    }

    /**
     * ************************ Getteurs *************************************
     */
    public int getNb_nodes() {
        return nb_nodes;
    }

    public int getDonnées_bs() {
        return données_bs;
    }

    public double getTotal_energie() {
        return total_energie;
    }

    public double getT() {
        return t;
    }

}
