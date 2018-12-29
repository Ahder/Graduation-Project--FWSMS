/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

/**
 *
 * @author redha
 */
public class Firefly_algo {

    public int TailleReseau; // nombres de noeuds dans le réseau
    public int MaxParticule; // le nombre de particules
    public int TailleParticule; // nombres de noeuds dans une particule
    public ArrayList<Node> reseau;
    public ArrayList<Particule> liste_particule;
    public int nb_round = 0;
    public double t_now;
    public double t_slot = 0.05;
    public double t_round = 30;
    public double durée_frame;
    public int area_size;

    public Firefly_algo(ArrayList<Node> reseau, ArrayList<Particule> liste_particule, int TailleReseau, int MaxParticule, int TailleParticule, int area_size) {
        this.reseau = reseau;
        this.liste_particule = liste_particule;
        this.MaxParticule = MaxParticule;
        this.TailleParticule = TailleParticule;
        this.TailleReseau = TailleReseau;
        this.area_size = area_size;

    }

    public Firefly_algo() {

    }

    public void InitResau(ArrayList<Node> reseau) {
        int x, y;
        Random r = new Random();

        for (int i = 0; i < TailleReseau; i++) {
            x = r.nextInt(area_size);
            y = r.nextInt(area_size);
            reseau.add(new Node(i, x, y, "Normal"));
            System.out.println("Le noeud N°" + i + " a la position : X=" + x + " Y=" + y);
        }

        System.out.println("Reseau Initialisé !");

    }

    /**
     * ************************ Getteurs *************************************
     */
    public double getT_round() {
        return t_round;
    }

    public double getNb_round() {
        return nb_round;
    }

    public double get_tslot() {
        return t_slot;
    }

    /**
     * *******************************************************************************************************************
     */
    public double CalculDistance(double x1, double y1, double x2, double y2) {
        double dist = Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
        return dist;
    }

    /**
     * *******************************************************************************************************************
     */
    public double CalculEnergieTransmise(double distance) {
        double EnergieUtilisée;
        int TailleMessage = 4000;
        double E_elec = 50 * Math.pow(10, -9);
        double epsilon_amp = 13 * Math.pow(10, -16);
        double epsilon_efs = 10 * Math.pow(10, -12);
        double d0 = Math.sqrt(epsilon_efs / epsilon_amp);

        if (distance < d0) {
            EnergieUtilisée = (TailleMessage * E_elec)
                    + (TailleMessage * epsilon_efs * Math.pow(distance, 2));
        } else {
            EnergieUtilisée = (TailleMessage * E_elec)
                    + (TailleMessage * epsilon_amp * Math.pow(distance, 4));

        }

        return EnergieUtilisée;
    }

    /**
     * *******************************************************************************************************************
     */
    public double CalculEnergieRecu() {
        int TailleMessage = 4000;
        double E_elec = 50 * Math.pow(10, -9);
        return TailleMessage * E_elec;
    }

    /**
     * *******************************************************************************************************************
     */
    public ArrayList<Particule> choix_particule(ArrayList<Node> reseau, int MaxParticule) {
        ArrayList<Particule> liste_particules = new ArrayList<>();
        Random r = new Random();

        //génener "Maxparticule" particules aléatoire
        for (int i = 0; i < MaxParticule; i++) {
            Particule par = new Particule();
            int ancien = -1;

            for (int j = 0; j < TailleParticule; j++) {
                int nouveau = r.nextInt(TailleReseau - 1);

                while (ancien == nouveau && reseau.get(nouveau).isVivant()) {
                    nouveau = r.nextInt(TailleReseau - 1);
                }

                if (reseau.get(nouveau).isVivant()) {
                    reseau.get(nouveau).setEtat("CH");
                    reseau.get(nouveau).getNoeud().setFill(Color.BLUE);
                    par.noeuds.add(reseau.get(nouveau));
                    ancien = nouveau;

                } else {
                    j--;

                }

            }

            liste_particules.add(par);
            System.out.println("");
            // par.affichage_contenu();

        }

        return liste_particules;

    }

