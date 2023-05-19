/**
 * 
 */
package edu.ics211.h11;

/**
 * Implementation of a Huffman tree.
 * The Huffman tree is used for encoding and decoding text.
 * 
 * The original CodeReview.txt takes 933 bytes. The compressed version now takes 494 bytes.
 * 
 * @author edo, loelle
 *
 */
public class Huffman {

	// your instance variables and inner class definitions go here
	  HuffmanNode tree;

	  private class HuffmanNode {
	    int frequency;
	    // a node in a Huffman tree has either a value or a left and right subtree
	    //
	    // for the implementation of the priority queue we also keep a next
	    // reference.
	    char value;
	    HuffmanNode next;
	    HuffmanNode left;
	    HuffmanNode right;

	    public HuffmanNode(int f, char v, HuffmanNode n) {
	      frequency = f;
	      value = v;
	      next = n;
	      left = null;
	      right = null;
	    }

	    public HuffmanNode(HuffmanNode l, HuffmanNode r) {
	      frequency = l.frequency + r.frequency;
	      value = l.value;  // never used, but might as well initialize
	      next = null;
	      left = l;
	      right = r;
	    }

	    // swap the values while not changing the next field
	    private void swapNext() {
	      int f = frequency;
	      char v = value;
	      HuffmanNode l = left;
	      HuffmanNode r = right;
	      
	      frequency = next.frequency;
	      value = next.value;
	      left = next.left;
	      right = next.right;

	      next.frequency = f;
	      next.value = v;
	      next.left = l;
	      next.right = r;
	    }

	    //returns the code for character c
	    public String findChar(char c) {
	      if (left == null) {
	        if (value == c) {
	          return "";
	        } else {
	          return null;
	        }
	      }
	      String l = left.findChar (c);
	      if (l != null) {
	        return "0" + l;
	      }
	      String r = right.findChar (c);
	      if (r != null) {
	        return "1" + r;
	      }
	      return null;
	    }

	    public String toString() {
	      if (left == null) {
	        return value + ": " + frequency + "\n";
	      } else {
	        return "---: " + frequency + "\n" + left + right;
	      }
	    }
	  }
	  
	  //end of HuffmanNode definition
	  

	  //add method for a character that already exists in the tree
	  private boolean addIfThere(HuffmanNode start, char v) {
	    HuffmanNode current = start;
	    while (current != null) {
	      if (current.value == v) {
	        current.frequency = current.frequency + 1; 
	        while ((current.next != null) &&
	               (current.next.frequency < current.frequency)) {
	          current.swapNext();
	          current = current.next;
	        }
	        return true;
	      }
	      current = current.next;
	    }
	    return false;
	  }

	  private HuffmanNode add(HuffmanNode start, char v) {
	    // have to traverse the linked list up to twice, once to see if it
	    // is there, then to add it if it isn't already there
	    if (addIfThere(start, v)) {
	      return start;
	    }
	    return new HuffmanNode(1, v, start);
	  }

	  private HuffmanNode add(HuffmanNode start, HuffmanNode newNode) {
	    if (start == null) {
	      return newNode;
	    }
	    if (newNode.frequency <= start.frequency) {
	      newNode.next = start;
	      return newNode;
	    }
	    HuffmanNode current = start;
	    while ((current.next != null) &&
	           (newNode.frequency > current.next.frequency)) {
	      current = current.next;
	    }   // current.next is null or newNode.frequency <= current.next.frequency,
	        // insert after the current node
	    newNode.next = current.next;
	    current.next = newNode;
	    return start;
	  }

	  /**
	   * Creates a Huffman tree
	   * @param letters		use Huffman coding on this string
	   */
	  public Huffman(String letters) {
	    HuffmanNode queue = null;
	    for (int i = 0; i < letters.length(); i++) {
	      queue = add(queue, letters.charAt(i));
	    }
	    java.util.ArrayList<Character> chars = new java.util.ArrayList<Character>();
	    HuffmanNode collect = queue;
	    while (collect != null) {
	      chars.add(collect.value);
	 System.out.print(collect.value + ": " + collect.frequency + ", ");
	      collect = collect.next;
	    }
	 System.out.println();
	    // makes this a Huffman tree with the letter queue from the string
	    while (queue.next != null) {
	      // take the first two values and make them into a new node
	      queue = add(queue.next.next, new HuffmanNode (queue, queue.next));
	    }
	     System.out.println("Huffman tree is: " + queue);
	     for (Character c: chars) {
	       System.out.println(c + " = " + queue.findChar(c));
	     }
	    tree = queue;
	  }
	  

