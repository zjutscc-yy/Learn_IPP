package generators;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import structure.ActionNode;
import structure.GoalNode;
import structure.Literal;
import structure.PlanNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class XMLWriter {
    public XMLWriter()
    {
    }

    /**
     * Write the rich environment and GPT forest
     * @param environment The full passed environment
     * @param goalForests The GPT forest
     * @param path The file to write to
     */
    public void CreateXML(HashMap<String, Literal> environment, ArrayList<GoalNode> goalForests, String path) {
        try
        {
            Element forest = new Element("Forest");
            Document document = new Document(forest);
            // Small, short forest of variables
            Element environmentElement = new Element("Environment");
            // Write all the variables to this element
            for (Literal envVar: environment.values()) {
                writeEnvVar(envVar, environmentElement);
            }
            // Add the environment element to the XML
            forest.addContent(environmentElement);
            // write each top-level goals
            for (GoalNode gl : goalForests) {
                writeGoal(gl, forest);
            }
            XMLOutputter xmlOutputer = new XMLOutputter();
            xmlOutputer.setFormat(Format.getPrettyFormat());
            xmlOutputer.output(document, new FileWriter(path));
            System.out.println("XML File was created successfully!");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * write a literal
     * @param envVar The Literal to be written
     * @param parent The Parent
     */
    private void writeEnvVar(Literal envVar, Element parent){
        Element var = new Element("Literal");
        var.setAttribute("name", envVar.getId());
        var.setAttribute("stochastic", Boolean.toString(envVar.isStochastic()));
        String initVal = envVar.isRandomInit() ? "random" : Boolean.toString(envVar.getState());
        var.setAttribute("initVal", initVal);
        //var.setAttribute("prob", Double.toString(envVar.getProbability()));
        parent.addContent(var);
    }

    /**
     * write a plan
     * @param pl The target plan
     * @param parent The goal to achieve
     */
    private void writePlan(PlanNode pl, Element parent)
    {
        Element plan = new Element("Plan");
        plan.setAttribute(new Attribute("name", pl.getName()));

        // precondition
        ArrayList<Literal> st = pl.getPre();

        if(st != null && st.size() > 0){
            StringBuilder pre = new StringBuilder();
            for(int i = 0 ; i < st.size(); i++)
            {
                if (i > 0){
                    pre.append(", ");
                }
                pre.append(st.get(i).toSimpleString());
            }
            pre.append(";");
            plan.setAttribute(new Attribute("precondition", pre.toString()));
        }

        // post-condition
        st = pl.getPost();

        if(st != null && st.size() > 0){
            StringBuilder post = new StringBuilder();
            for(int i = 0 ; i < st.size(); i++)
            {
                if (i > 0){
                    post.append(", ");
                }
                post.append(st.get(i).toSimpleString());
            }
            post.append(";");
            plan.setAttribute(new Attribute("postcondition", post.toString()));
        }



        // write all actions, subgoals and parallel compositions it contains
        for(int i = 0; i < pl.getPlanBody().size(); i++)
        {
            if(pl.getPlanBody().get(i) instanceof ActionNode)
            {
                ActionNode act = (ActionNode) pl.getPlanBody().get(i);
                writeAction(act, plan);
            }
            if(pl.getPlanBody().get(i) instanceof GoalNode)
            {
                GoalNode gl = (GoalNode) pl.getPlanBody().get(i);
                writeGoal(gl, plan);
            }
        }
        parent.addContent(plan);
    }

    /**
     * write action
     * @param act The target action
     * @param parent The plan which contain this action
     */
    private void writeAction(ActionNode act, Element parent)
    {
        Element action = new Element("Action");
        action.setAttribute(new Attribute("name", act.getName()));
        // pre-condition
        ArrayList<Literal> st = act.getPreC();
        if(st != null && st.size() > 0){
            StringBuilder pre = new StringBuilder();
            for(int i = 0 ; i < st.size(); i++)
            {
                if (i > 0){
                    pre.append(", ");
                }
                pre.append(st.get(i).toSimpleString());
            }
            pre.append(";");
            action.setAttribute(new Attribute("precondition", pre.toString()));
        }

        // postcondition
        st = act.getPostC();
        if(st != null && st.size() > 0){
            StringBuilder post = new StringBuilder();
            for(int i = 0 ; i < st.size(); i++)
            {
                if (i > 0){
                    post.append(", ");
                }
                post.append(st.get(i).toSimpleString());
            }
            post.append(";");
            action.setAttribute(new Attribute("postcondition", post.toString()));
        }
        parent.addContent(action);
    }

    /**
     * write goal
     * @param gl The target goal
     * @param parent the Plan which contain this goal
     */
    private void writeGoal(GoalNode gl, Element parent)
    {
        Element goal = new Element("Goal");
        goal.setAttribute(new Attribute("name", gl.getName()));

        // goal-condition
        ArrayList<Literal> st = gl.getGoalConds();
        if(st != null && st.size() > 0){
            StringBuilder goalCond = new StringBuilder();
            for(int i = 0 ; i < st.size(); i++)
            {
                if (i > 0){
                    goalCond.append(", ");
                }
                goalCond.append(st.get(i).toSimpleString());
            }
            goalCond.append(";");
            goal.setAttribute(new Attribute("goal-condition", goalCond.toString()));
        }

        for(int i = 0; i < gl.getPlans().size(); i++)
        {
            PlanNode pl = gl.getPlans().get(i);
            writePlan(pl, goal);
        }
        parent.addContent(goal);
    }

}
