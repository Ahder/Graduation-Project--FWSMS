/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author redha
 */
public class Trace_simulation extends Application {

    public int TailleReseau = 100; // nombres de noeuds dans le réseau  100 et zone 200x200
    public int MaxParticule = 10; // le nombre de particules = 10% des noeuds
    public int TailleParticule = 5;// nombres de noeuds dans une particule = 5% des noeuds
    public int nb_vivant;
    public double energie_total = 0;
    public int t;
    public double t_now = 0;
    public int MaxGeneration = 2; // itérations
    public double durée_simulation = 1300;
    public Node sink;
    public ArrayList<Node> reseau = new ArrayList<>();
    public ArrayList<Particule> liste_particule = new ArrayList<>();
    public ArrayList<Données> données = new ArrayList<>();
    public ArrayList<Node> liste_mort = new ArrayList<>();
    public Particule par_min = new Particule();
    public Firefly_algo ntw;
    public Wolf_algo wolf;
    public Group root;
    public int area_size = 500; // a Initialiser !

    @Override
    public void start(Stage primaryStage) {

        root = new Group();
        Scene scene = new Scene(root, area_size, area_size);
        primaryStage.setTitle("Firefly Wolf Search - Moving BS");
        primaryStage.setScene(scene);

        sink = new Node(-1, area_size / 2, area_size / 2, "Sink");

        ntw = new Firefly_algo(reseau, liste_particule, TailleReseau, MaxParticule, TailleParticule, area_size);
        ntw.InitResau(reseau);
        Démarrage(reseau, sink);

        wolf = new Wolf_algo(area_size);

        nb_vivant = reseau.size();
        energie_total = Calcul_total_energie(reseau);

        données.add(new Données(t_now, Calcul_vivant(reseau), energie_total, 0));

        System.out.println("******************************** t_now =" + t_now + " ********************************");

//---------------------------------- Debut ----------------------------------//
        while (t_now < durée_simulation) { //t_now < durée_simulation && nb_vivant > TailleParticule

            liste_particule = ntw.choix_particule(reseau, MaxParticule);
            t = 0;
            int data = 0;

            while (t < MaxGeneration) {

                ntw.Clustering(reseau, liste_particule);

                for (int j = 0; j < liste_particule.size(); j++) ////////// Calcul du cout
                {
                    //System.out.println("Calcul de la fonction COST de la particule "+j );
                    double f1 = ntw.f__one(liste_particule.get(j));
                    double f2 = ntw.f_two(liste_particule.get(j));
                    double cost = ntw.Calcul_cost_fct(f1, f2, 0.2);
                    liste_particule.get(j).coast_fct = cost;
                }

                int indice = ntw.Calcul_min_cost(liste_particule);
                //System.out.println("le coast min appartient a la particule :" + indice);
                par_min = liste_particule.get(indice);

                if (t != MaxGeneration - 1) {

                    // System.out.println("-------------------les noeuds de la particule min---------------------");
                    for (int i = 0; i < par_min.noeuds.size(); i++) {

                        // System.out.println("Noeud " + par_min.noeuds.get(i).getId());
                        par_min.noeuds.get(i).setMembres(new ArrayList<>());

                    }

                    Réinitialisation();

                    liste_particule = ntw.choix_particule(reseau, MaxParticule - 1);
                    liste_particule.add(par_min);

                }

                t++;

            }

            Réinitialisation();
            liste_particule.clear();
            liste_particule.add(par_min);
            ntw.Clustering(reseau, liste_particule);

            double t_round = ntw.tdma(reseau, par_min, sink);

            nb_vivant = Test_energie(reseau);
            data = data + wolf.wolf_start(reseau, par_min, sink);

            nb_vivant = Test_energie(reseau);
            energie_total = Calcul_total_energie(reseau);

            t_now = t_now + t_round;

            données.add(new Données(t_now, Calcul_vivant(reseau), energie_total, data));

            System.out.println("******************************** t_now =" + t_now + " ********************************");

        }

        //-------------- Vider liste_particules avant le dessins -----------------//
        for (int j = 0; j < liste_particule.size(); j++) {

            for (int k = 0; k < liste_particule.get(j).noeuds.size(); k++) {

                if (!liste_particule.get(j).noeuds.get(k).isVivant()) {
                    liste_particule.get(j).noeuds.remove(k);
                } else {
                    ArrayList<Node> membres;
                    membres = liste_particule.get(j).noeuds.get(k).getMembres();

                    for (int l = 0; l < membres.size(); l++) {
                        if (!membres.get(l).isVivant()) {
                            membres.remove(l);
                        }
                    }
                }

            }

        }

        //------------------------------------------------------------------//  
        Dessiner_cluster(liste_particule);

        Courbe_vivants(données);
        Courbe_energie(données);
        Courbe_donnéesBs_recu(données);

        primaryStage.show();

    }