	  public BitStringInterface encode(String s) {
		//create empty bitstring
	    BitString result = new BitString();
	    // use the Huffman tree to encode this string
	    
	    //loop through s and store the code for each character in a string
	    String codes = "";
	    for (int i = 0; i < s.length(); i++) {
	    	codes += tree.findChar(s.charAt(i));
	    }
	    
	    //convert the string codes into an array of booleans
	    Boolean[] boolCodes = new Boolean[codes.length()];
	    for (int i = 0; i < codes.length(); i++) {
	    	if (codes.charAt(i) == '0') {
	    		boolCodes[i] = false;
	    	}
	    	else {
	    		boolCodes[i] = true;
	    	}
	    }
	    
	    //add to bitstring and convert to desired format
	    result.addBits(boolCodes);
	    return new BitString(result.toBytes());
	  }

	  public String decode(BitStringInterface data) {
	    StringBuilder result = new StringBuilder();
	    // use the Huffman tree to decode this string
	    
	    //use iterator method to traverse the bitstring called data
	    java.util.Iterator<Boolean> it = data.iterator();
	    //current variable to keep track of where you are in the huffman tree
	    HuffmanNode current = tree;
	    
	    while (it.hasNext()) {
	    	if (it.next()) { //1, go right
	    		current = current.right;
	    	}
	    	else { //0, go left
	    		current = current.left;
	    	}
	    	if (current.left == null && current.right == null) { //leaf node
	    		result.append(current.value);
	    		current = tree; //restart traversal from root
	    	}
	    }
	    
	    
	    return result.toString();
	  }

	  public static void testHuffman(String textOrFileName) {
	    String text = textOrFileName;
	    String filename = "";
	    try {   // read from the file if textOrFileName is the name of a file 
	      StringBuilder sb = new StringBuilder();
	      java.nio.file.Path p =
	        java.nio.file.FileSystems.getDefault().getPath(textOrFileName);
	      for (String s: java.nio.file.Files.readAllLines(p)) {
	        sb.append(s + "\n");
	      }
	      text = sb.toString();
	      filename = textOrFileName;
	    } catch (Exception e) {
	      // nothing to do, text and filename are already initialized
	    }
	// System.out.println ("text " + text);
	    Huffman key = new Huffman(text);
	    BitStringInterface encoded = key.encode(text);
	// System.out.println("encoded bitstring is " + encoded);
	    String decoded = key.decode(encoded);
	// System.out.println ("decoded " + decoded);
	    assert (text.equals(decoded)) : "decoded != text";
	    System.out.println("text has " + text.length() + " bytes = " +
	                       (text.length() * 8) + " bits, encoded text has " +
	                       encoded.length() + " bits");
	    System.out.println((filename.equals("") ? "" : (filename + ": ")) +
	                       "encoded size is " +
	                       (double)encoded.length() / (text.length() * 8.0) +
	                       " of original");
	  }

	  public static void main(String[] args) {
	    for (String s: args) {
	      testHuffman(s);
	    }
	    
	    
	    String s = "hello there, world";
	    s = "BitStringInterface contains methods that must be implemented in BitString. BitString is used to store, convert, and access relevant information on bits represented as an array of booleans. This class provides a way to store and access bits in Huffman.java. Huffman.java includes the implementation of a Huffman tree and allows encoding and decoding using the tree. The tree is the key to encoding and decoding as it’s structured so that fewer bits are required to represent high frequency characters. After using the tree to obtain codes for each character and storing it in an array, the array can be passed to addBits, a method that stores the array data in another array called bitString. The bitstring is converted to the desired format using toBytes. This bitstring is then returned as the encoded version of the text. I learned that ? is the conditional operator and that assert statements can be used outside of JUnit tests.";
	    Huffman h = new Huffman(s);
	    
	    BitStringInterface bstr = h.encode(s);
	    System.out.println("encoding...\n" + bstr);
	    System.out.println("decoding...\n" + h.decode(bstr));
	  }

}
