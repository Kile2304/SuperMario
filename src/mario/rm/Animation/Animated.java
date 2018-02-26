/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.rm.Animation;

import java.awt.image.BufferedImage;

/**
 *
 * @author LENOVO
 */
public interface Animated {
    
    String getType();
    
    String getTile();
    
    BufferedImage getBgImage(String id);
    
}
