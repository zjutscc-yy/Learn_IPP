package structure;

import java.util.ArrayList;

public class PlanNode extends TreeNode {
    // Plan -> PlanName {Prec}{Postc}{PlanStep ; ... ; PlanStep}
    /**
     * planbody
     */
    final private ArrayList<TreeNode> pb;

    /**
     * precondition
     */
    final private ArrayList<Literal> pre;

    /**
     * post-condition
     */
    final private ArrayList<Literal> post;


    public PlanNode(String name) {
        super(name);
        this.pb = new ArrayList<>();
        this.pre = new ArrayList<>();
        this.post = new ArrayList<>();
    }

    public PlanNode(String name, ArrayList<TreeNode> planbody, ArrayList<Literal> precondition, ArrayList<Literal> postcondition) {
        super(name);
        this.pb = planbody;
        this.pre = precondition;
        this.post = postcondition;
    }


    /**
     * method to return the precondition of this plan
     */
    public ArrayList<Literal> getPre() {
        return this.pre;
    }

    /**
     * method to return the post-condiiton of this plan
     */
    public ArrayList<Literal> getPost() {
        return this.post;
    }

    /**
     * method to return its planbody
     */
    public ArrayList<TreeNode> getPlanBody() {
        return this.pb;
    }
}
