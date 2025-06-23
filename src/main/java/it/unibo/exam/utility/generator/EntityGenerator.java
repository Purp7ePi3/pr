package it.unibo.exam.utility.generator;

import it.unibo.exam.utility.geometry.Point2D;

/**
 * Abstract class for an Entity Generator.
 * @param <E> Item tipe to generate
 */
public abstract class EntityGenerator<E> implements  Generator<E> {
    private final Point2D enviromentSize;

    /**
     * @param enviromentSize
     */
    public EntityGenerator(final Point2D enviromentSize) {
        this.enviromentSize = new Point2D(enviromentSize);
    }

    /**
     * @return enviromentSize
     */
    protected Point2D getEnv() {
        return new Point2D(enviromentSize);
    }

    /**
     * @param id of the enity
     * @return Entity e
    */
    @Override
    public abstract E generate(int id);
}
