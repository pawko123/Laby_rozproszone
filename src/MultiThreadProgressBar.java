package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class MultiThreadProgressBar extends JFrame{
        private JProgressBar[] progressBars;
        private JButton startButton;
        private int numThreads = 4;

        public MultiThreadProgressBar() {
            setTitle("Multi-Thread Progress Bar");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 200);
            setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(numThreads, 1));
            progressBars = new JProgressBar[numThreads];

            for (int i = 0; i < numThreads; i++) {
                progressBars[i] = new JProgressBar(0, 100);
                progressBars[i].setValue(0);
                progressBars[i].setStringPainted(true);
                panel.add(progressBars[i]);
            }

            add(panel, BorderLayout.CENTER);

            startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startThreads();
                }
            });
            add(startButton, BorderLayout.SOUTH);
        }

        private void startThreads() {
            for (int i = 0; i < numThreads; i++) {
                final int threadIndex = i;
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        for (int j = 0; j <= 100; j++) {
                            final int progress = j;
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    progressBars[threadIndex].setValue(progress);
                                }
                            });
                            try {
                                Thread.sleep((int) (Math.random() * 100));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    MultiThreadProgressBar frame = new MultiThreadProgressBar();
                    frame.setVisible(true);
                }
            });
        }
    }

