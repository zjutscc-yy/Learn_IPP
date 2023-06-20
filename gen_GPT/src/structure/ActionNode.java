package structure;

import java.util.ArrayList;

//动作节点
public class ActionNode extends TreeNode {
    // Action -> ActionName {pre-conditions}{post-conditions}

    /**
     * 前置条件
     */
    final private ArrayList<Literal> prec;

    /**
     * 后置条件
     */
    final private ArrayList<Literal> postc;

    /**
     * constructor for dummy action
     *
     * @param name name of the action
     */
    public ActionNode(String name) {
        super(name);
        this.prec = new ArrayList<>();
        this.postc = new ArrayList<>();
    }

    /**
     * constructor
     *
     * @param name          name of the action
     * @param precondition  precondition of the action
     * @param postcondition postcondition of the action
     */
    public ActionNode(String name, ArrayList<Literal> precondition, ArrayList<Literal> postcondition) {
        super(name);
        this.prec = precondition;
        this.postc = postcondition;
    }

    /**
     * method to return the precondition of this action
     */
    public ArrayList<Literal> getPreC() {
        return this.prec;
    }

    /**
     * method to return the postcondition of this action
     */
    public ArrayList<Literal> getPostC() {
        return this.postc;
    }

}
