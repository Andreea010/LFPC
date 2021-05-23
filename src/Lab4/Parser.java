package Lab4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Parser {
    public static int count = 0;
    private static ArrayList<String> rules = new ArrayList<String>();
    private static ArrayList<Integer> lasttermspecialRule = new ArrayList<Integer>();
    private static ArrayList<Integer> firsttermspecialRule = new ArrayList<Integer>();
    private static ArrayList<String> parts = new ArrayList<String>();
    private static ArrayList<String> rightparts = new ArrayList<String>();
    private static ArrayList<String> terminals = new ArrayList<String>();
    private static ArrayList<Integer> indexes = new ArrayList<Integer>();
    private static ArrayList<String> inputs = new ArrayList<String>();
    private static ArrayList<String>[] parse = new ArrayList[3];
    private static ArrayList<String>[] firstterms;
    public static ArrayList<String>[] lastterms ;
    public static String [][]parseTable;

    public static void main(String[] args) throws IOException {

        System.out.println("Enter the input for parsing with $ at the end: ");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();


        rules.add("S ::- A;");
        rules.add("A ::- cB;");
        rules.add("B ::- Cd;");
        rules.add("C ::- D;");
        rules.add("C ::- CbD;");
        rules.add("D ::- a;");
        rules.add("D ::- acCd;");
        int n = rules.size();
        StringBuilder sb= new StringBuilder();
        for(int i=0; i<n; i++) sb.append(rules.get(i));
        String grammar = sb.toString();
        for(int i=0; i<grammar.length(); i++){
            if(lowercase(grammar.charAt(i)) && nonterminal(grammar.charAt(i)))
                terminals.add(Character.toString(grammar.charAt(i)));
        }
        Set<String> set = new HashSet<>(terminals);
        terminals.clear();
        terminals.addAll(set);
        parseTable = new String[terminals.size()+1][terminals.size()+1];

        for(int i=0; i<n; i++){
            rightparts.addAll(partFinder(rules.get(i)));
        }
        int size = rightparts.size();

        for(int i=0; i<size; i++){
            String str = rightparts.get(i);
            char []strchar = str.toCharArray();
            for(int j=0; j<str.length(); j++){
                if(uppercase(str.charAt(j))){
                    strchar[j]='N';
                }
            }
            str = String.copyValueOf(strchar);
            rightparts.set(i, str);

        }
        set = new HashSet<>(rightparts);
        rightparts.clear();
        rightparts.addAll(set);

        for(int i=0; i<terminals.size()+1; i++){
            for(int j=0; j<terminals.size()+1; j++){
                parseTable[i][j]= "e";
            }
        }

        int k=1;
        for(int i=0; i<terminals.size();i++){
            parseTable[k][0] = terminals.get(i);
            parseTable[0][k] = terminals.get(i);
            k++;
        }
        parseTable[0][0]=" ";





        lastterms = new ArrayList[n];
        firstterms = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            lastterms[i] = new ArrayList<String>();
        }
        for (int i = 0; i < n; i++) {
            firstterms[i] = new ArrayList<String>();
        }
        for (int i = 0; i < 3; i++) {
            parse[i] = new ArrayList<String>();
        }


        count =0;
        for (int i = 0; i < n; i++) {
            lasttermFinder(rules.get(i));
        }

        for (int i = 0; i < n; i++)
            completeLastterm(lasttermspecialRule);




        count =0;
        for (int i = 0; i < n; i++) {
            firsttermFinder(rules.get(i));
        }

        for (int i = 0; i < n; i++)
            completeFirstterm(firsttermspecialRule);

        parseTableGenerator(rules);






        for(int i=0; i<terminals.size()+1; i++){
            for(int j=0; j<terminals.size()+1; j++){
                System.out.print(parseTable[i][j]+"   ");
            }
            System.out.println();
        }

        parse[0].add("$");
        parse[1].add(input);
        parser(input);


        System.out.println("Stack\t\t"+"Input\t\t"+"Action");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i <parse[0].size()-1; i++) { //printing parse
            System.out.println(parse[0].get(i)+"\t\t"+ parse[1].get(i)+"\t\t"+ parse[2].get(i));
        }

    }


    public static int ruleFinder(char a){
        for(int i=0; i<rules.size(); i++){
            if(rules.get(i).charAt(0)==a)
                return i;
        }
        return 0;
    }


    public static void insertParseTable(String s1, String s2, String str){
        int row = 0 ,column = 0;
        for(int i=0; i<terminals.size()+1; i++){
            if(parseTable[i][0].equals(s1)) row=i;
            if(parseTable[0][i].equals(s2)) column=i;
        }
        parseTable[row][column] = str;
    }


    public static boolean nonterminal(char a){
        if(a==';' || a==':' ||a=='-' ||a=='|' ||a==' ') return false;
        return true;
    }


    public static boolean lowercase(char a){
        if(a<65 || a>90) return true;
        else return false;
    }


    public static boolean uppercase(char a){
        if(a>=65 && a<=90) return true;
        else return false;
    }


    public static ArrayList<String> partFinder(String rule){
        parts.clear();
        int pos = 0;
        for(int i=0; i<rule.length(); i++){
            if(rule.charAt(i)=='-' || rule.charAt(i)=='|' ||rule.charAt(i)==';'){
                parts.add(rule.substring(pos+1,i).trim());
                pos = i;
            }
        }
        parts.remove(0);

        return parts;
    }

    public static void firsttermFinder(String rule){

        partFinder(rule);
        for(int i=0; i<parts.size();i++){
            if(parts.get(i).length()==1){
                if(lowercase(parts.get(i).charAt(0)))
                    firstterms[count].add(parts.get(i));
                else if(uppercase(parts.get(i).charAt(0)))
                    firsttermspecialRule.add(count);
            }
            else{
                String str = parts.get(i);
                if(lowercase(str.charAt(0))) {
                    firstterms[count].add(Character.toString(str.charAt(0)));
                } else if(uppercase(str.charAt(0))){
                    if(lowercase(str.charAt(1))) {
                        firstterms[count].add(Character.toString(str.charAt(1)));
                    }
                }

            }
        }
        count ++;
    }

    public static void completeFirstterm(ArrayList specialRule){
        for(int i=0; i<specialRule.size();i++){
            int num = (int)specialRule.get(i);
            ArrayList <String>parts = partFinder(rules.get((int)specialRule.get(i)));
            for (int j=0; j<parts.size();j++){
                if(parts.get(j).length()==1 && uppercase(parts.get(j).charAt(0))){
                    char key = parts.get(j).charAt(0);
                    for (int k=0; k<rules.size();k++){
                        if(rules.get(k).charAt(0)==key){

                            for(int m=0; m<firstterms[k].size(); m++){
                                if(!firstterms[num].contains(firstterms[k].get(m)))
                                    firstterms[num].add(firstterms[k].get(m));
                            }

                        }
                    }
                }
            }
        }
    }


    public static void lasttermFinder(String rule){

        partFinder(rule);
        for(int i=0; i<parts.size();i++){
            if(parts.get(i).length()==1){
                if(lowercase(parts.get(i).charAt(0)))
                    lastterms[count].add(parts.get(i));
                else if(uppercase(parts.get(i).charAt(0)))
                    lasttermspecialRule.add(count);
            }
            else{
                String str = parts.get(i);
                if(lowercase(str.charAt(str.length()-1))) {
                    lastterms[count].add(Character.toString(str.charAt(str.length()-1)));
                } else if(uppercase(str.charAt(str.length()-1))){
                    if(lowercase(str.charAt(str.length()-2))) {
                        lastterms[count].add(Character.toString(str.charAt(str.length()-2)));
                    }
                }

            }
        }
        count ++;
    }

    public static void completeLastterm(ArrayList specialRule){
        for(int i=0; i<specialRule.size();i++){
            int num = (int)specialRule.get(i);
            ArrayList <String>parts = partFinder(rules.get((int)specialRule.get(i)));
            for (int j=0; j<parts.size();j++){
                if(parts.get(j).length()==1 && uppercase(parts.get(j).charAt(0))){
                    char key = parts.get(j).charAt(0);
                    for (int k=0; k<rules.size();k++){
                        if(rules.get(k).charAt(0)==key){

                            for(int m=0; m<lastterms[k].size(); m++){
                                if(!lastterms[num].contains(lastterms[k].get(m)))
                                    lastterms[num].add(lastterms[k].get(m));
                            }

                        }
                    }
                }
            }
        }
    }

    public static void parseTableGenerator(ArrayList<String> grammar){
        for(int i=0; i<grammar.size(); i++){
            ArrayList <String>parts = partFinder(grammar.get(i));
            for(int j=0; j<parts.size(); j++){
                String part = parts.get(j);
                if(part.length()>1){
                    for(int k=0; k<part.length()-1; k++){
                        String substr = part.substring(k, k+2);
                        if(lowercase(substr.charAt(0)) && lowercase(substr.charAt(1))){
                            insertParseTable(Character.toString(substr.charAt(0)),
                                    Character.toString(substr.charAt(1)), "=");
                        }else if(lowercase(substr.charAt(0)) && uppercase(substr.charAt(1))){
                            int var = ruleFinder(substr.charAt(1));
                            for(int m=0; m<firstterms[var].size(); m++){
                                insertParseTable(Character.toString(substr.charAt(0)), firstterms[var].get(m),"<");
                            }
                        }
                        else if(uppercase(substr.charAt(0)) && lowercase(substr.charAt(1))){
                            int var = ruleFinder(substr.charAt(0));
                            for(int m=0; m<lastterms[var].size(); m++){
                                insertParseTable(lastterms[var].get(m), Character.toString(substr.charAt(1)), ">");
                            }
                        }
                        if(part.length() >= 3){
                            for(int n=0; n<part.length()-2; n++){
                                String sub = part.substring(n,n+3);
                                if(lowercase(sub.charAt(0)) && uppercase(sub.charAt(1)) && lowercase(sub.charAt(2))){
                                    insertParseTable(Character.toString(sub.charAt(0)),
                                            Character.toString(sub.charAt(2)), "=");
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    public static void SyntaxError(){
        System.out.println("The parser encountered a problem while parsing the input string.");
    }


    public static void success(){
        System.out.println("String was parsed successfully!");
    }


    public static String fetch(char a, char b){
        int row = 0 ,column = 0;
        String s1 = Character.toString(a);
        String s2 = Character.toString(b);
        for(int i=0; i<terminals.size()+1; i++){
            if(parseTable[i][0].equals(s1)) row=i;
            if(parseTable[0][i].equals(s2)) column=i;
        }
        return parseTable[row][column];
    }


    public static String correct(String handle){
        StringBuilder sb = new StringBuilder();
        sb.append(handle);
        sb.deleteCharAt(handle.indexOf('<'));
        String str=sb.toString();
        return str;
    }


    public static void parser(String input){
        StringBuilder sb = new StringBuilder();
        String handle = null;
        for(int i=0; i<200; i++){
            if(parse[0].get(i).equals("$N") && parse[1].get(i).equals("$")){
                parse[0].add("$N");
                parse[1].add("$");
                parse[2].add("accept");
                success();
                return;
            }
            else{
                String yardstick = null;
                if(parse[0].get(i).charAt(parse[0].get(i).length()-1)!='N')
                    yardstick = fetch(parse[0].get(i).charAt(parse[0].get(i).length()-1),parse[1].get(i).charAt(0));
                if(parse[0].get(i).charAt(parse[0].get(i).length()-1)=='N')
                    yardstick = fetch(parse[0].get(i).charAt(parse[0].get(i).length()-2),parse[1].get(i).charAt(0));
                if(yardstick.equals("e")){
                    SyntaxError();
                    return;
                }
                else if(yardstick.equals("=")){
                    sb = new StringBuilder();
                    sb.append(parse[0].get(i));
                    sb.append(parse[1].get(i).charAt(0));
                    String str = sb.toString();
                    parse[0].add(str);
                    sb = new StringBuilder();
                    str = parse[1].get(i);
                    sb.append(str);
                    sb.deleteCharAt(0);
                    str = sb.toString();
                    parse[1].add(str);
                    parse[2].add("shift");
                }
                else if(yardstick.equals("<")){
                    sb = new StringBuilder();
                    sb.append(parse[0].get(i));
                    sb.append("<");
                    sb.append(parse[1].get(i).charAt(0));
                    String str = sb.toString();
                    parse[0].add(str);
                    sb = new StringBuilder();
                    str = parse[1].get(i);
                    sb.append(str);
                    sb.deleteCharAt(0);
                    str = sb.toString();
                    parse[1].add(str);
                    parse[2].add("shift");
                }
                else if(yardstick.equals(">")){
                    sb = new StringBuilder();
                    int index =0;
                    String str= parse[0].get(i);
                    for(int j = str.length()-1; j>=0; j--){
                        if(str.charAt(j) == '<'){
                            index=j;
                            break;
                        }

                    }
                    if(str.charAt(index-1)!='N'){
                        handle = str.substring(index,str.length());
                    }
                    else{
                        handle = str.substring(index-1,str.length());
                    }
                    String result= correct(handle);
                    if(!rightparts.contains(result)){
                        parse[0].add(parse[0].get(i));
                        parse[1].add(parse[1].get(i));
                        parse[2].add("syntax error");
                        SyntaxError();
                        return;
                    }
                    if(str.charAt(index-1) == 'N') index--;
                    sb.append(str);
                    sb.delete(index, str.length());
                    sb.append("N");
                    str = sb.toString();
                    parse[0].add(str);
                    parse[1].add(parse[1].get(i));
                    parse[2].add("reduce");

                }
            }
        }
    }
}
