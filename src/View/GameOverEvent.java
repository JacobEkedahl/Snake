/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.EventObject;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author Jacob
 */
public class GameOverEvent extends Event{
   
    public GameOverEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
   
    
}
