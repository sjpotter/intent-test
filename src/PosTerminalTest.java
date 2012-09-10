/* Simple Cash Register tests */

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;


public class PosTerminalTest {
    PosTerminal terminal;
    
    @Before
    public void setup() throws IOException {
        terminal = new PosTerminal();
        
        String input = "4\n" +
                       "A,2.00,4,7.00\n"+
                       "B,12.00\n"+
                       "C,1.25,6,6.00\n"+
                       "D,0.15";
        
        BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
        
        terminal.setPricing(in);
    }
    
    private void runTest(String input) {
        terminal.resetItems();
        
        for(int i = 0; i < input.length(); i++) {
            terminal.scan(input.charAt(i));
        }
    }

    @Test
    public void test1() {
        String input = "ABCDABAA";

        runTest(input);
        
        assertTrue(terminal.total().equals("32.40"));
    }
    
    @Test
    public void test2() {
        String input = "CCCCCCC";
        
        runTest(input);
        
        assertTrue(terminal.total().equals("7.25"));
        
    }

    @Test
    public void test3() {
        String input = "ABCD";

        runTest(input);
        
        assertTrue(terminal.total().equals("15.40"));
        
    }
}