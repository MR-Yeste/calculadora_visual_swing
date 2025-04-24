package Calculadora;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

/*   
     MR_Yeste
     calculadora_visual_swing
 */  

public class Calculadora extends JFrame implements ActionListener {

    private JTextField display;
    private double primerNumero = 0;
    private String operacion = "";
    private boolean inicio = true;

    public Calculadora() {
        // Configuración básica de la ventana
        setTitle("Calculadora");
        setSize(350, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    // Centra la ventana
        setResizable(false);    //Evita que se redimensione
        getContentPane().setBackground(Color.BLACK); //Fondo negro
        initComponents(); //Carga y configura los componentes
    }

    /**
     * Inicia el display y los botones de la calculadora
     */
    private void initComponents() {
        display = new JTextField();
        display.setFont(new Font("Monospaced", Font.BOLD, 26));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(display, BorderLayout.NORTH);

        // Panel para los botones con diseño en cuadrícula 4x4
        JPanel panelBotones = new JPanel(new GridLayout(4, 4, 5, 5));
        panelBotones.setBackground(Color.BLACK);

        //Valores de los botones de la calculadora
        String[] valores = {
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "*",
            "0", "C", "=", "/"
        };

        //Crea cada botón con su imagen y texto (si falla la imagen) correspondiente
        for (String valor : valores) {
            JButton boton = new JButton();
            boton.setBackground(Color.BLACK);
            boton.setBorder(null);
            boton.setFocusable(false);
            boton.addActionListener(this);
            boton.setActionCommand(valor);

            // Asigna nombre del archivo de icono según el valor
            String nombreArchivo = switch (valor) {
                case "+" -> "suma";
                case "-" -> "resta";
                case "*" -> "multiplicar";
                case "/" -> "dividir";
                case "=" -> "igual";
                case "C" -> "reset";
                default -> valor; //Para los númeross
            };

            String rutaIcono = "/iconos/" + nombreArchivo + ".png";
            URL recurso = getClass().getResource(rutaIcono);

            //Si el icono existe, se usa, y si no, se muestra texto
            if (recurso != null) {
                ImageIcon icono = new ImageIcon(recurso);
                Image imagenEscalada = icono.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagenEscalada));
            } else {
                System.err.println("No se encontró el icono: " + rutaIcono);
                boton.setText(valor);
                boton.setForeground(Color.WHITE);
                boton.setFont(new Font("Monospaced", Font.BOLD, 20));
            }

            panelBotones.add(boton);    // Añade el botón al panel
        }

        // Añade el panel de botones a la ventana
        add(panelBotones, BorderLayout.CENTER);
    }

    /**
     * Maneja los eventos de los botones pulsados
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = ((JButton) e.getSource()).getActionCommand();

        // Si se pulsa un número
        if ("0123456789".contains(input)) {
            if (inicio) {
                display.setText(input); // Reemplaza el contenido
                inicio = false;
            } else {
                display.setText(display.getText() + input); //Añade al final
            }
            
            //Si se pulsa una operación +, -, *, /
        } else if ("+-*/".contains(input)) {
            try {
                primerNumero = Double.parseDouble(display.getText());
                operacion = input; //Guarda la operación seleccionada
                inicio = true; //Espera la entrada del segundo número
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
            
            //Si se pulsa el =
        } else if ("=".equals(input)) {
            try {
                double segundoNumero = Double.parseDouble(display.getText());
                double resultado = switch (operacion) {
                    case "+" -> primerNumero + segundoNumero;
                    case "-" -> primerNumero - segundoNumero;
                    case "*" -> primerNumero * segundoNumero;
                    case "/" -> (segundoNumero != 0) ? primerNumero / segundoNumero : Double.NaN;
                    default -> Double.NaN;
                };
                
                //Muestra el resultado o sale Error si no es válido
                display.setText(Double.isNaN(resultado) ? "Error" : String.valueOf(resultado));
                inicio = true;
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
            
            //Si se pulsa el botón C (clear/reset)
        } else if ("C".equals(input)) {
            display.setText("");
            operacion = "";
            inicio = true;
        }
    }
    
    /**
     * Método principal: ejecuta la aplicación
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculadora().setVisible(true));
    }
}