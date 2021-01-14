/**
 * [EncoderCopyToCheckForDecimalBinaryTree.java]
 * Compresses text files by reducing the binary size of characters
 * @author: Ali Meshkat, David Stewart
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException; 

class Encoder{
  public static void main(String[]args) {
    
    //user interface
    Scanner userInput = new Scanner(System.in);
    System.out.println("Enter name of encoded file: ");
    String encodedFileName = userInput.nextLine() + ".txt";
    userInput.close(); // closes Scanner
    
    String text = "";
    try {
      Scanner fileInput = new Scanner(new File("input.txt")); //saves the whole file at ones 
      text = fileInput.useDelimiter("\\A").next();
      fileInput.close(); //closes scanner
    }catch( FileNotFoundException exception){
      System.out.println("ERROR: 'input.txt' not found: " + exception);
    };
    
    
    
    
    String finalTree; //stores the final tree level
    
    ArrayList<Integer > occurence = new ArrayList<Integer>(); //saves the occurences of each char
    ArrayList<String> letter = new ArrayList<String>(); //saves each letter parallel to occurence 
    
    //will be used to run the tree process(will store packages which are the letters + their value as each index)
    ArrayList<String> tree = new ArrayList<String>(); //will be used in the method 'treeBuilder only'
    
    String[] finalValues; //this will be stored as our tree(reference in the file) (char + new binary valie(ex "H0101")
    
    occurenceNum(text, letter,  occurence);       ///runs occurence num
    
    finalValues = new String[letter.size()]; //gives final value its size(initializes it)
    
    for (int i = 0; i < letter.size(); i++){ //gives tree its initial chars + their values 
      tree.add("(" + stringBinaryChar(letter.get(i)) + ")" + occurence.get(i));
    }
    
    
    //creates an array to store the binary assigned values as strings, and sets them to ""
    String[] binaryValues = new String[letter.size()];
    for (int i = 0; i < binaryValues.length; i++){
      binaryValues[i] = "";
    }
    
    for (int i = 0; i < occurence.size(); i ++){ //prints occurence 
      System.out.println(letter.get(i) + " " + occurence.get(i));
    }
    
    
    finalTree = treeBuilder(tree).get(0); //will return the final tree level as the first component of the arraylist(shrunken)(size 1)
    finalTree = finalTree.substring(0, finalTree.lastIndexOf(")")+1); // removes the value at the end
    System.out.println(finalTree);
    
    binaryAssigner(finalTree, binaryValues, letter); // assigns thee new binary values and saves to binaryValues
    
    
    
    //gives finalValues the characters followed by their new binary value 
    for (int i = 0; i< letter.size(); i++){
      finalValues[i] = letter.get(i) + binaryValues[i];
    }
    
    
    //uses bubble sort to final values in order from highest occuring to speed up the encode process
    bubbleSort(finalValues, occurence);
    
    /////////////////////////////
    for (int i = 0; i < finalValues.length; i++){
      System.out.println(finalValues[i]);
    }
    //////////////////////////////
    
    
    /* String result = "";
     char[] messChar = finalValues[5].toCharArray();
     
     for (int i = 0; i < messChar.length; i++) {
     result += Integer.toBinaryString(messChar[i]) + " ";
     }
     
     System.out.println(result);
     */
    
    
    
    //david 
    try{
      FileWriter fw = new FileWriter(encodedFileName, true);
      BufferedWriter bw = new BufferedWriter(fw);
      PrintWriter output = new PrintWriter(bw);
      
      // String[] tree = {"s0","i110","a111","g100","u1010","m1011"};
      saveTree(finalValues, output);
      encode(finalValues, output, text);
    }catch (Exception E){};
    
  }/////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////
  
  
  /**
   * bubbleSort 
   * sorts the final array with chars and their new binary value in order from 
   * highest used first. This is done by getting help from frequency of each 
   * char stored in ArrayList<Integer> occurence
   * @param: string array  of final values, arraylist of occurence 
   * @return: void
   * @author: Ali Meshkat 
   */
  public static void bubbleSort(String[] finalValues, ArrayList<Integer> occurence){
    //temprory variables 
    int mid; 
    String mid2; 
    
    for (int j = 0; j <= finalValues.length-1; j++){ //runs through the array
      for (int i = 0 ; i <= finalValues.length-2; i ++){
        
        if (occurence.get(i) < occurence.get(i+1)){ //compares adjacent chars (if higher occurence is on right)
          //set temps 
          mid = (int)occurence.get(i); 
          mid2 = finalValues[i];
          
          //switch values for both final values and occurence
          occurence.set(i, occurence.get(i+1));
          finalValues[i] = finalValues[i+1];
          finalValues[i+1] = mid2;
          occurence.set(i +1 , new Integer(mid));
          
        }
      }
    }
    
  }
  
  
  /**
   * binaryAssigner 
   * recursively assigns the new binary values of each character by breaking the tree down
   * into two different packages each time and assigning 0's to one and 1's to the other
   * records them in binaryValues array which is parallel to letter 
   * @param: a string representing the final tree level, array of strings to store values in, and letters arraylist 
   * @return: void 
   * @author: Ali Meshkat 
   */
  public static void binaryAssigner(String treeStr, String[] binaryValues, ArrayList<String> letter){
    
    if (treeStr.length() > 5){
      String newStr; 
      int bracketValue= 0;   //to find the two outermost packages 
      newStr = treeStr.substring(1, treeStr.length()-1); // removes first and last brackets 
      String package1, package2; // the two separated parts will be stored in 
      
      
      //finds the outermost packages of the string 
      //a do-while is used since a while would stop at the first iteration 
      int counter = -1; //keeps track of the characters
      do{
        counter++;
        //adds one per open bracket and subtracts per closed bracket(stops at zero)
        if (newStr.substring(counter,counter+1).equals("(")){
          bracketValue++;
        }else if (newStr.substring(counter,counter+1).equals(")")){
          bracketValue--;
        } 
        
      }while(bracketValue > 0);
      
      //separates treeStr into two packages
      package1 = newStr.substring(0,counter + 1); 
      package2 = newStr.substring(counter+1 , newStr.length());
      
      
      
      //assigns a 0 to all those in package 1
      for (int i = 0; i < package1.length()-2; i++){ //runs through string 
        for (int j = 0; j < letter.size(); j++){  //runs through letters arraylist 
          if(stringBinaryChar(letter.get(j)).equals(package1.substring(i,i+3))){ //if the string decimal binary letters match 
            binaryValues[j] += "0"; //add 0
          }
        }
      }
      //assigns a 1 to all those in package 2
      for (int i = 0; i < package2.length()-2; i++){
        for (int j = 0; j < letter.size(); j++){
          if(stringBinaryChar(letter.get(j)).equals(package2.substring(i,i+3))){
            binaryValues[j] += "1";
          }
        }
      }
      
      //ruruns the same method passing in the new smaller packages
      binaryAssigner(package1, binaryValues, letter);
      binaryAssigner(package2, binaryValues, letter);
    }
  }
  
  /**
   * stringBinaryChar
   * takes in a string of size one, converts to char, gets its binary value in decimal, and returns the value in three digits as a string 
   * @param: a string of size one(only charAt(0) will be used )
   * @return: a string representing of the decimal binary value in 3 digits 
   * @author: Ali Meshkat 
   */
  public static String stringBinaryChar(String str){
    if ((int)str.charAt(0)  < 10) { //if the value is smaller than 10 
      return "00" + (int)str.charAt(0);   //add two zeroes to the left of it(three digits)
    }else if ((int)str.charAt(0)  < 100) { // if 9 < x < 100 
      return "0" + (int)str.charAt(0);
    }else{     // if x > 99 
      return "" + (int)str.charAt(0);
    }
    
  }
  
  
  /**
   * treeBuilder
   * a recursive algorithm that does the tree process by going through the array and combining the two lowest values until none is left while adding brackets to 
   * show every step 
   * @param: the arraylist of strings 
   * @return: an arraylist of string 
   * @author: Ali Meshkat 
   */
  public static ArrayList<String> treeBuilder(ArrayList<String> tree){
    int min1 = 2147483647;
    int min2 = 2147483647;
    int indexNum1 = 0;
    int indexNum2 = 0;
    String addedComponents;
    
    /////finds the two lowest values of the tree(array)
    if (tree.size() > 1){
      for (int i = 0; i < tree.size(); i++){
        if (Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") +1 ,tree.get(i).length())) < min1){ // finds the value of the package 
          min1 = Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") +1 ,tree.get(i).length())) ;        //sets the min value
          indexNum1 = i;  //stores index of min valie 
        }
      }
      for (int i = 0; i < tree.size(); i++){
        if (Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") +1 ,tree.get(i).length())) < min2 &&     i != indexNum1){
          min2 = Integer.parseInt(tree.get(i).substring(tree.get(i).lastIndexOf(")") +1 ,tree.get(i).length()));
          indexNum2 = i;
        }
      }
      /////
      
      addedComponents = addComponents(tree.get(indexNum1), tree.get(indexNum2)); // adds the two lowerst packages using method and stores 
      
      //$removes old ones
      if (indexNum1 > indexNum2){
        tree.remove(indexNum1); 
        tree.remove(indexNum2); 
      }else{
        tree.remove(indexNum2); 
        tree.remove(indexNum1); 
      }
      //$
      
      tree.add(addedComponents); //adds new combined packages 
      
      return treeBuilder(tree); //calls itself with one less components
    }else {
      return tree; //base case when theres only one component left 
    }
    
  }
  
  
  /**
   * addComponents 
   * adds two packages together, adds brackets and the sum of values and returns
   * @param: two strings that need to be combined 
   * @return: a string representing the final result 
   * @author: Ali Meshkat 
   */
  public static String addComponents(String first, String second){
    String str;
    str = "(" + first.substring(0,first.lastIndexOf(")") +1 ) + second.substring(0,second.lastIndexOf(")") +1 ) + ")"   + //** make it add and store as decimal binary values nvm prob no change needed
      (Integer.parseInt(first.substring(first.lastIndexOf(")") +1 ,first.length()))  
         + Integer.parseInt(second.substring(second.lastIndexOf(")") +1 ,second.length()))) ; //makes the two packages into 1 and adds brackets
    
    return str;
  } 
  
  
  
  /**
   * occurenceNum 
   * finds the occurence of every character 
   * @param: a string, arrayList of characters(string), Integer arraylist of occurences 
   * @return: void
   * @author: Ali Meshkat 
   */
  public static void occurenceNum(String str, ArrayList<String> letter, ArrayList<Integer> occurence){
    String newChar;
    boolean alreadySaved;
    int index = 0; 
    
    
    for (int i = 0; i < str.length();i++){ //runs thru string 
      alreadySaved = false; 
      newChar = str.substring(i, i+1); //takes a char 
      for (int j = 0; j < letter.size(); j ++){ //runs through array of chars 
        if ( letter.size() > 0){ //if already saved 
          if (newChar.equals(letter.get(j))){ //if already saved
            alreadySaved = true;
            index = j;
          }
        }
        
      }
      
      
      if (letter.size() == 0){  //if array has a lenght of 0 (fist letter)
        letter.add(newChar);    //addd letter
        occurence.add(new Integer(0));   //add occurence of 1
        alreadySaved = true;    
      }
      
      
      if (!alreadySaved){      //if letter is new 
        letter.add(newChar);
        occurence.add(new Integer(1));
      }else{                   //if not new just add to its occurence 
        Integer integerIntValue =occurence.get(index) + 1;   //takes out the old value and adds one
        occurence.set(index, integerIntValue);  //sets the new value 
      }
    }
    
    
  }
  
  
  
  
  ////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////
  
  
  //@author: David Stewart + Ali Meshkat 
  public static void encode(String[] tree, PrintWriter output, String text)throws IOException {
    String encodedBinaryTemp = "";
    String encodedBinary = ""; //create variable to store encoded data as ones and zeroes as a string
    System.out.println("finding binary");
    //converts file into zeroes and ones saved as a string
    for (int i = 0; i<text.length();i++){ //run through text character by character
      String originalChar = text.substring(i,i+1); //isolate charater
      
      for (int j = (tree.length-1); j>=0;j--){
        if (originalChar.equals(tree[j].substring(0,1))){//find character in tree
          encodedBinaryTemp += tree[j].substring(1,(tree[j].length()));//add encoded value from tree to total text
          break;
        }
      }
      
      //resets encodedBinaryTemp so java is working with a smaller string(speeds up the process by A LOT)
      //1752 is found to one of the fastest sizes after several trials
      if(encodedBinaryTemp.length() > 1752){ 
        printEncoded(encodedBinaryTemp.substring(0,1752), output);
        
        encodedBinaryTemp = encodedBinaryTemp.substring(1752, encodedBinaryTemp.length());
      }
    }
    
    
    encodedBinary += encodedBinaryTemp;
    int emptyBits = 0;
    
    for (int i = 0;i<(8-(encodedBinary.length()%8));i++){
      encodedBinary += "0";
      emptyBits++;
    }
    
    printEncoded(encodedBinary, output); //prints rest of it to file 
    
    output.print(emptyBits); //prints the number of empty bits at the end
    
    System.out.println("saved as binary");
    
  }//end of method encode
  
  //@author: David Stewart
  public static void saveTree(String[] tree, PrintWriter output)throws IOException{
    
    for (int i = 1; i<255;i++){//run through all 255 binary characters
      Boolean charNotPresent = true;
      
      for (int j = (tree.length-1); j>=0;j--){
        if ((char)i == tree[j].charAt(0)){
          output.println(tree[j].substring(1,tree[j].length()));
          charNotPresent = false;
        }
      }
      if (charNotPresent){
        output.println("");
      }
    }
  }
  
  //@author: David Stewart
  public static void printEncoded(String encodedBinary, PrintWriter output){
    
    
    for (int i = (encodedBinary.length()/8)-1; i >= 0;i--){//calculates how many groupings of 8 bits are necassary to save data
      
      
      char character = '\0';//null char to edit
      
      
      for (int j = 1; j<=8;j++){//runs 8 times, once for each bit
        
        
        if(i > 0 && encodedBinary.substring(j-1,j).equals("1")){//if a character in a specific place equals one
          character = (char)(character | (char)(Math.pow(2,8-j)));//writes a bit to the appropriate place in the character
        }//else this bit is left blank (0)
        
      }//end for
      
      encodedBinary = encodedBinary.substring(8,encodedBinary.length());//removes the 8 bits that have just been written
      
      
      output.print(character);//send the character to the text file
      
    }//end for
    
  }//end of method printEncoded
  
 
  
  
}