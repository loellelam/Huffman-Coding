/**
 * 
 */
package edu.ics211.h11;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Compresses a file.
 * @author loelle
 *
 */
public class Compress {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		FileInputStream in = null;
        FileOutputStream out = null;

        String fileName = args[0];
        String extension = fileName.substring(fileName.length()-3, fileName.length());
        try {
        	//txt file
        	if (extension.equals("txt")) {
	            in = new FileInputStream("CodeReview.txt");
	            out = new FileOutputStream("CodeReview.huf");
	            
	            //store the input file's contents as a string
	            byte[] b = new byte[(int) in.getChannel().size()]; 
	            in.read(b);
	            String inStr = new String(b, StandardCharsets.UTF_8); //convert byte arr to string
	            
	            //encode the contents
	            Huffman huffman = new Huffman(inStr);
	            BitStringInterface bitStr = huffman.encode(inStr);
	            
	            //save the bitstring in byte format to the output file
	            out.write(bitStr.toBytes());
	            
        	}
        	//huf file
        	else if (extension.equals("huf")) {
        		in = new FileInputStream("CodeReview.huf");
	            out = new FileOutputStream("CodeReview.211.txt");
	            
	            //reread original txt file to recreate Huffman tree
	            FileInputStream in2 = new FileInputStream("CodeReview.txt");
	            byte[] b2 = new byte[(int) in2.getChannel().size()]; 
	            in2.read(b2);
	            String inStr = new String(b2, StandardCharsets.UTF_8); //convert byte arr to string
	            Huffman huffman = new Huffman(inStr);
	            
	            //decode the contents
	            byte[] b = new byte[(int) in.getChannel().size()]; 
	            in.read(b);
	            BitString bitStr = new BitString(b);
	            String decoded = huffman.decode(bitStr);
	            
	            //convert decoded string to bytes and write it to the output
	            out.write(decoded.getBytes());
        	}
        	//neither txt or huf file
        	else {
        		System.out.println("Error. Not a .txt or .huf file");
        	}
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

	}
	

}