    /**
     * *******************************************************************************************************************
     */
    public void Clustering(ArrayList<Node> reseau, ArrayList<Particule> liste_particule) {

        for (int i = 0; i < reseau.size(); i++) {
            double distance;
            double min = 1000;
            int id = -1;

            if (reseau.get(i).getEtat().equals("Normal") && reseau.get(i).isVivant()) {
                for (int j = 0; j < liste_particule.size(); j++) {
                    // System.out.print("La particule "+j+" ");

                    for (int k = 0; k < liste_particule.get(j).noeuds.size(); k++) {
                        // System.out.println("le noeud "+liste_particule.get(j).noeuds.get(k).getId());

                        distance = CalculDistance(reseau.get(i).getX(), reseau.get(i).getY(), liste_particule.get(j).noeuds.get(k).getX(), liste_particule.get(j).noeuds.get(k).getY());
                        /*System.out.print("la distance entre les noeuds : " + reseau.get(i).getId() + " " + liste_particule.get(j).noeuds.get(k).getId() + " = ");
                        System.out.printf("%.2f", distance);
                        System.out.println("");*/
                        if (distance < min) {
                            min = distance;
                            id = liste_particule.get(j).noeuds.get(k).getId();

                        }
                        /* System.out.print("Le min est :");
                        System.out.printf("%.2f", min);
                        System.out.println("le noeud min est :" + id);

                        System.out.println("");*/

                    }

                    // System.out.println("");
                }

                int ancien = -1;
                int nouveau = 0;

                for (int m = 0; m < liste_particule.size(); m++) {
                    for (int l = 0; l < liste_particule.get(m).noeuds.size(); l++) {
                        nouveau = liste_particule.get(m).noeuds.get(l).getId();
                        if (nouveau != ancien) {
                            if (liste_particule.get(m).noeuds.get(l).getId() == id) {
                                liste_particule.get(m).noeuds.get(l).Ajoutermembre(reseau.get(i));
                                // System.out.println("le noeud " + reseau.get(i).getId() + " a été ajouter a aux membres du noeud" + liste_particule.get(m).noeuds.get(l).getId());
                                ancien = nouveau;
                                //reseau.get(i).setId_ch(liste_particule.get(m).noeuds.get(l).getId());
                            }
                        }

                    }

                }
            }

        }

    }

    /**
     * *******************************************************************************************************************
     */
    public double Calcul_cost_fct(double f1, double f2, double beta) {
        double resultat = beta * f1 + (1 - beta) * f2;

        // System.out.println("Cost = " + resultat);
        return resultat;
    }

    /**
     * *******************************************************************************************************************
     */
    public double f__one(Particule par) {
        double f1;
        double max = 0;
        ArrayList<Node> membres;

        for (int j = 0; j < par.noeuds.size(); j++) {
            f1 = 0;

            membres = par.noeuds.get(j).getMembres();
            for (int k = 0; k < membres.size(); k++) // Parcours des membres de chaque CHs
            {
                f1 = f1 + CalculDistance(par.noeuds.get(j).getX(), par.noeuds.get(j).getY(),
                        membres.get(k).getX(), membres.get(k).getY());
            }

            f1 = f1 / membres.size();

            if (f1 > max) {
                max = f1;
            }

        }

        // System.out.println("f1 = " + max);
        return max;
    }

    /**
     * *******************************************************************************************************************
     */
    public double f_two(Particule par) {
        double f2 = 0d;
        double s1 = 0d;
        double s2 = 0d;

        for (int i = 0; i < reseau.size(); i++) {

            if (reseau.get(i).isVivant()) {
                s1 = s1 + reseau.get(i).getEnergie_Résid();

            }

        }

        for (int k = 0; k < par.noeuds.size(); k++) {
            if (par.noeuds.get(k).isVivant()) {
                s2 = s2 + par.noeuds.get(k).getEnergie_Résid();

            }

        }

        f2 = s1 / s2;

        // System.out.println("f2 = " + f2);
        return f2;
    }

    /**
     * *******************************************************************************************************************
     */
    public int Calcul_min_cost(ArrayList<Particule> liste_particule) {
        double min = 1000; // changer en cas d'un plus grand nbr de noeuds
        int indice = -1;

        for (int i = 0; i < liste_particule.size(); i++) {

            if (liste_particule.get(i).coast_fct < min) {

                min = liste_particule.get(i).coast_fct;
                indice = i;

            }

        }

        return indice;
    }

