//package gsampallo;

//Las libreria de swing para poder crear la ventana
import javax.swing.*; 
import java.awt.Dimension;

//Permite interactuar con las teclas
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

//Para poder dibujar en la ventana
import java.awt.Toolkit;

//Para poder tener el punto en el plano
import java.awt.Point;

//Para el color de la Snake
import java.awt.Color;

//Para los graficos
import java.awt.Graphics;

public class Snake extends JFrame { //Debe ser una extension JFrame para poder crear la ventana

    int widht = 640; //Tamaño de la ventana (640x480)
    int height = 480; //Tamaño de la ventana (640x480)

    Point snake;
    Point comida;

    boolean gameOver = false; //Para saber si el juego termino

    ArrayList<Point> lista = new ArrayList<Point>(); //Array de la Snake (para que crezca la Snake)

    int widthPoint = 10; //Ancho de la Snake
    int heightPoint = 10; //Alto de la Snake

    int direccion = KeyEvent.VK_LEFT; //Direccion de la Snake
    long frecuencia = 50; //Frecuencia de la Snake
    ImagenSnake imagenSnake;

    public Snake()  {
        setTitle("Snake");

        setSize(widht, height); //Tamaño de la ventana
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); //Obtiene el tamaño de la pantalla
        this.setLocation(dim.width/2-widht/2, dim.height/2-height/2); //Centra la ventana en la pantalla

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Al cerrar la ventana se termina el programa

        Teclas teclas = new Teclas(); //Se crea un objeto de la clase Teclas
        this.addKeyListener(teclas);

        startGame();

        imagenSnake = new ImagenSnake(); //Se crea un objeto de la clase ImagenSnake
        this.getContentPane().add(imagenSnake);

        setVisible (true);

        Momento momento = new Momento(); //Se crea un objeto de la clase Momento
        Thread trid = new Thread(momento); //Hace el manejo del juego
        trid.start();
    }
    
    public void startGame() {
        comida = new Point(200,200);
        snake = new Point(widht/2, height/2);
       
        lista = new ArrayList<Point>();
        lista.add(snake);

        CrearComida();
    }

    public void CrearComida() {
        Random rnd = new Random();

        comida.x  = rnd.nextInt(widht);
        if ((comida.x % 5) > 0) {
            comida.x = comida.x - (comida.x % 5);
        }

        if(comida.x < 5) {
            comida.x = comida.x + 10;
        }

        comida.y  = rnd.nextInt(height);
        if ((comida.x % 5) > 0) {
            comida.y = comida.y - (comida.y % 5);
        }

        if(comida.y < 5) {
            comida.y = comida.y + 10;
        }
    }

    public static void main(String[] args) throws Exception {
        Snake s = new Snake();
    }

    public void actualizar() { //Vuelve a dibujar el panel 
        imagenSnake.repaint();
        
        lista.add(0, new Point(snake.x, snake.y));
        lista.remove(lista.size()-1);

        for (int i=1;i<lista.size();i++ ) {
            Point punto = lista.get(i);
            if (snake.x == punto.x && snake.y == punto.y) {
                gameOver=true;
            }
        }

        if ((snake.x > (comida.x-10)) && (snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10) && (snake.y > -10))) {
            lista.add(0, new Point(snake.x, snake.y));
            CrearComida();
        }
    }

    public class ImagenSnake extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(0,0,255));
            g.fillRect(snake.x, snake.y, widthPoint, heightPoint);

            for(int i=0;i<lista.size();i++) {
                Point point =(Point)lista.get(i);
                g.fillRect(point.x, point.y, widthPoint, heightPoint);
            }
            
            

            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);
            
            if(gameOver) {
                g.drawString("Game Over", 200, 320);
            }
        }
    }

    public class Teclas extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { //Si se presiona la tecla ESC se cierra el programa
                System.exit(0);
            } else if (e.getKeyCode()==KeyEvent.VK_UP) {
                if(direccion != KeyEvent.VK_DOWN) {
                    direccion = KeyEvent.VK_UP;
                }
            } else if (e.getKeyCode()==KeyEvent.VK_DOWN) {
                if (direccion != KeyEvent.VK_UP) {
                    direccion = KeyEvent.VK_DOWN;
                }
            } else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
                if (direccion != KeyEvent.VK_RIGHT) {
                    direccion = KeyEvent.VK_LEFT;
                }
            } else if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
                if (direccion != KeyEvent.VK_LEFT) {
                    direccion = KeyEvent.VK_RIGHT;
                }
            }
        }    
            
    }

    public class Momento extends Thread {
        long last = 0;
        public void run() {
            while(true) {
                if ((java.lang.System.currentTimeMillis() - last) > frecuencia) {
                    if (!gameOver) {

                        if (direccion == KeyEvent.VK_UP) {
                            snake.y = snake.y - heightPoint;

                            if (snake.y > height) {
                                snake.y = 0;
                            }

                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }

                        } else if(direccion == KeyEvent.VK_DOWN){
                            snake.y = snake.y + heightPoint;

                            if (snake.y > height) {
                                snake.y = 0;
                            }

                            if (snake.y < 0) {
                                snake.y = height - heightPoint;
                            }

                        } else if(direccion == KeyEvent.VK_LEFT) {
                            snake.x = snake.x - widthPoint;

                            if (snake.x > widht) {
                                snake.x = 0;
                            }

                            if (snake.x < 0) {
                                snake.x = widht - widthPoint;
                            }

                        } else if(direccion == KeyEvent.VK_RIGHT) {
                            snake.x = snake.x + widthPoint;

                            if (snake.x > widht) {
                                snake.x = 0;
                            }

                            if (snake.x < 0) {
                                snake.x = widht - widthPoint;
                            }

                        }
                    }
                    actualizar();
                    last = java.lang.System.currentTimeMillis();
                }
            }
        }
    }
}
