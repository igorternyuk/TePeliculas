/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tepeliculas.dto;

/**
 *
 * @author igor
 */
public class Director extends SimpleDTO{

    public Director() {
    }

    public Director(String name) {
        super(name);
    }

    public Director(int id, String name) {
        super(id, name);
    }
}
