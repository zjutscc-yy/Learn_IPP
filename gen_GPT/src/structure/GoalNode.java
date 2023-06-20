package structure;

import java.util.ArrayList;

public class GoalNode extends TreeNode {
    // Goal -> GoalName {Plans}{Goal-Condition}
    /**
     * 实现此目标的计划
     */
    final private ArrayList<PlanNode> plans;

    /**
     * goalConds-condition
     */
    final private ArrayList<Literal> goalConds;

    /**
     * constructor for empty goal
     *
     * @param name
     */
    public GoalNode(String name) {
        super(name);
        this.plans = new ArrayList<>();
        this.goalConds = new ArrayList<>();
    }

    /**
     * constructor
     *
     * @param name      the name of the goal
     * @param plan      the set of associated plans
     * @param goalConds the goal-condition
     */
    public GoalNode(String name, ArrayList<PlanNode> plan, ArrayList<Literal> goalConds) {
        super(name);
        this.plans = plan;
        this.goalConds = goalConds;
    }

    /**
     * method to return the plans to achieve this goalConds
     */
    public ArrayList<PlanNode> getPlans() {
        return this.plans;
    }

    /**
     * method to return the plans to achieve this goalConds
     */
    public ArrayList<Literal> getGoalConds() {
        return this.goalConds;
    }
}
