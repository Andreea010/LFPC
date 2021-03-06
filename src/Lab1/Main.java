package Lab1;

public class Main {
    public static void main(String[] args) {
        State s = RG.S;
        Input in = new Input("ac");
        for(; !(s instanceof FinalState); s = s.next(in)) {}
        switch((Output)s) {
            case Invalid: System.out.println("Invalid"); break;
            case Valid: System.out.println("Valid"); break;
        }
    }
}