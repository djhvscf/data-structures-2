package cuatroraya;

import javax.swing.*;
import java.awt.event.*;

public class Configuracion extends JFrame implements ActionListener{
    
    private final JTextField nombreJugadorTxt1;
    private final JLabel nombreJugador1Lbl;
    private final JButton aceptarBtn;
    
    public Configuracion() {
        setLayout(null);
        //Configuracion del Label nombreJugadorLbl1
        nombreJugador1Lbl = new JLabel("Nombre del jugador 1:");
        nombreJugador1Lbl.setBounds(10,10,200,30);
        add(nombreJugador1Lbl);
        
        //Configuración del TextField nombreJugadorTxt1
        nombreJugadorTxt1 = new JTextField();
        nombreJugadorTxt1.setBounds(140,15,150,20);
        add(nombreJugadorTxt1);
        
        //Configuración del botón aceptarBtn
        aceptarBtn = new JButton("Aceptar");
        aceptarBtn.setBounds(10,80,100,30);
        add(aceptarBtn);
        aceptarBtn.addActionListener(this);
        
        this.setBounds(0,0,400,150);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == aceptarBtn) {
            String cad = nombreJugadorTxt1.getText();
            setTitle(cad);
        }
    }
}