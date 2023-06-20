package generators;

import structure.Literal;

import java.util.HashMap;

abstract class AbstractGenerator {
    /**
     * Default values
     */
    static final int def_seed = 100, def_num_tree = 20;


    /**
     * environment
     */
    HashMap<String, Literal> environment;

    /**
     * Helper function to find, copy, set and return a literal
     *
     * @param id    The Literal's name as a string
     * @param state The desired state
     * @return The Literal produced
     */
    Literal produceLiteral(String id, boolean state) {
        // Find and copy
        Literal workingLiteral = environment.get(id).clone();
        // Set state
        workingLiteral.setState(state);
        // Return
        return workingLiteral;
    }
}