    /**
     * *******************************************************************************************************************
     */
    public void Démarrage(ArrayList<Node> reseau, Node sink) {

        for (int i = 0; i < reseau.size(); i++) {

            if (reseau.get(i).isVivant()) {
                Circle noeud = new Circle();
                Label label = new Label(String.valueOf(reseau.get(i).getId()));
                label.setLayoutX(reseau.get(i).getX() + 2);
                label.setLayoutY(reseau.get(i).getY() + 2);

                noeud.setCenterX(reseau.get(i).getX());
                noeud.setCenterY(reseau.get(i).getY());
                noeud.setRadius(5);
                noeud.setFill(Color.RED);
                noeud.setStroke(Color.BLACK);
                noeud.setStrokeWidth(1);

                reseau.get(i).setNoeud(noeud);
                reseau.get(i).setLabel(label);

                root.getChildren().addAll(noeud, label);

            }

        }

        Circle noeud = new Circle();
        Label label = new Label("Sink");
        label.setLayoutX(sink.getX() + 2);
        label.setLayoutY(sink.getY() + 2);

        noeud.setCenterX(sink.getX());
        noeud.setCenterY(sink.getY());
        noeud.setRadius(10);
        noeud.setFill(Color.YELLOW);
        noeud.setStroke(Color.BLACK);
        noeud.setStrokeWidth(3);

        sink.setNoeud(noeud);
        root.getChildren().addAll(noeud, label);

    }

