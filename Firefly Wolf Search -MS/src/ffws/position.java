/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ffws;

/**
 *
 * @author redha
 */
public class position {

    private double x;
    private double y;

    public position(double x, double y) {
        this.x = x;
        this.y = y;

    }

    /**
     * ************************ Setteurs *************************************
     */
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * ************************ Getteurs *************************************
     */

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
