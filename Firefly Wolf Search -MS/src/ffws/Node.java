/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

/**
 *
 * @author Gaming Station
 */
public class Node {

    private int id;
    private double x;
    private double y;
    private double Energie_Résid;
    private double Energie_Init; // en Jouls
    private String Etat; // Normal || CH || Sink
    private ArrayList<Node> membres;
    private Circle noeud;
    private Label label;

    private int data_recu;
    private boolean vivant;

    public Node(int id, int x, int y, String Etat) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.Etat = Etat;
        Energie_Init = 2;
        Energie_Résid = Energie_Init;
        membres = new ArrayList<>();
        vivant = true;
        data_recu = 0;

    }

    /**
     * *******************************************************************************************************************
     */
    public void Ajoutermembre(Node n) {
        membres.add(n);
    }

    /**
     * ************************ Getteurs *************************************
     */
    public Label getLabel() {
        return label;
    }

    public boolean isVivant() {
        return vivant;
    }

    public Circle getNoeud() {
        return noeud;
    }

    public int getId() {
        return id;
    }

    public int getData_recu() {
        return data_recu;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getEnergie_Résid() {
        return Energie_Résid;
    }

    public double getEnergie_Init() {
        return Energie_Init;
    }

    public String getEtat() {
        return Etat;
    }

    public ArrayList<Node> getMembres() {
        return membres;
    }

    /**
     * ************************ Setteurs *************************************
     */
    public void setMembres(ArrayList<Node> membres) {
        this.membres = membres;
    }

    public void setEnergie_Résid(double Energie_Résid) {
        this.Energie_Résid = Energie_Résid;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setEnergie_Init(double Energie_Init) {
        this.Energie_Init = Energie_Init;
    }

    public void setEtat(String Etat) {
        this.Etat = Etat;
    }

    public void setNoeud(Circle noeud) {
        this.noeud = noeud;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setData_recu(int data_recu) {
        this.data_recu = data_recu;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setVivant(boolean vivant) {
        this.vivant = vivant;
    }

    /**
     * *******************************************************************************************************************
     */
    public String afficherMembres() {
        String données = "";
        for (int i = 0; i < membres.size(); i++) {
            if (membres.get(i).isVivant()) {
                données = données + membres.get(i).id + "/";

            }

        }
        return données;
    }

}
