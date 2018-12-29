/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

import java.util.Random;

/**
 *
 * @author redha
 */
public class Cardinal_walk {

    public Firefly_algo fct = new Firefly_algo();
    public Random r = new Random();
    public double vitesse = 0.01;
    public double vect_x;
    public double vect_y;
    public double nouvelle_position_x;
    public double nouvelle_position_y;

    public Cardinal_walk() {

    }

    public int Calcul_random(Node sink, int area_size, int direction) {

        switch (direction) {

            case 1: //Nord-ouest

                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (0 - sink.getX());
                    vect_y = (0 - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == 0 && sink.getY() == 0) {
                    direction = 2;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;
            case 2: //Nord

                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (area_size / 2 - sink.getX());
                    vect_y = (0 - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == area_size / 2 && sink.getY() == 0) {
                    direction = 3;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;
            case 3: //Nord-est
                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (area_size - sink.getX());
                    vect_y = (0 - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == area_size && sink.getY() == 0) {
                    direction = 4;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;
            case 4: //Est
                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (area_size - sink.getX());
                    vect_y = (area_size / 2 - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == area_size && sink.getY() == area_size / 2) {
                    direction = 5;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;
            case 5: //Sud-est
                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (area_size - sink.getX());
                    vect_y = (area_size - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == area_size && sink.getY() == area_size) {
                    direction = 6;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;
            case 6: //Sud
                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (area_size / 2 - sink.getX());
                    vect_y = (area_size - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == area_size / 2 && sink.getY() == area_size) {
                    direction = 7;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;
            case 7: // Sud-ouest
                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (0 - sink.getX());
                    vect_y = (area_size - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == 0 && sink.getY() == area_size) {
                    direction = 8;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;
            case 8: // Ouest
                if (sink.getX() == area_size / 2 && sink.getY() == area_size / 2) {
                    vect_x = (0 - sink.getX());
                    vect_y = (area_size / 2 - sink.getY());
                }

                nouvelle_position_x = sink.getX() + vect_x * vitesse;
                sink.setX(nouvelle_position_x);

                nouvelle_position_y = sink.getY() + vect_y * vitesse;
                sink.setY(nouvelle_position_y);

                if (sink.getX() == 0 && sink.getY() == area_size / 2) {
                    direction = -1;
                    sink.setX(area_size / 2);
                    sink.setY(area_size / 2);

                }

                break;

            default:
                break;

        }

        System.out.println("direction =" + direction);
        return direction;

    }

}
