/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.input;

import mario.rm.identifier.Type;

/**
 *
 * @author LENOVO
 */
public interface Reader {
    
    void creaLivello(int x0, int y0, Type type, Type unlockable, String tile);
    
}
