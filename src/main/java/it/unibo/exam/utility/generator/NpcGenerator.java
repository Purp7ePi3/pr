package it.unibo.exam.utility.generator;

import it.unibo.exam.model.entity.Npc;
import it.unibo.exam.utility.geometry.Point2D;

/**
 * NPC generator.
 */
public final class NpcGenerator extends EntityGenerator<Npc> {

    private static final String[] NAMES = {
        "Professor Oak",
        "Professor Elm",
        "Professor Birch",
        "Professor Rowan",
        "Professor Sycamore",
        "Professor Kukui",
        "Professor Magnolia",
        "Professor Cerise",
    };

    private static final String[] DESCRIPTIONS = {
        "A wise and knowledgeable professor.",
        "An expert in Pokémon research.",
        "A friendly and helpful professor.",
        "A professor with a passion for Pokémon.",
        "A professor who loves to teach.",
        "A professor with a deep understanding of Pokémon.",
        "A professor who is always ready to help.",
        "A professor with a wealth of knowledge.",
    };

    private static final String[] DIALOGUES = {
        "Hello, trainer! Are you ready for your adventure?",
        "Welcome to the world of Pokémon!",
        "I have a special task for you, trainer.",
        "Are you interested in learning more about Pokémon?",
        "Let's embark on an exciting journey together!",
        "I have some valuable information for you.",
        "Do you want to hear about my latest research?",
        "I'm here to help you on your journey.",
    };

    /**
     * Constructor for NpcGenerator.
     *
     * @param enviromentSize the size of the environment
     */
    public NpcGenerator(final Point2D enviromentSize) {
        super(enviromentSize);
    }

    /**
     * Generates an NPC based on the given ID.
     *
     * @param id the ID of the NPC to generate
     * @return the generated NPC
     */
    @Override
    public Npc generate(final int id) {
        final String name = NAMES[id];
        final String description = DESCRIPTIONS[id];
        final String dialogue = DIALOGUES[id];
        return new Npc(super.getEnv(), name, description, dialogue);
    }
}
