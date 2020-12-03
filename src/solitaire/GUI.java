package solitaire;

import Clock.MinuteThread;
import Clock.SecondThread;
import control.ClientControl;
import static control.ClientControl.userResult;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.User;

import solitaire.Pile.PileType;
import view.HomeView;

public class GUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    private JMenuBar menuBar;

    // Map all the GUI text
    Map<String, String> displayText;
    JPanel gameArea;
    JPanel columns;
    JPanel topColumns;
    JLayeredPane lp;
    Engine game;
    ClientControl clientControl;
       private HomeView homeView;
    // tu viet
    private Label lblTime = new Label("Thoi gian con lai : "+ "3: 00");
 
    private int minute , second ;
    // Auxiliary elements to use while dragging
    Pile tempPile;
    Point mouseOffset;
   private MinuteThread mtd ;
   private SecondThread std ;
    /**
     * GUI class constructor
     */
    public GUI(Engine game,ClientControl clientControl,HomeView homeView) {
     mtd =new MinuteThread(GUI.this,1);
     std = new SecondThread(GUI.this, mtd,60);
        this.game = game;
        this.clientControl=clientControl;
        this.homeView=homeView;
        // Initialize stuff
        createTextMap();
        // tao dong ho

        // Window settings
        setTitle("Game Solitaire of group 16");
        setSize(900, 700);
        lblTime.setSize(150, 50);
        lblTime.setLocation(50, 580);
       // lblTime.setBackground(Color.red);
        this.add(lblTime);
    //=====================================  
       
       
  //======================================  
        
        try {
            setContentPane((new JPanelWithBackground("../images/background.jpg")));
            setContentPane(lblTime);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        gameArea = new JPanel();
        gameArea.setOpaque(false);
        gameArea.setLayout(new BoxLayout(gameArea, BoxLayout.PAGE_AXIS));

        // Center the window
        setLocationRelativeTo(null);

        // Window close event
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Add GUI elements
        createTopMenu();

        // Flow layout to display multiple columns on the same row
        FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
        flow.setAlignOnBaseline(true);

        // Add the columns panel
        columns = new JPanel();
        columns.setOpaque(false);
        columns.setLayout(flow);
        columns.setMinimumSize(new Dimension(200, 900));

        // Add the top columns panel
        FlowLayout topFlow = new FlowLayout(FlowLayout.LEFT);
        topFlow.setAlignOnBaseline(true);

        topColumns = new JPanel();
        topColumns.setOpaque(false);
        topColumns.setLayout(topFlow);

        gameArea.add(topColumns);
        gameArea.add(columns);

        //layers.add(dragLayer, JLayeredPane.DRAG_LAYER);
        add(gameArea);

        // Display the window
        lp = getLayeredPane();
        setVisible(true);

        // Auxiliarry elements
        mouseOffset = new Point(0, 0);

        initialize();
        //Create 
        game.load();
	validate();
    }

    public MinuteThread getMtd() {
        return mtd;
    }

    public void setMtd(MinuteThread mtd) {
        this.mtd = mtd;
    }

    public SecondThread getStd() {
        return std;
    }

    public void setStd(SecondThread std) {
        this.std = std;
    }

    
    
    /**
     * Add cards from the game to the GUI
     */
    private void initialize() {
        topColumns.removeAll();
        columns.removeAll();

        // Add a listener for each card
        for (Card c : game.deck.cards) {
            c.addMouseListener(this);
            c.addMouseMotionListener(this);
        }

        game.setupGame();
        for (Pile p : game.piles) {
            columns.add(p);
        }

        topColumns.add(game.drawPile);
        topColumns.add(game.getPile);

        for (Pile p : game.finalPiles) {
            topColumns.add(p);
        }

        validate();
    }
    //ham tu viet

    public void msg(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void setSecond(int second) {

        this.second = second;
    lblTime.setText("Thoi gian con lai :" + minute + ":" + second);
        

    }

    public void setMinute(int minute) {
        this.minute = minute;
         lblTime.setText("Thoi gian con lai :"+ + minute + ":" + second);
  
    }

    /**
     * Resets the whole game
     */
    public void reset(int de) {
        
     //     int dePrev = Integer.parseInt(game.getFile());
         
      
                  mtd =new MinuteThread(GUI.this,1);
                   std = new SecondThread(GUI.this, mtd,60);
     

                game.setFile(String.valueOf(de));               
              game.load();
            validate(); 
            mtd.start();std.start();
       // game.resetCards();
//        initialize();
//        repaint();
    }

    /**
     * Creates the displayText map Change this if you want to translate the game
     * into another language
     */
    private void createTextMap() {
        displayText = new HashMap<String, String>();

        displayText.put("Menu", "Menu");
        displayText.put("New", "New");
        displayText.put("Save", "Save");
        displayText.put("Load", "Load");
        displayText.put("Home", "Home");
        displayText.put("Exit", "Exit");
    }

    /**
     * Create the top menu bar
     */
    private void createTopMenu() {
        menuBar = new JMenuBar();

        JMenu FileMenu = new JMenu("Menu");
        FileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(FileMenu);

        menuOption[] fileOptions = new menuOption[]{
//            new menuOption(displayText.get("New"), KeyEvent.VK_N),
//            new menuOption(displayText.get("Save"), KeyEvent.VK_S),
//            new menuOption(displayText.get("Load"), KeyEvent.VK_L),
           new menuOption(displayText.get("Home"), KeyEvent.VK_H),
            new menuOption(displayText.get("Exit"), KeyEvent.VK_X)
        };

        for (menuOption option : fileOptions) {
            JMenuItem opt = new JMenuItem(option.name);
            if (option.shorcut != 0) {
                opt.setMnemonic(option.shorcut);
            }

            opt.addActionListener(this);
            FileMenu.add(opt);
        }

        setJMenuBar(menuBar);
    }

    private void setContentPane( Label lblTime) {
        //
       //  lblTime.setSize(10, 10);       
        lblTime.setFont(new Font("Arial", 14, 14));    
        lblTime.setForeground(Color.red); 
        GUI.this.add(lblTime);
                mtd.start();
                std.start();       
    }

    public void sendResult(String str){
    clientControl.sendData(str);
    }
    
    public void CheckUserOff(String valueOf) {
       //  System.out.println("cau lenh :exit-" +valueOf+"-"+getTime());
       
            clientControl.sendData("exit-" +valueOf+"-"+getTime());
         //   clientControl.closeConnection();
    }

    private void returnHome(ClientControl clientControl) {
        this.dispose();
        mtd.stop();std.stop();
       homeView.setVisible(true);
        User user=  clientControl.getUserResult();
      clientControl.sendData("CheckSatusUser-"+user.getId()+"-playgame");
      //  System.out.println("CheckSatusUser"+user.getId());
    }

    /**
     * Auxiliary class which stores information about a single menu option
     *
     * @member {String} name The name of the
     * @member {Integer} shortcut The mnemonic for this button
     */
    class menuOption {

        public String name;
        public Integer shorcut = 0;

        public menuOption(String name, Integer shorcut) {
            this.name = name;
            this.shorcut = shorcut;
        }
    }

    /**
     * Function to handle most of the events performed on the GUI
     */
    public void actionPerformed(ActionEvent e) {

        // Handle all menu interactions
        if (e.getSource() instanceof JMenuItem) {
            handleMenuInteraction(e);
        }

    }

    /**
     * Handles the activation of any of the menu bar buttons
     *
     * @param {ActionEvent} e
     */
    private void handleMenuInteraction(ActionEvent e) {
        JMenuItem item = (JMenuItem) e.getSource();

        if (item.getText().equals(displayText.get("Exit"))) {
            
             if (JOptionPane.showConfirmDialog(this, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
           
            CheckUserOff(String.valueOf(clientControl.userResult.getId())); 
          
            this.dispose();
           System.exit(0);
            return;
             }
        }
        if (item.getText().equals(displayText.get("New"))) {
           // reset();
            return;
        }
        if (item.getText().equals(displayText.get("Save"))) {
            game.save();
            JOptionPane.showMessageDialog(this, "Game saved!");
            return;
        }
        if (item.getText().equals(displayText.get("Load"))) {
            game.load();
            validate();
            return;
        }
        if (item.getText().equals(displayText.get("Home"))) {
         returnHome(clientControl);
            return;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (tempPile != null) {

            Point pos = getLocationOnScreen();
            pos.x = e.getLocationOnScreen().x - pos.x - mouseOffset.x;
            pos.y = e.getLocationOnScreen().y - pos.y - mouseOffset.y;

            tempPile.setLocation(pos);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent() instanceof Card) {
            Card c = (Card) e.getComponent();
            Pile p = (Pile) c.getParent();

            switch (p.type) {
                case Draw:
                    game.drawCard();
                    break;
                case Normal:
                    game.clickPile(p);
                    break;
                case Get:
                    game.turnGetPile();
                    break;
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getComponent() instanceof Card) {
            Card c = (Card) e.getComponent();

            // Do nothing if card is reversed
            if (c.isReversed) {
                return;
            }

            Pile p = (Pile) c.getParent();

            if (p.cards.isEmpty() || p.type == PileType.Final) {
                return;
            }

            tempPile = p.split(c);

            lp.add(tempPile, JLayeredPane.DRAG_LAYER);

            Point pos = getLocationOnScreen();
            mouseOffset = e.getPoint();
            pos.x = e.getLocationOnScreen().x - pos.x - mouseOffset.x;
            pos.y = e.getLocationOnScreen().y - pos.y - mouseOffset.y;

            tempPile.setLocation(pos);

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (tempPile != null) {

            Point mousePos = e.getLocationOnScreen();
            boolean match = false;

            // Check if pile can merge with the pile it is dropped on
            ArrayList<Pile> droppable = new ArrayList<Pile>(game.piles);
            droppable.addAll(game.finalPiles);

            for (Pile p : droppable) {
                Point pilePos = p.getLocationOnScreen();
                Rectangle r = p.getBounds();
                r.x = pilePos.x;
                r.y = pilePos.y;

                if (r.contains(mousePos) && p.acceptsPile(tempPile)) {
                    p.merge(tempPile);
                    match = true;
                    break;
                }
            }

            // Snap back if no merge is found
            if (!match) {
                tempPile.parent.merge(tempPile);
            }

            lp.remove(tempPile);
            tempPile = null;

            repaint();

            if (game.checkWin()) {
               
                 mtd.stop();
                 std.stop();
                 clientControl.sendData("You winner:"+ getTime());
                 msg("Bạn thắng");
                 //   JOptionPane.showMessageDialog(this, "You won! Congrats!");
              
                checkRequestRepeat();
            }
        }
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }
    public void checkRequestRepeat(){
              
             int i =   JOptionPane.showConfirmDialog(this, "You continue ??", "Continue", JOptionPane.YES_NO_OPTION);
          // dong y choi tiep  
             if (i==0) {

                     clientControl.sendData("dong y choi tiep" );
                }
             //khong choi tiep
             
                else {
                 clientControl.sendData("khong choi tiep");
                 System.exit(0);
             }
            
    
    }
    public void StopGame(){
        System.out.println("stop");
        mtd.stop();std.stop();
    }
    public String getTime(){
        
        return lblTime.getText();
        
    }
    
    public class JPanelWithBackground extends JPanel {

        private Image backgroundImage;

        // Some code to initialize the background image.
        // Here, we use the constructor to load the image. This
        // can vary depending on the use case of the panel.
        public JPanelWithBackground(String fileName) throws IOException {
            URL urlToImage = this.getClass().getResource(fileName);
            backgroundImage = ImageIO.read(urlToImage);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the background image.
            g.drawImage(backgroundImage, 0, 0, this);
        }
    }
    // close app
    public void close(){
     if (JOptionPane.showConfirmDialog(this, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                        try {
                                            //     System.out.println(result.getId());
                                          //  sendRequest(String.valueOf(userResult.getId()), gui);
                                          this.CheckUserOff("exit-" +String.valueOf(clientControl.userResult.getId())+"-gui");
                                        System.out.println(this.getTime()+"---------------------------");
                                        String timeFinish[] = this.getTime().split("\\:");
                                            for (String string : timeFinish) {
                                                System.out.println(string);
                                            }
                                        
                                        
                                        } catch (Exception ex) {
                                            Logger.getLogger(ClientControl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        
                                       
                                        System.exit(0);
                                    }
    }
    
    
    
//    public static void main(String[] args) {
//       
//           
//                ClientControl clientControl = new ClientControl();
//                
//                Engine game = new Engine("1");
//                HomeView homeView = new HomeView(clientControl);
//                GUI gui = new GUI(game,clientControl,homeView);
//              
//           
//
//    }

}
