package generators;

import structure.GoalNode;
import structure.Literal;

import java.util.HashMap;

public interface GPTGenerator {
    HashMap<String, Literal> genEnvironment();

    GoalNode genTopLevelGoal(int index);
}
