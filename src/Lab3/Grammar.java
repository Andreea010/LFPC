package Lab3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Grammar {

    private static String pathToGrammar = "grammar_v3.txt";


    ArrayList<String[]> rules;

    public Grammar(String grammarFile) {
        this.rules = convertrules(readrules(grammarFile));
    }

    public static ArrayList<String[]> convertrules(ArrayList<String[]> rules) {
        if (rules == null) {
            return null;
        }

        stepOne(rules);
        stepTwo(rules);
        stepThree(rules);
        stepFour(rules);
        stepFive(rules);
        return rules;
    }

    public static void stepFive(ArrayList<String[]> rules) {
        List<String[]> unitProductions = findUnitProductions(rules);

        for (int i = 0; i < unitProductions.size(); i++) {
            String[] production = unitProductions.get(i);
            for (int j = 0; j < unitProductions.size(); j++) {
                String[] tempProduction = unitProductions.get(j);
                if (production[0] == tempProduction[1]
                        && production[1] == tempProduction[0]) {

                    for (int k = 0; k < rules.size(); k++) {
                        String[] rule = rules.get(k);
                        for (int l = 0; l < rule.length; l++) {
                            if (rule[l].equals(production[1])) {
                                rule[l] = production[0];
                            }
                        }
                        rules.set(k, rule);
                    }
                }
            }
        }

        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            if (rule[0].equals(rule[1]) && rule.length == 2) {
                rules.remove(i);
                i--;
            }
        }

        unitProductions = findUnitProductions(rules);
        for (int i = 0; i < unitProductions.size(); i++) {
            String[] production = unitProductions.get(i);
            step5Recursion(rules, production, getIndexInRules(rules, production));
        }
        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            for (int j = 0; j < rules.size(); j++) {
                if (rules.get(j).length == rules.get(i).length) {
                    boolean isEqual = true;

                    for (int k = 0; k < rule.length; k++) {
                        if (!rules.get(j)[k].equals(rule[k])) {
                            isEqual = false;
                        }
                    }
                    if (i != j && isEqual) {
                        rules.remove(j);
                        j--;
                    }
                }
            }
        }
        System.out.println("STEP 5");
        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            for (int j = 0; j < rule.length; j++) {
                System.out.print(rule[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int getIndexInRules(ArrayList<String[]> rules, String[] production) {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i)[0].equals(production[0]) &&
                    rules.get(i)[1].equals(production[1])) {
                return i;
            }
        }
        return -1;
    }

    public static void step5Recursion(ArrayList<String[]> rules,
                                      String[] production, int oldProductionIndex) {
        for (int j = 0; j < rules.size(); j++) {

            if (rules.get(j)[0].equals(production[1])) {
                if (rules.get(j).length == 2
                        && Character.isLowerCase(rules.get(j)[1].charAt(0))) {
                    String[] rule = {production[0], rules.get(j)[1]};
                    rules.add(rule);
                    int ruleCount = 0;
                    int meetedInRight = 0;
                    for (int k = 0; k < rules.size(); k++) {
                        String[] curRule = rules.get(k);
                        if (curRule[0].equals(production[1])) {
                            ruleCount++;
                        }
                        for (int i = 1; i < curRule.length; i++) {
                            if (curRule[i].equals(production[1])) {
                                meetedInRight++;
                            }
                        }
                    }


                    if (ruleCount > 0 && meetedInRight <= 1) {
                        rules.remove(j);
                        j--;
                    }


                } else if (rules.get(j).length == 2
                        && Character.isUpperCase(rules.get(j)[1].charAt(0))) {

                    String[] newProduction = {production[0], rules.get(j)[1]};
                    rules.remove(oldProductionIndex);
                    if (oldProductionIndex < j) {
                        j--;
                    }
                    rules.add(newProduction);
                    int tempIndexOfAddedRule = rules.size() - 1;
                    step5Recursion(rules, newProduction, tempIndexOfAddedRule);
                    if (rules.get(tempIndexOfAddedRule)[0].equals(newProduction[0]) &&
                            rules.get(tempIndexOfAddedRule)[1].equals(newProduction[1])) {
                        rules.remove(tempIndexOfAddedRule);
                    }
                } else if (rules.get(j).length == 3) {

                    String[] rule = {production[0], rules.get(j)[1],
                            rules.get(j)[2]};
                    rules.add(rule);

                }

            }
        }
        if (rules.get(oldProductionIndex)[0].equals(production[0]) &&
                rules.get(oldProductionIndex)[1].equals(production[1])) {
            rules.remove(oldProductionIndex);
        }
    }

    public static ArrayList<String[]> findUnitProductions(
            ArrayList<String[]> rules) {
        ArrayList<String[]> unitProductions = new ArrayList();
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).length == 2
                    && Character.isUpperCase(rules.get(i)[1].charAt(0))) {
                unitProductions.add(rules.get(i));
            }
        }
        return unitProductions;
    }

    public static void stepFour(ArrayList<String[]> rules) {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i)[1].equals("o")) {

                String nullNonTerminal = rules.get(i)[0];
                rules.remove(i);

                removeEpsilon(rules, nullNonTerminal);
            }

        }
        System.out.println("STEP 4");
        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            for (int j = 0; j < rule.length; j++) {
                System.out.print(rule[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void removeEpsilon(ArrayList<String[]> rules,
                                     String nullNonTerminal) {

        for (int j = 0; j < rules.size(); j++) {

            if (rules.get(j)[1].equals(nullNonTerminal)) {
                if (rules.get(j).length == 3) {
                    if (rules.get(j)[2].equals(nullNonTerminal)) {
                        String newNullNonTerminal = rules.get(j)[0];


                        if (!isDoubleNonTerminal(rules, nullNonTerminal)) {
                            rules.remove(j);

                            removeEpsilon(rules, newNullNonTerminal);
                        }
                    } else {

                        String[] newRule = {rules.get(j)[0], rules.get(j)[2]};
                        if (isDoubleNonTerminal(rules, nullNonTerminal)) {
                            rules.add(j, newRule);
                            j++;
                        } else
                            rules.set(j, newRule);
                    }
                } else {

                    String newNullNonTerminal = rules.get(j)[0];

                    if (!isDoubleNonTerminal(rules, nullNonTerminal)) {

                        rules.remove(j);

                        removeEpsilon(rules, newNullNonTerminal);
                    }
                }

            } else if (rules.get(j).length == 3) {

                if (rules.get(j)[2].equals(nullNonTerminal)) {

                    String[] newRule = {rules.get(j)[0], rules.get(j)[1]};
                    if (isDoubleNonTerminal(rules, nullNonTerminal)) {
                        rules.add(j, newRule);

                        j++;
                    } else
                        rules.set(j, newRule);
                }
            }
        }
    }

    public static boolean isDoubleNonTerminal(ArrayList<String[]> rules,
                                              String NonTerminal) {

        int count = 1;
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i)[0].equals(NonTerminal))
                count++;
        }
        if (count > 1)
            return true;
        return false;
    }

    public static void stepThree(ArrayList<String[]> rules) {
        boolean thereIsS = false;
        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            for (int j = 1; j < rule.length; j++) {
                if (rule[j].equals("S")) {
                    thereIsS = true;
                    break;
                }
            }
        }
        if (thereIsS) {
            for (int i = 0; i < rules.size(); i++) {
                String[] rule = rules.get(i);
                for (int j = 0; j < rule.length; j++) {
                    if (rule[j].equals("S")) {
                        rule[j] = "S_0";
                    }
                }
            }
            String[] SigmaRule = {"S", "S_0"};
            rules.add(SigmaRule);
        }
        System.out.println("STEP 3");
        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            for (int j = 0; j < rule.length; j++) {
                System.out.print(rule[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void stepTwo(ArrayList<String[]> rules) {
        int count = 0;

        for (int i = 0; i < rules.size(); i++) {
            while (rules.get(i).length > 3) {

                int n = rules.get(i).length;

                String[] g = new String[3];

                g[0] = "P" + count;
                g[1] = rules.get(i)[n - 2];
                g[2] = rules.get(i)[n - 1];

                rules.add(g);

                String[] h = new String[n - 1];

                for (int j = 0; j < n - 2; j++) {
                    h[j] = rules.get(i)[j];
                }

                h[n - 2] = "P" + count;
                count++;

                rules.remove(i);
                rules.add(h);

            }

        }
        System.out.println("STEP 2");
        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            for (int j = 0; j < rule.length; j++) {
                System.out.print(rule[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void stepOne(ArrayList<String[]> rules) {
        int s = rules.size();

        for (int i = 0; i < s; i++) {
            if (rules.get(i).length > 2) {
                for (int j = 0; j < rules.get(i).length; j++) {

                    char n = rules.get(i)[j].charAt(0);

                    if (Character.isLowerCase(n)) {
                        String[] g = new String[2];

                        g[0] = rules.get(i)[j].toUpperCase() + "_0";// add one
                        // more

                        g[1] = rules.get(i)[j];
                        boolean isAlreadyDefined = false;
                        for (int k = 0; k < rules.size(); k++) {
                            if (g[0].equals(rules.get(k)[0]))
                                isAlreadyDefined = true;
                        }
                        if (!isAlreadyDefined)
                            rules.add(g);

                        rules.get(i)[j] = rules.get(i)[j].toUpperCase()
                                + "_0"; // change the grammar
                    }

                }

            }

        }
        System.out.println("STEP 1");
        for (int i = 0; i < rules.size(); i++) {
            String[] rule = rules.get(i);
            for (int j = 0; j < rule.length; j++) {
                System.out.print(rule[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public int size() {
        return this.rules.size();
    }

    public String[] get(int i) {
        return this.rules.get(i);
    }

    public ArrayList<String[]> getRules() {
        return rules;
    }

    public boolean isRead() {
        if (this.rules != null)
            return true;
        return false;
    }

    public ArrayList<String[]> readrules(String grammarFile) {
        ArrayList<String[]> rules = new ArrayList<String[]>();
        try {
            FileReader fr = new FileReader(grammarFile);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                String[] rule = line.toString().split(" ");
                rules.add(rule);
                line = br.readLine();
            }

        } catch (Exception e) {
            System.err.println("Can't find file " + grammarFile + " for parse grammar!");
            return null;
        }
        return rules;
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            pathToGrammar = args[0];
            System.out.println("You set path to grammar file: " + pathToGrammar + ".");
        }
        System.out.println();
    }
}