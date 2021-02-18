package Lab1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class RegExHashMap<K, V> extends HashMap<K, V> {
    // list of regular expression patterns
    private ArrayList<Pattern> regExPatterns = new ArrayList<Pattern>();
    // list of regular expression values which match patterns
    private ArrayList<V> regExValues = new ArrayList<V>();

    @Override
    public V put(K key, V value) {

        regExPatterns.add(Pattern.compile(key.toString()));
        regExValues.add(value);
        return value;
    }

    @Override
    public V get(Object key) {
        CharSequence cs = new String(key.toString());

        for (int i = 0; i < regExPatterns.size(); i++) {
            if (regExPatterns.get(i).matcher(cs).matches()) {

                return regExValues.get(i);
            }
        }
        return super.get(key);
    }
}