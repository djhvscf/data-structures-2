/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuatroraya;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Dennis
 */
public class Ganador extends JFrame implements ActionListener, MouseListener {

    private final JLabel nombreJugador1Lbl;
    private final JButton aceptarBtn;
    
    public Ganador(String nombreGanador) {
        this.setAlwaysOnTop(true);
        setLayout(null);
        //Configuracion del Label nombreJugadorLbl1
        nombreJugador1Lbl = new JLabel(nombreGanador);
        nombreJugador1Lbl.setBounds(10,10,200,30);
        add(nombreJugador1Lbl);
                
        //Configuración del botón aceptarBtn
        aceptarBtn = new JButton("Aceptar");
        aceptarBtn.setBounds(10,80,100,30);
        add(aceptarBtn);
        aceptarBtn.addActionListener(this);
        aceptarBtn.addMouseListener(this);
        this.setVisible(true);
        
        this.setBounds(0,0,400,150);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //Logic
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Logic
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Logic
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //Logic
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //Logic
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        if (me.getSource() == aceptarBtn) {
            this.setVisible(false);
        }
    }
    
}
