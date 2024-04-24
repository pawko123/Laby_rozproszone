package GraLina;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGraphical extends JFrame {
    private JProgressBar[] progressBars;
    private JButton connectbutton;
    public ClientGraphical() {
        setTitle("Multi-Thread Progress Bar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        add(panel, BorderLayout.CENTER);

        connectbutton = new JButton("Start");
        connectbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client();
            }
        });
        add(connectbutton, BorderLayout.SOUTH);
    }
}
