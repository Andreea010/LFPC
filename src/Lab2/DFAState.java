package Lab2;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;

public class DFAState {
    ArrayList<Integer> startStates;
    ArrayList<Integer> endStates;
    String symbol;

    DFAState(ArrayList<Integer> var1, String var2, ArrayList<Integer> var3) {
        this.startStates = var1;
        this.symbol = var2;
        this.endStates = var3;
    }

    public boolean equalToStart(ArrayList<Integer> var1) {
        int var2 = 0;
        if (var1.size() != this.startStates.size()) {
            return false;
        } else {
            for(int var3 = 0; var3 < var1.size(); ++var3) {
                if (this.startStates.contains(var1.get(var3))) {
                    ++var2;
                }
            }

            return var2 == var1.size();
        }
    }
}


