package Lab1;

class Input {
    public static final char END = 0;
    private String data;
    private int current;
    public Input(String data) {this.data = data;}
    char read() { return current>= data.length() ? END : data.charAt(current++); }
}
