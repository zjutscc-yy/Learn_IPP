package generators;

import structure.GoalNode;
import structure.Literal;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    /**
     * The main function, takes a set of commandline arguments
     */
    public static void main(String[] args)
    {

        // Shared parameters with their default values
        int seed = AbstractGenerator.def_seed, num_tree = AbstractGenerator.def_num_tree;
        // default path
        String path ="gpt.xml";

        //Synth parameters with their default values
        int sy_depth = SynthGenerator.def_depth,	// the maximum depth
                sy_num_goal = SynthGenerator.def_num_goal, // the number of subgoals per plan
                sy_num_plan = SynthGenerator.def_num_plan, // the number of plans for each goal
                sy_num_action = SynthGenerator.def_num_action, // the number of actions in each plan
                sy_num_var = SynthGenerator.def_num_var, // the total number of environmental variables
                sy_num_lit = SynthGenerator.def_num_literal, // the number of literals appear in actions' pre- and post-condition
                sy_num_selected = SynthGenerator.def_num_selected; // the number of variables selected for each gpt

        double sy_prob_lplan = SynthGenerator.def_lplan; // the probability of a plan being leave plan

        GPTGenerator gen;

        if (args.length == 0){
            String help = "\n"+
                    "HELP:\n" +
                    "synth\n The pure synthetic generator\n";
            System.out.println(help);
            return;
        }


        switch(args[0]){
            case "synth": // Synthetic Generator
            {
                String help = "\n" +
                        "HELP:\n" +
                        "-s\n Random seed. If the value is not specified, 100 is default \n" +
                        "-d\n Maximum depth of the goal-plan tree. If the value is not specified, 3 is default.\n" +
                        "-g\n Number of subgoals in each plan (except the leaf plan). If the value is not specified, 1 is default.\n" +
                        "-p\n Number of plans to achieve each goal. If the value is not specified, 2 is default.\n" +
                        "-a\n Number of actions in each plan. If the value is not specified, 1 is default.\n" +
                        "-v\n Number of environment variables. If the value is not specified, 60 is default.\n" +
                        "-t\n Number of goal-plan trees. If the value is not specified, 1 is default.\n" +
                        "-e\n Number of selected literals. If the value is not specified, 30 is default.\n" +
                        "-l\n Number of literals per action. If the value is not specified, 1 is default.\n" +
                        "-x\n Probability of a plan being leave plan. If the value is not specified, 0 is default\n" +
                        "-f\n The output file path to which the set of goal-plan tree is saved. If the value is not specified, gpt.xml is default.\n";

                // parser for the input parameters
                int i = 1;
                String arg;
                // check each flag
                while(i < args.length && args[i].startsWith("-"))
                {
                    arg = args[i++];
                    // parser to find out the flags
                    if(arg.length() != 2)
                    {
                        System.out.println(arg + " is not a valid flag");
                        System.out.println(help);
                        System.exit(1);
                    }
                    // get the flag value
                    char flag = arg.charAt(1);
                    switch (flag)
                    {
                        case 'h': // help
                            System.out.println(help);
                            System.exit(0);

                        case 's': // seed
                            try
                            {
                                seed = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("Seed must be an integer");
                                System.exit(1);
                            }
                        case 'd': // depth
                            try
                            {
                                sy_depth = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("Depth must be an integer");
                                System.exit(1);
                            }
                        case 'g': // number of goals
                            try
                            {
                                sy_num_goal = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("The number of goals in each plan must be an integer");
                                System.exit(1);
                            }
                        case 'p': // number of plans
                            try
                            {
                                sy_num_plan = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("The number of plans to achieve each goal must be an integer");
                                System.exit(1);
                            }
                        case 'a': // number of actions
                            try
                            {
                                sy_num_action = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("The number of actions in each plan must be an integer");
                                System.exit(1);
                            }
                        case 'v': // number of variables
                            try
                            {
                                sy_num_var = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("The number of environment variables must be an integer");
                                System.exit(1);
                            }
                        case 'e': // selected number of variables
                            try
                            {
                                sy_num_selected = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("The number of selected environment variables must be an integer");
                                System.exit(1);
                            }
                        case 'l': // number of literal per action
                            try
                            {
                                sy_num_lit = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("The number literals per action must be an integer");
                                System.exit(1);
                            }
                        case 't': // number of trees
                            try
                            {
                                num_tree = Integer.parseInt(args[i++]);break;
                            }catch(Exception e)
                            {
                                System.out.println("The number of goal-plan tree must be an integer");
                                System.exit(1);
                            }
                        case 'x': // probability of a plan being leave plan
                            try{
                                sy_prob_lplan = Double.parseDouble(args[i++]);
                                if (sy_prob_lplan < 0 || sy_prob_lplan > 1){
                                    throw (new IllegalArgumentException());
                                }
                                break;
                            }catch (Exception e){

                            }
                        case 'f': // path
                            path = args[i++];break;

                        default:
                            System.out.println(arg + " is not a valid flag");
                            System.out.println(help);
                            System.exit(1);
                    }
                }


                // check the value of the input arguments
                if(sy_depth <= 0)
                {
                    System.out.println("Depth must be greater than 0");
                    System.exit(1);
                }
                if(sy_num_goal <= 0)
                {
                    System.out.println("Maximum number of goals must be greater than 0");
                    System.exit(1);
                }
                if(sy_num_plan <= 0)
                {
                    System.out.println("Maximum number of plans must be greater than 0");
                    System.exit(1);
                }
                if(sy_num_action < 0)
                {
                    System.out.println("Maximum number of actions must be greater than 0");
                    System.exit(1);
                }
                if(sy_num_var <= 0)
                {
                    System.out.println("Total number of variables must be greater than 0");
                    System.exit(1);
                }
                // need to find more
                if(sy_num_selected <= 0)
                {
                    System.out.println("The number of selected variables must be greater than 0");
                    System.exit(1);
                }
                if(sy_num_selected > sy_num_var)
                {
                    System.out.println("The number of selected variables must be less than or equal to the total number of variables");
                    System.exit(1);
                }
                if(sy_num_lit <= 0){
                    System.out.println("The number of literals per action must be greather than 0");
                    System.exit(1);
                }
                if(num_tree <= 0)
                {
                    System.out.println("Total number of goal-plan tree must be greater than 0");
                    System.exit(1);
                }
                if(sy_prob_lplan < 0 || sy_prob_lplan > 1){
                    System.out.println("probability of a plan being leave plan must be between 0 and 1");
                    System.exit(1);
                }

                System.out.println("seed: " + seed);
                System.out.println("depth: " + sy_depth);
                System.out.println("tree: " + num_tree);
                System.out.println("goals: " + sy_num_goal);
                System.out.println("plans: " + sy_num_plan);
                System.out.println("actions: " + sy_num_action);
                System.out.println("var: " + sy_num_var);
                System.out.println("selected: " + sy_num_selected);
                System.out.println("literals: " + sy_num_lit);
                System.out.println("lplan: " + sy_prob_lplan);

                gen = new SynthGenerator(seed, sy_depth, num_tree, sy_num_goal, sy_num_plan, sy_num_action,
                        sy_num_var, sy_num_selected, sy_num_lit, sy_prob_lplan);

                break;
            }

            default:
                String help = "\n"+
                        "HELP:\n" +
                        "synth\n The pure synthetic generator\n";
                System.out.println(help);
                return;
        }

        // generate the environment
        HashMap<String, Literal> environment = gen.genEnvironment();
        // generate the tree
        ArrayList<GoalNode> goalForests = new ArrayList<>();

        System.out.println(num_tree);
        for (int k = 0; k < num_tree; k++)
        {
            goalForests.add(gen.genTopLevelGoal(k));
        }

        // write the set of goal plan tree to an XML file
        XMLWriter wxf = new XMLWriter();
        wxf.CreateXML(environment, goalForests, path);
    }


}
