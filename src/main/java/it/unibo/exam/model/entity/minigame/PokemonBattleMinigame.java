package it.unibo.exam.model.entity.minigame;

import it.unibo.exam.model.entity.pokemon.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Pokemon-style battle minigame: Students vs Pianini
 * Easter Egg minigame with customizable moves, damage, and HP.
 */
public class PokemonBattleMinigame implements Minigame {
    
    private JFrame battleFrame;
    private MinigameCallback callback;
    private long startTime;
    
    // Battle state
    private List<Pokemon> studentTeam;
    private List<Pokemon> pianiniTeam;
    private Pokemon currentStudent;
    private Pokemon currentPianini;
    private int studentIndex = 0;
    private int pianiniIndex = 0;
    private boolean playerTurn = true;
    private boolean battleEnded = false;
    
    // UI Components
    private JLabel studentHpLabel;
    private JLabel pianiniHpLabel;
    private JProgressBar studentHpBar;
    private JProgressBar pianiniHpBar;
    private JTextArea battleLog;
    private JPanel moveButtonsPanel;
    private JButton[] moveButtons;
    
    @Override
    public void start(JFrame parentFrame, MinigameCallback onComplete) {
        this.callback = onComplete;
        this.startTime = System.currentTimeMillis();
        
        initializeBattle();
        createBattleWindow(parentFrame);
        startBattle();
    }
    
    @Override
    public void stop() {
        if (battleFrame != null) {
            battleFrame.dispose();
        }
    }
    
    @Override
    public String getName() {
        return "Pokemon Battle: Students vs Pianini";
    }
    
    @Override
    public String getDescription() {
        return "Epic Pokemon-style battle between Students and Professors!";
    }
    
    /**
     * Initializes the battle teams and starting Pokemon.
     */
    private void initializeBattle() {
        studentTeam = PokemonFactory.createStudentTeam();
        pianiniTeam = PokemonFactory.createPianiniTeam();
        
        currentStudent = studentTeam.get(0);
        currentPianini = pianiniTeam.get(0);
        
        studentIndex = 0;
        pianiniIndex = 0;
        playerTurn = true;
        battleEnded = false;
    }
    