    /**
     * *******************************************************************************************************************
     */
    public void Dessiner_cluster(ArrayList<Particule> liste_particule) {

        for (int j = 0; j < liste_particule.size(); j++) {

            for (int k = 0; k < liste_particule.get(j).noeuds.size(); k++) {

                if (liste_particule.get(j).noeuds.get(k).isVivant()) {

                    ArrayList<Node> membres;
                    membres = liste_particule.get(j).noeuds.get(k).getMembres();

                    // System.out.println("la liste du noeud" + liste_particule.get(j).noeuds.get(k).getId() + " : " + liste_particule.get(j).noeuds.get(k).afficherMembres());
                    if (!membres.isEmpty()) {
                        for (int l = 0; l < membres.size(); l++) {
                            if (membres.get(l).isVivant()) {
                                Line line = new Line(liste_particule.get(j).noeuds.get(k).getX(), liste_particule.get(j).noeuds.get(k).getY(), membres.get(l).getX(), membres.get(l).getY());
                                root.getChildren().add(line);
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
    public void Réinitialisation() {

        for (int i = 0; i < reseau.size(); i++) {
            reseau.get(i).setEtat("Normal");
            reseau.get(i).getNoeud().setFill(Color.RED);
            reseau.get(i).setMembres(new ArrayList<>());

        }

        for (int i = 0; i < reseau.size(); i++) {
            for (int j = 0; j < par_min.noeuds.size(); j++) {
                if (reseau.get(i).getId() == par_min.noeuds.get(j).getId()) {
                    reseau.get(i).setEtat("CH");
                    reseau.get(i).getNoeud().setFill(Color.BLUE);
                    //System.out.println("Le noeud " + reseau.get(i).getId() + " reste CH");
                }

            }

        }
    }

    /**
     * *******************************************************************************************************************
     */
    public int Test_energie(ArrayList<Node> reseau) {

        int nb_vivant = 0;

        for (int i = 0; i < reseau.size(); i++) {
            if (reseau.get(i).getEnergie_Résid() <= 0) {
                reseau.get(i).setVivant(false);
                //System.out.println("Le noeud " + reseau.get(i).getId() + " a été rétiré de la liste");
                root.getChildren().remove(reseau.get(i).getNoeud());
                root.getChildren().remove(reseau.get(i).getLabel());

            } else {
                nb_vivant++;
            }
        }

        return nb_vivant;
    }

    /**
     * *******************************************************************************************************************
     */
    public double Calcul_total_energie(ArrayList<Node> reseau) {
        double somme = 0;
        for (int i = 0; i < reseau.size(); i++) {
            if (reseau.get(i).isVivant()) {
                somme = somme + reseau.get(i).getEnergie_Résid();

            }

        }
        return somme;
    }

    /**
     * *******************************************************************************************************************
     */
    public int Calcul_vivant(ArrayList<Node> reseau) {
        int somme = 0;
        for (int i = 0; i < reseau.size(); i++) {
            if (reseau.get(i).isVivant()) {
                somme++;

            }

        }
        return somme;
    }

    /**
     * *******************************************************************************************************************
     */
    public void Courbe_vivants(ArrayList<Données> données) {
        // System.out.println("Size donnée =" + données.size());
        //System.out.println("nbr rounds =" + ntw.getNb_round());

        Stage stage = new Stage();

        final LineChart.Series series = new LineChart.Series<>();
        series.setName("nbr_noeuds");

        for (int i = 0; i < données.size(); i++) {
            final LineChart.Data data = new LineChart.Data(données.indexOf(données.get(i)), données.get(i).getNb_nodes());
            series.getData().add(data);
            /* int round = données.indexOf(données.get(i));
            switch(round)
            {
                case 10 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    case 25 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 50 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                      case 75 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 100 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 125 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 150 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                      case 200 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 250 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                            
                    case 300 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    case 350 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 400 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                      case 500 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 600 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 700:
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 800 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                      case 900 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                      case 1000 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                    
                    case 1100 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" nombre de noeuds : "+données.get(i).getNb_nodes());
                    break;
                           
            }*/

        }

        final NumberAxis xAxis = new NumberAxis(0, ntw.getNb_round(), 100);
        final NumberAxis yAxis = new NumberAxis(0, TailleReseau, 10);
        final LineChart chart = new LineChart(xAxis, yAxis);

        chart.getData().addAll(series);

        xAxis.setLabel("Rounds");
        yAxis.setLabel("Noeuds surv.");
        chart.setTitle("Courbe des noeuds survivants");

        final StackPane stkpane = new StackPane();
        stkpane.getChildren().add(chart);
        final Scene scene = new Scene(stkpane, 500, 500);
        stage.setTitle("Courbe des noeuds survivants");
        stage.setScene(scene);
        stage.show();

        stage.show();

        System.out.println("*************************************************************************");
    }

    /**
     * *******************************************************************************************************************
     */
    public void Courbe_energie(ArrayList<Données> données) {

        Stage stage = new Stage();

        final LineChart.Series series = new LineChart.Series<>();
        series.setName("énergie_totale");

        for (int i = 0; i < données.size(); i++) {
            final LineChart.Data data = new LineChart.Data(données.indexOf(données.get(i)), données.get(i).getTotal_energie());
            series.getData().add(data);
            /* int round = données.indexOf(données.get(i));
            switch(round)
            {
                case 10 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    case 25 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 50 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                      case 75 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 100 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 125 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 150 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                      case 200 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 250 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                     case 300 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    case 350 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 400:
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                      case 500 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 600 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 700 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 800 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                      case 900 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                      case 1000 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                          case 1100 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" total energie : "+données.get(i).getTotal_energie());
                    break;
                    
                    
            }*/

        }

        final NumberAxis xAxis = new NumberAxis(0, ntw.getNb_round(), 100);
        final NumberAxis yAxis = new NumberAxis(0, sink.getEnergie_Init() * TailleReseau, 10);
        final LineChart chart = new LineChart(xAxis, yAxis);

        chart.getData().addAll(series);

        xAxis.setLabel("Rounds");
        yAxis.setLabel("Energie (Jouls)");
        chart.setTitle("Courbe de l'énergie totale");

        final StackPane stkpane = new StackPane();
        stkpane.getChildren().add(chart);
        final Scene scene = new Scene(stkpane, 500, 500);
        stage.setTitle("Courbe de l'énergie totale");
        stage.setScene(scene);
        stage.show();

        stage.show();
        System.out.println("*************************************************************************");
    }

    /**
     * *******************************************************************************************************************
     */
    public void Courbe_donnéesBs_recu(ArrayList<Données> données) {

        Stage stage = new Stage();

        final LineChart.Series series = new LineChart.Series<>();
        series.setName("Data received");

        for (int i = 0; i < données.size(); i++) {
            final LineChart.Data data = new LineChart.Data(données.indexOf(données.get(i)), données.get(i).getDonnées_bs());
            series.getData().add(data);

            /*int round = données.indexOf(données.get(i));
            switch(round)
            {
                case 10 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    case 25 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 50 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                      case 75 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 100 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 125 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 150 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                      case 200 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 250 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                     case 300 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    case 350 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 400 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                      case 500 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 600 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 700 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 800 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                      case 900 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                      case 1000 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                     case 1100 :
                    System.out.println("Le round : "+données.indexOf(données.get(i)) +" || "+" données envoyeés : "+données.get(i).getDonnées_bs());
                    break;
                    
                    
            }*/
        }

        final NumberAxis xAxis = new NumberAxis(0, ntw.getNb_round(), 100);
        final NumberAxis yAxis = new NumberAxis(0, données.get(données.size() - 1).getDonnées_bs(), 1000);
        final LineChart chart = new LineChart(xAxis, yAxis);

        chart.getData().addAll(series);

        xAxis.setLabel("Rounds");
        yAxis.setLabel("Data received");
        chart.setTitle("Courbe des données reçues par la BS");

        final StackPane stkpane = new StackPane();
        stkpane.getChildren().add(chart);
        final Scene scene = new Scene(stkpane, 500, 500);
        stage.setTitle("Courbe des données reçues par la BS");
        stage.setScene(scene);
        stage.show();

        stage.show();
        System.out.println("*************************************************************************");
    }

    public static void main(String args[]) {
        launch(args);
    }

}