    /**
     * *******************************************************************************************************************
     */
    public double tdma(ArrayList<Node> reseau, Particule particule, Node sink) {
        t_now = 0;

        while (t_now < t_round) {

            for (int i = 0; i < particule.noeuds.size(); i++) {

                ArrayList<Node> membres;
                membres = particule.noeuds.get(i).getMembres();
                durée_frame = membres.size() * t_slot;

                if (particule.noeuds.get(i).isVivant() && particule.noeuds.get(i).getEnergie_Résid() > 0) {

                    for (int k = 0; k < membres.size(); k++) {

                        if (t_now < t_round) {

                            System.out.println("t_now ===========>" + t_now);

                            double E_nécessaire_envoie = CalculEnergieTransmise(CalculDistance(membres.get(k).getX(), membres.get(k).getY(),
                                    particule.noeuds.get(i).getX(), particule.noeuds.get(i).getY()));

                            double E_nécessaire_reception = CalculEnergieRecu();

                            if (membres.get(k).isVivant() && (membres.get(k).getEnergie_Résid() - E_nécessaire_envoie) > 0 && (particule.noeuds.get(i).getEnergie_Résid() - E_nécessaire_reception) > 0) {

                                System.out.println("---Le noeud : " + membres.get(k).getId() + " envoie un packet au CH " + particule.noeuds.get(i).getId() + "---");

                                membres.get(k).setEnergie_Résid(membres.get(k).getEnergie_Résid() - E_nécessaire_envoie);

                                System.out.println("Energie du noeud :" + membres.get(k).getId() + " est : " + membres.get(k).getEnergie_Résid());

                                for (int l = 0; l < reseau.size(); l++) // maj Energie du noeud membre dans la liste reseau
                                {
                                    if (reseau.get(l).getId() == membres.get(k).getId()) {
                                        reseau.get(l).setEnergie_Résid(membres.get(k).getEnergie_Résid());
                                    }

                                }

                                particule.noeuds.get(i).setEnergie_Résid(particule.noeuds.get(i).getEnergie_Résid() - E_nécessaire_reception);

                                System.out.println("Energie du CH :" + particule.noeuds.get(i).getId() + " est : " + particule.noeuds.get(i).getEnergie_Résid());

                                for (int m = 0; m < reseau.size(); m++) // maj Energie du CH dans la liste reseau
                                {
                                    if (reseau.get(m).getId() == particule.noeuds.get(i).getId()) {
                                        reseau.get(m).setEnergie_Résid(particule.noeuds.get(i).getEnergie_Résid());
                                    }

                                }

                                particule.noeuds.get(i).setData_recu(particule.noeuds.get(i).getData_recu() + 1);

                            } else {

                                System.out.println("Le noeud : " + membres.get(k).getId() + " est mort : " + membres.get(k).getEnergie_Résid());
                                membres.get(k).setEnergie_Résid(0);

                            }

                            t_now = t_now + t_slot;
                            System.out.println("<==============================================================>");
                            System.out.println("<==============================================================>");

                        } else {
                            System.out.println("-------------- t_now > t_round " + t_now + " -------------- "); // fin du round
                            System.out.println("----------------END ROUND " + nb_round + " -----------------------");
                            nb_round++;
                            return t_now - t_slot;
                        }

                    }

                    // System.out.println("Le CH "+particule.noeuds.get(i).getId()+" a reçu un total Data de :"+particule.noeuds.get(i).getData_recu()); 
                } else {
                    System.out.println("Le CH : " + particule.noeuds.get(i).getId() + " est mort : " + particule.noeuds.get(i).getEnergie_Résid());
                    particule.noeuds.get(i).setEnergie_Résid(0);
                    t_now = t_now + durée_frame;
                }

                System.out.println("Energie du noeud :" + particule.noeuds.get(i).getEnergie_Résid());

            }

        }

        System.out.println("----------------END ROUND " + nb_round + " -----------------------");
        nb_round++;
        return t_now;
    }

}
