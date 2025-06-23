package it.unibo.exam.model.entity.pokemon;

import java.util.List;

/**
 * Factory class for creating predefined Pokemon for the battle system.
 * Contains the Students vs Pianini Pokemon definitions.
 */
public class PokemonFactory {
    
    /**
     * Creates the Student team Pokemon.
     * 
     * @return list of student Pokemon
     */
    public static List<Pokemon> createStudentTeam() {
        return List.of(
            createStudentePrimoPoke(),
            createStudenteSeconddoPoke(),
            createStudenteTerzoPoke()
        );
    }
    
    /**
     * Creates the Pianini (Professor) team Pokemon.
     * 
     * @return list of professor Pokemon
     */
    public static List<Pokemon> createPianiniTeam() {
        return List.of(
            createProfessorePrimoPoke(),
            createProfessoreSecondoPoke(),
            createProfessoreTerzoPoke()
        );
    }
    
    /**
     * Creates the first student Pokemon.
     */
    private static Pokemon createStudentePrimoPoke() {
        List<Move> moves = List.of(
            new Move("Cramming", 25, "Intense last-minute studying!", Move.MoveType.PHYSICAL),
            new Move("Coffee Rush", 20, "Caffeine-powered energy burst!", Move.MoveType.SPECIAL),
            new Move("Group Study", 15, "Collaborative learning attack!", Move.MoveType.PHYSICAL),
            new Move("All-Nighter", 35, "Sleep-deprived desperation move!", Move.MoveType.SPECIAL)
        );
        
        return new Pokemon(
            "Studente Informatico",
            120,  // HP
            85,   // Attack
            70,   // Defense
            moves,
            "A dedicated computer science student armed with keyboards and determination!"
        );
    }
    
    /**
     * Creates the second student Pokemon.
     */
    private static Pokemon createStudenteSeconddoPoke() {
        List<Move> moves = List.of(
            new Move("Code Debug", 30, "Systematic error elimination!", Move.MoveType.PHYSICAL),
            new Move("Stack Overflow", 40, "Infinite recursion attack!", Move.MoveType.SPECIAL),
            new Move("Git Commit", 25, "Version control discipline!", Move.MoveType.PHYSICAL),
            new Move("Compiler Error", 20, "Confusing syntax mistake!", Move.MoveType.STATUS)
        );
        
        return new Pokemon(
            "Studente Magistrale",
            140,  // HP
            90,   // Attack
            85,   // Defense
            moves,
            "An advanced student with years of academic battle experience!"
        );
    }
    
    /**
     * Creates the third student Pokemon.
     */
    private static Pokemon createStudenteTerzoPoke() {
        List<Move> moves = List.of(
            new Move("Thesis Defense", 45, "Ultimate academic presentation!", Move.MoveType.SPECIAL),
            new Move("Research Paper", 35, "Peer-reviewed knowledge bomb!", Move.MoveType.PHYSICAL),
            new Move("Internship", 25, "Real-world experience!", Move.MoveType.PHYSICAL),
            new Move("Graduation", 50, "Final academic achievement!", Move.MoveType.SPECIAL)
        );
        
        return new Pokemon(
            "Studente Laureando",
            160,  // HP
            95,   // Attack
            90,   // Defense
            moves,
            "A final-year student ready to conquer any academic challenge!"
        );
    }
    
    /**
     * Creates the first professor Pokemon.
     */
    private static Pokemon createProfessorePrimoPoke() {
        List<Move> moves = List.of(
            new Move("Pop Quiz", 30, "Surprise examination attack!", Move.MoveType.SPECIAL),
            new Move("Homework Bomb", 25, "Overwhelming assignment load!", Move.MoveType.PHYSICAL),
            new Move("Lecture Drone", 20, "Monotone information overload!", Move.MoveType.STATUS),
            new Move("Attendance Check", 35, "Mandatory presence verification!", Move.MoveType.PHYSICAL)
        );
        
        return new Pokemon(
            "Professor Pianini Jr.",
            130,  // HP
            80,   // Attack
            95,   // Defense
            moves,
            "A young but strict professor with traditional teaching methods!"
        );
    }
    
    /**
     * Creates the second professor Pokemon.
     */
    private static Pokemon createProfessoreSecondoPoke() {
        List<Move> moves = List.of(
            new Move("Theoretical Proof", 40, "Complex mathematical demonstration!", Move.MoveType.SPECIAL),
            new Move("Academic Bureaucracy", 30, "Paperwork paralysis!", Move.MoveType.STATUS),
            new Move("Peer Review", 35, "Critical academic evaluation!", Move.MoveType.PHYSICAL),
            new Move("Conference Paper", 45, "Published research impact!", Move.MoveType.SPECIAL)
        );
        
        return new Pokemon(
            "Professor Pianini Sr.",
            150,  // HP
            100,  // Attack
            100,  // Defense
            moves,
            "A seasoned academic veteran with decades of teaching experience!"
        );
    }
    
    /**
     * Creates the third professor Pokemon.
     */
    private static Pokemon createProfessoreTerzoPoke() {
        List<Move> moves = List.of(
            new Move("Final Exam", 55, "Ultimate academic challenge!", Move.MoveType.SPECIAL),
            new Move("Grade Curve", 40, "Statistical grade adjustment!", Move.MoveType.PHYSICAL),
            new Move("Department Meeting", 30, "Administrative authority!", Move.MoveType.STATUS),
            new Move("Tenure Track", 60, "Lifetime academic security!", Move.MoveType.SPECIAL)
        );
        
        return new Pokemon(
            "Professor Pianini Master",
            180,  // HP
            110,  // Attack
            120,  // Defense
            moves,
            "The ultimate academic boss - a legendary professor with unlimited power!"
        );
    }
}