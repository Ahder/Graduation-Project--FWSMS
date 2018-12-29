/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author redha
 */
public class Wolf_algo {

    public Particule CHs_pot;
    public Particule liste_CHs_visite;
    public int step_size = 1; //A init !
    public double alpha = 0.2;  //A init !
    public double v = 200; // champ de vision
    public double r_CH = 145; // portée d'un CH, entre 75 et 100 metres
    public int data = 0;
    public Firefly_algo fct = new Firefly_algo();
    public Cardinal_walk move = new Cardinal_walk();
    public int area_size;
    public ArrayList<position> deja_vu;
    public int direction;

    public Wolf_algo(int area_size) {
        this.area_size = area_size;

    }

    /**
     * *******************************************************************************************************************
     */
    public int wolf_start(ArrayList<Node> reseau, Particule particule, Node sink) {
        CHs_pot = new Particule();
        liste_CHs_visite = new Particule();
        deja_vu = new ArrayList<>();
        direction = 1;

        Set set = new HashSet();
        set.addAll(particule.noeuds);
        ArrayList distinctlist = new ArrayList(set);

        while (liste_CHs_visite.noeuds.size() != distinctlist.size()) {

            for (int i = 0; i < reseau.size(); i++) {
                if (reseau.get(i).isVivant()) {
                    double distance = fct.CalculDistance(sink.getX(), sink.getY(), reseau.get(i).getX(), reseau.get(i).getY());
                    if ((distance <= v) && (reseau.get(i).getEtat().equals("CH")) && (!liste_CHs_visite.noeuds.contains(reseau.get(i)))) {
                        CHs_pot.noeuds.add(reseau.get(i));
                        System.out.println("CHs " + reseau.get(i).getId() + " Potentiel trouvé !");

                    }

                }
            }

            if (!CHs_pot.noeuds.isEmpty()) {

                int membres_max = -1;
                int indice = -1;

                for (int j = 0; j < CHs_pot.noeuds.size(); j++) {
                    if (CHs_pot.noeuds.get(j).getMembres().size() >= membres_max) {
                        membres_max = CHs_pot.noeuds.get(j).getMembres().size();
                        indice = j;

                    }

                }

                System.out.println("Le ch avec le plus de membres est : " + CHs_pot.noeuds.get(indice).getId());

                double dist = fct.CalculDistance(sink.getX(), sink.getY(), CHs_pot.noeuds.get(indice).getX(), CHs_pot.noeuds.get(indice).getY());
                System.out.println("la distance est :" + dist);

                double vect_x = (CHs_pot.noeuds.get(indice).getX() - sink.getX());
                double vect_y = (CHs_pot.noeuds.get(indice).getY() - sink.getY());

                while (dist > r_CH) {

                    Random r1 = new Random();

                    double nouvelle_position_x = sink.getX() + vect_x * alpha * r1.nextDouble();
                    sink.setX(nouvelle_position_x);

                    double nouvelle_position_y = sink.getY() + vect_y * alpha * r1.nextDouble();
                    sink.setY(nouvelle_position_y);

                    dist = fct.CalculDistance(sink.getX(), sink.getY(), CHs_pot.noeuds.get(indice).getX(), CHs_pot.noeuds.get(indice).getY());

                    System.out.println("la nouvelle distance est :" + dist);

                }

                // CH transmet donnée a la BS
                double E_nécessaire_envoie = fct.CalculEnergieTransmise(dist);

                if ((CHs_pot.noeuds.get(indice).getEnergie_Résid() - E_nécessaire_envoie > 0) && (CHs_pot.noeuds.get(indice).getData_recu() != 0)) {
                    CHs_pot.noeuds.get(indice).setEnergie_Résid(CHs_pot.noeuds.get(indice).getEnergie_Résid() - E_nécessaire_envoie);
                    data = data + CHs_pot.noeuds.get(indice).getData_recu();
                    System.out.println("CH : " + CHs_pot.noeuds.get(indice).getId() + " transmet a la BS");
                    reseau.get(indice).setData_recu(0);

                } else {
                    System.out.println("CH : " + CHs_pot.noeuds.get(indice).getId() + " is dead");
                    CHs_pot.noeuds.get(indice).setEnergie_Résid(0);
                }

                liste_CHs_visite.noeuds.add(CHs_pot.noeuds.get(indice));
                CHs_pot.noeuds.remove(indice);
                direction = 1;
                sink.setX(area_size / 2);
                sink.setY(area_size / 2);

               // System.out.println("taille de ch potentiel : " + CHs_pot.noeuds.size());

            } else //aucun CH dans le champ de vision de la BS
            {
                if (direction != -1) {

                    direction = move.Calcul_random(sink, area_size, direction);

                    System.out.println("aucun CH dans le champ de vision de la BS =======> mouvement aléatoire");
                    System.out.println("BS X : " + sink.getX() + "      BS Y : " + sink.getY());

                }
            }

        }

        return data;

    }

}
