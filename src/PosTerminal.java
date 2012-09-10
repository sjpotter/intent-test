/* Simple Cash Register that supports single item and volume pricing */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

//TODO: probably can handle currency unit better, double != money
public class PosTerminal {
    public class VolumePrices {
        int volume;
        double price;
        
        VolumePrices(int v, double p) {
            volume = v;
            price = p;
        }
    }
    
    HashMap<String, Double> singlePricing;
    HashMap<String, VolumePrices> volumePricing;
    HashMap<String, Integer> items;
    
    PosTerminal() {
        singlePricing = new HashMap<String, Double>();
        volumePricing = new HashMap<String, VolumePrices>();
        items = new HashMap<String, Integer>();
    }
    
    public void resetItems() {
        items = new HashMap<String, Integer>();
    }
    

    public String total() {
        DecimalFormat myFormatter = new DecimalFormat("0.00");
        
        double total = 0;
        
        for(Entry<String, Integer> e : items.entrySet()) {
            String item = e.getKey();
            int count = e.getValue();

            if (volumePricing.containsKey(item)) {
                total += (count / volumePricing.get(item).volume) * volumePricing.get(item).price;
                count = count % volumePricing.get(item).volume;
            }
            total += count * singlePricing.get(item);
        }
        
        return myFormatter.format(total);
    }

    public void scan(char c) {
        String i = Character.toString(c);
        
        int count = 0;
        if (items.containsKey(i)) {
            count = items.get(i);
        }
        count++;
        items.put(i, count);
    }

    public void setPricing(BufferedReader in) throws IOException {
        String input = in.readLine();
        int count = Integer.parseInt(input);
        for(int i = 0; i < count; i++) {
            input = in.readLine();

            StringTokenizer st = new StringTokenizer(input, ",");
            
            int tokenCount = st.countTokens();
            if (tokenCount != 2 && tokenCount != 4) {
                System.out.println(input + " is invalid input");
                continue;
            }
            
            String item = st.nextToken();
            double singlePrice = Double.parseDouble(st.nextToken());
            singlePricing.put(item, singlePrice);

            if (tokenCount == 4) {
                int volume = Integer.parseInt(st.nextToken());
                double volumePrice = Double.parseDouble(st.nextToken());
                
                volumePricing.put(item, new VolumePrices(volume, volumePrice));
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        PosTerminal terminal = new PosTerminal();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        terminal.setPricing(in);

        String input;
        while ((input = in.readLine()) != null) {
            for(int i = 0; i < input.length(); i++) {
                terminal.scan(input.charAt(i));
            }

            System.out.println("result = " + terminal.total());
            terminal.resetItems();
        }
    }
}