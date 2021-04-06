package Lab2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class NFA {
    String[] states;
    String[] alphabet;
    String[] acceptStates;
    int start;
    ArrayList<String> transFunction = new ArrayList();
    ArrayList<State> transFStates = new ArrayList();
    Stack endStack = new Stack();
    HashMap<Integer, HashMap<String, ArrayList<Integer>>> hmap = new HashMap();
    HashMap<Integer, HashMap<String, ArrayList<Integer>>> epsilons = new HashMap();
    ArrayList<Integer> qPrime = new ArrayList();
    ArrayList<DFAState> dfaStates = new ArrayList();
    ArrayList<String> finalFunctions = new ArrayList();

    public NFA() {
    }

    public static void main(String[] var0) throws IOException {
        NFA var1 = new NFA();
        Scanner var2 = new Scanner(System.in);
        print("Enter filename: ");
        String var3 = var2.next();
        var1.readFile(var3);
        var1.convertToStates();
        var1.getQPrime();
        var1.acceptableStates();
        var1.fillEmptySets();
        print(var1.hmap);
        var1.getEndStates();
        print(var1.epsilons);
        var1.fillFinalStrings();
        var1.writeFile();
    }

    public void readFile(String var1) {
        try {
            FileReader var2 = new FileReader(var1);
            BufferedReader var3 = new BufferedReader(var2);
            ArrayList var5 = new ArrayList();

            String var4;
            while((var4 = var3.readLine()) != null) {
                var5.add(var4);
            }

            String var6 = ((String)var5.get(0)).toString();
            this.states = var6.replaceAll("\\p{P}", "").split("\\s+");
            var6 = ((String)var5.get(1)).toString();
            this.alphabet = var6.replaceAll("\\p{P}", "").split("\\s+");
            var6 = ((String)var5.get(2)).toString();
            this.start = Integer.parseInt(var6.replaceAll("\\p{P}", ""));
            var6 = ((String)var5.get(3)).toString();
            this.acceptStates = var6.replaceAll("\\p{P}", "").split("\\s+");

            for(int var7 = 4; var7 < var5.size(); ++var7) {
                this.transFunction.add(((String)var5.get(var7)).replaceAll("[{}\\s+]", ""));
            }

            var3.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    public static void print(Object var0) {
        System.out.println(var0);
    }

    public static void printList(String[] var0) {
        String[] var1 = var0;
        int var2 = var0.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            print(var4);
        }

    }

    public static void printArray(ArrayList var0) {
        for(int var1 = 0; var1 < var0.size(); ++var1) {
            System.out.print(var0.get(var1) + " ");
        }

    }

    public void convertToStates() {
        for(int var4 = 0; var4 < this.transFunction.size(); ++var4) {
            String var2 = (String)this.transFunction.get(var4);
            String[] var3 = var2.split("[,=]");
            State var1 = new State(Integer.parseInt(var3[0]), var3[1], Integer.parseInt(var3[2]));
            this.transFStates.add(var1);
        }

    }

    public void getQPrime() {
        this.qPrime.add(this.start);

        for(int var2 = 0; var2 < this.transFStates.size(); ++var2) {
            State var1 = (State)this.transFStates.get(var2);
            if (var1.symbol.equals("EPS")) {
                this.qPrime.add(var1.endState);
            }
        }

    }

    public void acceptableStates() {
        int var4;
        for(var4 = 0; var4 < this.states.length; ++var4) {
            this.hmap.put(Integer.parseInt(this.states[var4]), new HashMap());
        }

        for(var4 = 0; var4 < this.states.length; ++var4) {
            int var2 = Integer.parseInt(this.states[var4]);

            for(int var5 = 0; var5 < this.transFStates.size(); ++var5) {
                State var3 = (State)this.transFStates.get(var5);
                if (var3.current == var2 && !var3.symbol.equals("EPS")) {
                    HashMap var7 = (HashMap)this.hmap.get(var2);
                    ArrayList var6;
                    if (var7.containsKey(var3.symbol)) {
                        var6 = (ArrayList)var7.get(var3.symbol);
                    } else {
                        var6 = new ArrayList();
                    }

                    var6.add(var3.endState);
                    var7.put(var3.symbol, var6);
                    this.hmap.put(var2, var7);
                } else if (var3.current == Integer.parseInt(this.states[var4]) && var3.symbol.equals("EPS")) {
                    HashMap var1;
                    if (this.epsilons.containsKey(var2)) {
                        var1 = (HashMap)this.epsilons.get(var2);
                    } else {
                        var1 = new HashMap();
                    }

                    var1 = this.expandEpsilon(var1, var3);
                    this.epsilons.put(var3.current, var1);
                }
            }
        }

    }

    public HashMap<String, ArrayList<Integer>> expandEpsilon(HashMap<String, ArrayList<Integer>> var1, State var2) {
        for(int var3 = 0; var3 < this.alphabet.length; ++var3) {
            ArrayList var4;
            if (var1.containsKey(this.alphabet[var3])) {
                var4 = (ArrayList)var1.get(this.alphabet[var3]);
                var4.add(var2.endState);
                var1.put(this.alphabet[var3], var4);
            } else {
                var4 = new ArrayList();
                var4.add(var2.endState);
                var1.put(this.alphabet[var3], var4);
            }
        }

        return var1;
    }

    public ArrayList<Integer> getFromHash(int var1, String var2) {
        byte var3 = 0;
        if (var3 < this.states.length) {
            HashMap var4 = (HashMap)this.hmap.get(var1);
            return var4.containsKey(var2) ? (ArrayList)var4.get(var2) : null;
        } else {
            return null;
        }
    }

    public void getEndStates() {
        ArrayList var1;
        DFAState var4;
        int var5;
        label88:
        for(var5 = 0; var5 < this.alphabet.length; ++var5) {
            Stack var6 = new Stack();
            var1 = new ArrayList();

            int var8;
            for(var8 = 0; var8 < this.qPrime.size(); ++var8) {
                ArrayList var7 = new ArrayList();
                var7 = this.combineArrays(this.getFromHash((Integer)this.qPrime.get(var8), this.alphabet[var5]), var7);
                if (var7 != null) {
                    var1 = this.combineArrays(var7, var1);
                }
            }

            var6 = this.pushToStack(var1, var6);

            while(true) {
                do {
                    if (var6.empty()) {
                        var4 = new DFAState(this.qPrime, this.alphabet[var5], var1);
                        this.dfaStates.add(var4);
                        this.endStack.push(var1);
                        continue label88;
                    }

                    var8 = (Integer)var6.pop();
                } while(!this.epsilons.containsKey(var8));

                var1 = this.combineArrays((ArrayList)((HashMap)this.epsilons.get(var8)).get(this.alphabet[var5]), var1);
                Iterator var9 = ((ArrayList)((HashMap)this.epsilons.get(var8)).get(this.alphabet[var5])).iterator();

                while(var9.hasNext()) {
                    int var10 = (Integer)var9.next();
                    var6.push(var10);
                }
            }
        }

        while(true) {
            ArrayList var2;
            do {
                if (this.endStack.empty()) {
                    return;
                }

                var2 = (ArrayList)this.endStack.pop();
                new ArrayList();
            } while(this.containsStarts(var2));

            for(var5 = 0; var5 < this.alphabet.length; ++var5) {
                var1 = new ArrayList();

                for(int var11 = 0; var11 < var2.size(); ++var11) {
                    ArrayList var3 = this.getFromHash((Integer)var2.get(var11), this.alphabet[var5]);
                    if (var3 != null) {
                        var1 = this.combineArrays(var3, var1);

                        for(int var12 = 0; var12 < var3.size(); ++var12) {
                            if (this.epsilons.containsKey(var3.get(var12))) {
                                var1 = this.combineArrays((ArrayList)((HashMap)this.epsilons.get(var3.get(var12))).get(this.alphabet[var5]), var1);
                            }
                        }
                    }
                }

                var4 = new DFAState(var2, this.alphabet[var5], var1);
                this.dfaStates.add(var4);
                this.endStack.push(var1);
            }
        }
    }

    public boolean containsStarts(ArrayList<Integer> var1) {
        for(int var3 = 0; var3 < this.dfaStates.size(); ++var3) {
            DFAState var2 = (DFAState)this.dfaStates.get(var3);
            if (var2.equalToStart(var1)) {
                return true;
            }
        }

        return false;
    }

    public String formatFunctions(ArrayList<Integer> var1, String var2, ArrayList<Integer> var3) {
        String var4 = "{";

        for(int var5 = 0; var5 < var1.size(); ++var5) {
            var4 = var4 + (var5 == var1.size() - 1 ? (Serializable)var1.get(var5) : var1.get(var5) + ", ");
        }

        var4 = var4 + "} , " + var2 + " = ";
        String var7 = "{";

        for(int var6 = 0; var6 < var3.size(); ++var6) {
            var7 = var7 + (var6 == var3.size() - 1 ? (Serializable)var3.get(var6) : var3.get(var6) + ", ");
        }

        if (var7.equals("{")) {
            return var4 + "EM";
        } else {
            return var4 + var7 + "}";
        }
    }

    public void fillEmptySets() {
        for(int var2 = 0; var2 < this.states.length; ++var2) {
            HashMap var1 = (HashMap)this.hmap.get(Integer.parseInt(this.states[var2]));

            for(int var3 = 0; var3 < this.alphabet.length; ++var3) {
                if (!var1.containsKey(this.alphabet[var3])) {
                    var1.put(this.alphabet[var3], (Object)null);
                }
            }

            this.hmap.put(Integer.parseInt(this.states[var2]), var1);
        }

    }

    public void fillFinalStrings() {
        for(int var3 = 0; var3 < this.dfaStates.size(); ++var3) {
            DFAState var1 = (DFAState)this.dfaStates.get(var3);
            String var2 = this.formatFunctions(var1.startStates, var1.symbol, var1.endStates);
            if (!var2.substring(0, 2).equals("{}")) {
                print(var2);
                this.finalFunctions.add(var2);
            }
        }

    }

    public ArrayList<Integer> combineArrays(ArrayList<Integer> var1, ArrayList<Integer> var2) {
        if (var1 == null) {
            return var2;
        } else if (var2 == null) {
            return var1;
        } else {
            for(int var3 = 0; var3 < var1.size(); ++var3) {
                if (!var2.contains(var1.get(var3))) {
                    var2.add(var1.get(var3));
                }
            }

            return var2;
        }
    }

    public Stack<Integer> pushToStack(ArrayList<Integer> var1, Stack<Integer> var2) {
        Iterator var3 = var1.iterator();

        while(var3.hasNext()) {
            int var4 = (Integer)var3.next();
            var2.push(var4);
        }

        return var2;
    }

    public void writeFile() throws IOException {
        File var1 = new File("output.DFA");
        FileOutputStream var2 = new FileOutputStream(var1);
        BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(var2));
        String[] var4 = this.states;
        int var5 = var4.length;

        int var6;
        String var7;
        for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            var3.write(var7 + "\t");
        }

        var3.newLine();
        var4 = this.alphabet;
        var5 = var4.length;

        for(var6 = 0; var6 < var5; ++var6) {
            var7 = var4[var6];
            var3.write(var7 + "\t");
        }

        var3.newLine();
        String var8 = "{";

        for(var5 = 0; var5 < this.qPrime.size(); ++var5) {
            var8 = var8 + (var5 == this.qPrime.size() - 1 ? (Serializable)this.qPrime.get(var5) : this.qPrime.get(var5) + ",");
        }

        var3.write(var8 + "}");
        var3.newLine();
        String var9 = "{";

        for(var6 = 0; var6 < this.acceptStates.length; ++var6) {
            var9 = var9 + (var6 == this.acceptStates.length - 1 ? this.acceptStates[var6] : this.acceptStates[var6] + ",");
        }

        var3.write(var9 + "}");
        var3.newLine();
        Iterator var10 = this.finalFunctions.iterator();

        while(var10.hasNext()) {
            var7 = (String)var10.next();
            var3.write(var7);
            var3.newLine();
        }

        var3.close();
    }
}