    /**
     * Creates the main battle window with Pokemon battle UI.
     */
    private void createBattleWindow(JFrame parentFrame) {
        battleFrame = new JFrame("Pokemon Battle - " + getName());
        battleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        battleFrame.setSize(800, 600);
        battleFrame.setLocationRelativeTo(parentFrame);
        battleFrame.setResizable(false);
        battleFrame.setLayout(new BorderLayout());
        
        // Create main panels
        JPanel topPanel = createTopPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();
        
        battleFrame.add(topPanel, BorderLayout.NORTH);
        battleFrame.add(centerPanel, BorderLayout.CENTER);
        battleFrame.add(bottomPanel, BorderLayout.SOUTH);
        
        battleFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (callback != null) {
                    callback.onComplete(false, getElapsedSeconds());
                }
            }
        });
        
        battleFrame.setVisible(true);
    }
    
    /**
     * Creates the top panel with Pokemon info and HP bars.
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(135, 206, 235)); // Sky blue background
        
        // Pianini (opponent) info panel
        JPanel pianiniPanel = createPokemonInfoPanel(currentPianini, true);
        
        // Student (player) info panel  
        JPanel studentPanel = createPokemonInfoPanel(currentStudent, false);
        
        topPanel.add(pianiniPanel);
        topPanel.add(studentPanel);
        
        return topPanel;
    }
    
    /**
     * Creates an info panel for a Pokemon with HP bar and stats.
     */
    private JPanel createPokemonInfoPanel(Pokemon pokemon, boolean isOpponent) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(isOpponent ? new Color(255, 200, 200) : new Color(200, 255, 200));
        panel.setBorder(BorderFactory.createTitledBorder(
            isOpponent ? "Professor Team" : "Student Team"));
        
        // Pokemon name and level
        JLabel nameLabel = new JLabel(pokemon.getName() + " Lv.50");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // HP info and bar
        JPanel hpPanel = new JPanel(new BorderLayout());
        JLabel hpLabel = new JLabel(pokemon.getCurrentHp() + "/" + pokemon.getMaxHp() + " HP");
        hpLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JProgressBar hpBar = new JProgressBar(0, pokemon.getMaxHp());
        hpBar.setValue(pokemon.getCurrentHp());
        hpBar.setStringPainted(true);
        hpBar.setString(pokemon.getHpPercentage() + "%");
        updateHpBarColor(hpBar, pokemon);
        
        hpPanel.add(hpLabel, BorderLayout.WEST);
        hpPanel.add(hpBar, BorderLayout.CENTER);
        
        panel.add(nameLabel, BorderLayout.NORTH);
        panel.add(hpPanel, BorderLayout.CENTER);
        
        // Store references for updates
        if (isOpponent) {
            pianiniHpLabel = hpLabel;
            pianiniHpBar = hpBar;
        } else {
            studentHpLabel = hpLabel;
            studentHpBar = hpBar;
        }
        
        return panel;
    }
    
    /**
     * Updates HP bar color based on Pokemon's health.
     */
    private void updateHpBarColor(JProgressBar hpBar, Pokemon pokemon) {
        String colorName = pokemon.getHpColor();
        Color color;
        switch (colorName) {
            case "green": color = Color.GREEN; break;
            case "yellow": color = Color.YELLOW; break;
            case "red": color = Color.RED; break;
            default: color = Color.GRAY; break;
        }
        hpBar.setForeground(color);
    }
    
    /**
     * Creates the center panel with battle visualization.
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(144, 238, 144)); // Light green battlefield
        
        // Battle scene
        JPanel battleScene = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBattleScene(g, getWidth(), getHeight());
            }
        };
        battleScene.setPreferredSize(new Dimension(400, 200));
        battleScene.setBackground(new Color(144, 238, 144));
        
        centerPanel.add(battleScene, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    /**
     * Draws the battle scene with simple Pokemon representations.
     */
    private void drawBattleScene(Graphics g, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw student Pokemon (left side)
        if (!currentStudent.isFainted()) {
            g2d.setColor(Color.BLUE);
            g2d.fillOval(50, height - 80, 60, 60);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("STUDENT", 55, height - 45);
        }
        
        // Draw pianini Pokemon (right side)
        if (!currentPianini.isFainted()) {
            g2d.setColor(Color.RED);
            g2d.fillOval(width - 110, 20, 60, 60);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("PIANINI", width - 105, 55);
        }
        
        // Draw battle arena lines
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, height - 20, width, height - 20);
        g2d.drawLine(0, 100, width, 100);
    }
    
    /**
     * Creates the bottom panel with battle log and move buttons.
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Battle log
        battleLog = new JTextArea(8, 50);
        battleLog.setEditable(false);
        battleLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        battleLog.setBackground(new Color(248, 248, 255));
        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Move buttons panel
        moveButtonsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        moveButtonsPanel.setBorder(BorderFactory.createTitledBorder("Choose Your Move"));
        createMoveButtons();
        
        bottomPanel.add(logScroll, BorderLayout.CENTER);
        bottomPanel.add(moveButtonsPanel, BorderLayout.SOUTH);
        
        return bottomPanel;
    }
    
    /**
     * Creates move buttons based on current Pokemon's moves.
     */
    private void createMoveButtons() {
        moveButtonsPanel.removeAll();
        
        List<Move> moves = currentStudent.getMoves();
        moveButtons = new JButton[4];
        
        for (int i = 0; i < 4; i++) {
            if (i < moves.size()) {
                Move move = moves.get(i);
                JButton button = new JButton("<html><center>" + move.getName() + 
                                            "<br>DMG: " + move.getDamage() + "</center></html>");
                button.setFont(new Font("Arial", Font.BOLD, 11));
                
                // Color code by move type
                switch (move.getType()) {
                    case PHYSICAL:
                        button.setBackground(new Color(255, 200, 200));
                        break;
                    case SPECIAL:
                        button.setBackground(new Color(200, 200, 255));
                        break;
                    case STATUS:
                        button.setBackground(new Color(255, 255, 200));
                        break;
                }
                
                final Move selectedMove = move;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (playerTurn && !battleEnded) {
                            playerAttack(selectedMove);
                        }
                    }
                });
                
                moveButtons[i] = button;
            } else {
                JButton emptyButton = new JButton("---");
                emptyButton.setEnabled(false);
                moveButtons[i] = emptyButton;
            }
            
            moveButtonsPanel.add(moveButtons[i]);
        }
        
        moveButtonsPanel.revalidate();
        moveButtonsPanel.repaint();
    }
    
    /**
     * Starts the battle with opening message.
     */
    private void startBattle() {
        addToBattleLog("=== POKEMON BATTLE: STUDENTS vs PIANINI ===");
        addToBattleLog("");
        addToBattleLog("Professor Pianini challenges you to a battle!");
        addToBattleLog("Go! " + currentStudent.getName() + "!");
        addToBattleLog("Professor sends out " + currentPianini.getName() + "!");
        addToBattleLog("");
        addToBattleLog("Choose your move!");
        
        updateUI();
    }
    
    /**
     * Handles player's attack move.
     */
    private void playerAttack(Move move) {
        if (battleEnded || !playerTurn) return;
        
        playerTurn = false;
        setMoveButtonsEnabled(false);
        
        addToBattleLog(currentStudent.getName() + " uses " + move.getName() + "!");
        
        int damage = currentStudent.useMove(move, currentPianini);
        addToBattleLog("Dealt " + damage + " damage!");
        
        if (currentPianini.isFainted()) {
            addToBattleLog(currentPianini.getName() + " fainted!");
            handlePianiniSwitch();
        } else {
            // Pianini's turn after a delay
            Timer timer = new Timer(1500, e -> pianiniAttack());
            timer.setRepeats(false);
            timer.start();
        }
        
        updateUI();
    }
    
    /**
     * Handles Pianini's (AI) attack.
     */
    private void pianiniAttack() {
        if (battleEnded) return;
        
        // Simple AI: choose random move
        List<Move> moves = currentPianini.getMoves();
        Move chosenMove = moves.get((int) (Math.random() * moves.size()));
        
        addToBattleLog(currentPianini.getName() + " uses " + chosenMove.getName() + "!");
        
        int damage = currentPianini.useMove(chosenMove, currentStudent);
        addToBattleLog("Your " + currentStudent.getName() + " takes " + damage + " damage!");
        
        if (currentStudent.isFainted()) {
            addToBattleLog(currentStudent.getName() + " fainted!");
            handleStudentSwitch();
        } else {
            // Player's turn
            playerTurn = true;
            setMoveButtonsEnabled(true);
            addToBattleLog("Choose your next move!");
        }
        
        updateUI();
    }
    
    /**
     * Handles switching to next student Pokemon when current one faints.
     */
    private void handleStudentSwitch() {
        studentIndex++;
        if (studentIndex < studentTeam.size()) {
            currentStudent = studentTeam.get(studentIndex);
            addToBattleLog("Go! " + currentStudent.getName() + "!");
            createMoveButtons(); // Update move buttons for new Pokemon
            
            playerTurn = true;
            setMoveButtonsEnabled(true);
        } else {
            // All student Pokemon fainted - battle lost
            endBattle(false);
        }
    }
    
    /**
     * Handles switching to next Pianini Pokemon when current one faints.
     */
    private void handlePianiniSwitch() {
        pianiniIndex++;
        if (pianiniIndex < pianiniTeam.size()) {
            currentPianini = pianiniTeam.get(pianiniIndex);
            addToBattleLog("Professor sends out " + currentPianini.getName() + "!");
            
            // Continue with player's turn
            playerTurn = true;
            setMoveButtonsEnabled(true);
        } else {
            // All Pianini Pokemon fainted - battle won
            endBattle(true);
        }
    }
    
    /**
     * Ends the battle with victory or defeat.
     */
    private void endBattle(boolean victory) {
        battleEnded = true;
        setMoveButtonsEnabled(false);
        
        if (victory) {
            addToBattleLog("");
            addToBattleLog("=== VICTORY! ===");
            addToBattleLog("You defeated Professor Pianini!");
            addToBattleLog("Students are the ultimate champions!");
        } else {
            addToBattleLog("");
            addToBattleLog("=== DEFEAT ===");
            addToBattleLog("Professor Pianini was too strong...");
            addToBattleLog("Better luck next time, young trainer!");
        }
        
        // Close battle after delay
        Timer timer = new Timer(3000, e -> {
            battleFrame.dispose();
            if (callback != null) {
                callback.onComplete(victory, getElapsedSeconds());
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    /**
     * Updates the UI with current Pokemon states.
     */
    private void updateUI() {
        // Update HP displays
        if (studentHpLabel != null && studentHpBar != null) {
            studentHpLabel.setText(currentStudent.getCurrentHp() + "/" + currentStudent.getMaxHp() + " HP");
            studentHpBar.setValue(currentStudent.getCurrentHp());
            studentHpBar.setString(currentStudent.getHpPercentage() + "%");
            updateHpBarColor(studentHpBar, currentStudent);
        }
        
        if (pianiniHpLabel != null && pianiniHpBar != null) {
            pianiniHpLabel.setText(currentPianini.getCurrentHp() + "/" + currentPianini.getMaxHp() + " HP");
            pianiniHpBar.setValue(currentPianini.getCurrentHp());
            pianiniHpBar.setString(currentPianini.getHpPercentage() + "%");
            updateHpBarColor(pianiniHpBar, currentPianini);
        }
        
        // Repaint battle scene
        battleFrame.repaint();
    }
    
    /**
     * Enables or disables move buttons.
     */
    private void setMoveButtonsEnabled(boolean enabled) {
        if (moveButtons != null) {
            for (JButton button : moveButtons) {
                if (button != null) {
                    button.setEnabled(enabled && !battleEnded);
                }
            }
        }
    }
    
    /**
     * Adds a message to the battle log.
     */
    private void addToBattleLog(String message) {
        battleLog.append(message + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
    }
    
    /**
     * Gets elapsed battle time in seconds.
     */
    private int getElapsedSeconds() {
        return (int) ((System.currentTimeMillis() - startTime) / 1000);
    }
}