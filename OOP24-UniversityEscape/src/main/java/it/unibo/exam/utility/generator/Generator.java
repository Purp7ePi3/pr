package it.unibo.exam.utility.generator;

/**
 * Simple Generator class.
 * @param <E> 
 */
public interface Generator<E> {

    /**
     * @param id
     * @return E based on an id
     */
    E generate(int id);

}
