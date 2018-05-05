package com.carson.classes;




import java.util.ArrayList;
import java.util.List;


public class Gumbo {
  
  
  String password;
  boolean pass;
  private boolean printStatment = false;
  
  public Gumbo(String password){
    this.password = password;
    pass = true;
  }
  
  
  
  public Gumbo(){
    pass = false;
  }




  public String codePass(String text, boolean code, String password){
    pass = true;
    this.password = password;
    String returnText = code(text,code);
    pass = false;
    return returnText;
  }
  public String codeNormal(String text, boolean code){
    pass = false;
    String returnText = code(text, code);
    pass = true;
    return returnText;
  }
  public String code(String text, boolean code) {
   
 
  
  
  
 
  
  
 
 
 String input = text;
  
  input = input.toLowerCase();
  if(printStatment) {
	  System.out.println(input);
  }
  List<String> words = new ArrayList<String>();
  
  List<String> bits = new ArrayList<String>();
 
 List<String> swap0 = new ArrayList<String>();
  
  List<String> swap1= new ArrayList<String>();
 
 
 /*
   List<String> lines = FileIO.use(new File("C:\\users\\carson\\desktop\\discord\\carson-bot\\gumbo\\swaps.txt")).read();
   
   for(String line : lines){
     swap0.add(line.charAt(0) + line.charAt(1));
     swap1.add(line.charAt(3) + line.charAt(4));
   }
   
   
   
   
   
   
   */
 
 FileIO f = new FileIO("C:\\Users\\Carson\\Desktop\\discord\\carson-bot\\swaps.txt");
 List<String> swaps =  f.readList();
 for(String swap : swaps) {
	 swap0.add(   swap.substring(0,2)  );
	 swap1.add(   swap.substring(3,6)  );
	 
 }
 
 
 if(printStatment) {
	 for(int i = 0;i<swap0.size();i++) {
		 System.out.println("GUMBBO:" + swap0.get(i) + ":" + swap1.get(i));
	 }
	 
 }
 if(code){
	 if(printStatment) {
System.out.println("swap0 size:" + swap0.size() + "   swap1:" + swap1.size());
	 }
  boolean space = false;
  String temp = "";
  
  for(int i =0;i<input.length();i++){
	  if(printStatment) {
    System.out.println("running:"  + i);
	  }
    if(space){
    	if(printStatment) {
      System.out.println("was space");
    	}
      space = false;
    }else{
    	if(printStatment) {
      System.out.println("was not space");
    	}
      temp+= input.charAt(i);
      
      if(!(i+1 >= input.length())){
        if(input.charAt(i+1) == ' '){
          words.add(temp);
          temp = "";
          space = true;
        }
      }else{
        words.add(temp);
        break;
      }
    }
    
    
    
  }
  if(printStatment) {
System.out.println("==============");
  
for(int i = 0; i<words.size();i++){
  System.out.println(words.get(i));
}}

for(String word : words){
	if(printStatment) {
		System.out.println("running:" + word + "   word.ldngth()=" + word.length());
	}
  bits.add("NEW_WORD");
  for(int i =0; i<word.length();i++){
    
    if(i+2 <= word.length()){
      bits.add(String.valueOf(word.charAt(i)) + String.valueOf(word.charAt(i+1)));
      i++;
    }else{
      bits.add(String.valueOf(word.charAt(i)) );
    }
    
    
  }
  
  
  
  
}


if(printStatment) {
System.out.println("==============");

for(String bit : bits){ 
  System.out.println(bit);
}}

for(int i = 0; i<bits.size();i++){
  String bit = bits.get(i);
  
  if(bit.equals("NEW_WORD")){
    
  }else{
    
    for(int q =0;q<swap0.size();q++){
      
     if(swap0.get(q).equals(bit)){
        bits.set(i, swap1.get(q));
       break;
     }
   }
    
    
    
    
    
  }
  
  
  
  
  
  
}


for(int i = 0; i< bits.size();i++){
  String bit = bits.get(i);
  
  if(bit.length() == 2){
    bit = String.valueOf(flopChar(bit.charAt(0), true)) +   String.valueOf(flopChar(bit.charAt(1), true));
    
    
  }else if(bit.equals("NEW_WORD")){
    
    
    
  }else if(bit.length() == 1){
    
    bit = String.valueOf(flopChar(bit.charAt(0), true));
   
    
  }
  
  bits.set(i,bit);
  
  
  
  
}


if(printStatment) {
System.out.println("==============");

for(String bit : bits){ 
  System.out.println(bit);
}}

bits.add("END_BIT");
if(printStatment) {
System.out.println("==============");}
String out ="";
for(int i = 0; i<bits.size();i++){
  
  String bit = bits.get(i);
  if(bit.equals("END_BIT")){
	  if(printStatment) {
    System.out.println();}
    break;
  }else if(bit.equals("NEW_WORD")){
	  if(printStatment) {
    System.out.print(" ");}
    out+=' ';
  }else{
    if(bits.get(i+1).equals("NEW_WORD")){
    	if(printStatment) {
      System.out.print(bit);}
      out+=bit;
    }else{
    	if(printStatment) {
       System.out.print(bit + "-");}
       out+=bit + '-';
    }
    
  }
}
return out;

}else{//decode --------------------###################
  
  
  
   
  boolean space = false;
  String temp = "";
  
  for(int i =0;i<input.length();i++){
	  if(printStatment) {
    System.out.println("running:"  + i);}
    if(space){
    	if(printStatment) {
      System.out.println("was space");}
      space = false;
    }else{
    	if(printStatment) {
      System.out.println("was not space");}
      
      temp+= input.charAt(i);
      
      if(!(i+1 >= input.length())){
        if(input.charAt(i+1) == ' '){
          words.add(temp);
          temp = "";
          space = true;
        }
      }else{
        words.add(temp);
        break;
      }
    }
    
  }
  
   bits = new ArrayList<String>();
  //lup-nog-u-
   if(printStatment) {
  System.out.println("==============");}
    for(String word : words){
    	if(printStatment) {
        System.out.println(word);}
      
        String tempBit = "";
        for(int i =0;i<word.length();i++){
           char chr = word.charAt(i);
           
           if(chr == '-' || chr == ' '){
             bits.add(tempBit);
             tempBit = "";
             
           }else{
             
             tempBit+= chr;
             
           }
      
           if(tempBit != "" && i == word.length() - 1){
             bits.add(tempBit);
           }
        
        
       }
      
      bits.add("NEW_WORD");
      
    }
    
    bits.remove(bits.size()-1);
    if(printStatment) {
    System.out.println("=================");}
    
    for(int i = 0;i<bits.size();i++){
      String bit = bits.get(i);
      if(printStatment) {
      System.out.println(bit);
      }
     if(bit.equals("NEW_WORD")){
       
     }else if(bit.length() ==2){
       bit = String.valueOf(flopChar(bit.charAt(0), false)) + String.valueOf(flopChar(bit.charAt(1), false));
       bits.set(i,bit);
     }else if(bit.length() == 3){
       for(int q =0;q<swap1.size();q++){
      
       if(swap1.get(q).equals(bit)){
         bits.set(i, swap0.get(q));
          break;
       }
      }
    
     }else{
       //throw failure
     }
     
    }
    if(printStatment) {
  System.out.println("=================");
   for(String bit : bits){
     System.out.println(bit);
   }}
  
  
  

String output = "";
if(printStatment) {
System.out.println("=================");}
   for(String bit : bits){
	   if(printStatment) {
     System.out.println(bit);}
     if(bit.equals("NEW_WORD")){
       output+=' ';
     }else{
    	 if(printStatment) {
       System.out.print(bit);}
       output+=bit;
     }
   
   
   
   
   }
   if(printStatment) {
   System.out.println("=================");

System.out.println(output);}
   
   return output; 
  }
 

} 








  private char flopChar(char c, Boolean flop){
    
    
    String conStr = "bcdfghjklmnpqrstvwxyz";
    
    String vowStr = "aeiou";
    
    if(pass){
      //make conB and vowB
      //split pass into conP and vowP
      //loop though conB and replace anything that is alsp in vowP to �
      //same on vowB
      //delete �
      //make conStr into conP + conB
      //same on vow
      
      List<Character> conB = new ArrayList<Character>();
      List<Character> vowB = new ArrayList<Character>();
      List<Character> conP = new ArrayList<Character>();
      List<Character> vowP = new ArrayList<Character>();
      
      for(int i = 0;i< conStr.length();i++){
        conB.add(conStr.charAt(i));
      }
      
      for(int i = 0;i< vowStr.length();i++){
        vowB.add(vowStr.charAt(i));
      }
      
      
      
       for(int i = 0; i < password.length();i++){
         char tempC = password.charAt(i);
         for(int q = 0 ; q < conB.size();q++){
           if(conB.get(q) == tempC){
             conP.add(tempC);
             break;
           }
           
         }
         
         for(int q =0; q< vowB.size();q++){
           if(vowB.get(q) == tempC){
             vowP.add(tempC);
             break;
           }
         }
       }
        
        for(int i = 0; i<conP.size();i++){
          for(int q = 0 ; q < conB.size();q++){
            if(conP.get(i) == conB.get(q)){
              conB.set(q,'^');
            }
           }
        }
        
        for(int i = 0; i<vowP.size();i++){
          for(int q = 0 ; q < vowB.size();q++){
            if(vowP.get(i) == vowB.get(q)){
              vowB.set(q,'^');
            }
           }
        }
        
        
        if(true){
          int i = 0;
        
          while(true){
            if(i >= conB.size()){
              break;
            }else if(conB.get(i) == '^'){
              conB.remove(i);
            }else{
              i++;
            }
         
          
          
          } 
          i = 0;
          
          while(true){
            if(i >= vowB.size()){
              break;
            }else if(vowB.get(i) == '^'){
              vowB.remove(i);
            }else{
              i++;
            }
         
          
          
          }
          
        }
        
        for(int i = 0;i<conP.size()-1;i++){
          for(int q = i+1;q<conP.size();q++){
 
            if(i == q){
              
            }else if(conP.get(i) == conP.get(q)){
              conP.set(q, '^');
            }
          }
        }
           
        for(int i = 0;i<vowP.size()-1;i++){
          for(int q = i+1;q<vowP.size();q++){
 
            if(i == q){
              
            }else if(vowP.get(i) == vowP.get(q)){
              vowP.set(q, '^');
            }
          }
        }
       
       for(int i = 0;i<conP.size();i++){
         if(conP.get(i)  == '^'){
           conP.remove(i);
         }
       }
       
       for(int i = 0;i<vowP.size();i++){
         if(vowP.get(i) == '^'){
           vowP.remove(i);
         }
       }
         
        String cS = "";
        String vS = "";
        
        for(char c1 : conP){
          cS+=c1;
        }
        for(char c1 : vowP){
          vS+=c1;
        }
        
        for(char c1 : conB){
          cS+=c1;
        }
        for(char c1 : vowB){
          vS+=c1;
        }
        
        conStr = cS;
        vowStr = vS;
        if(printStatment) {
        	System.out.println(">>> cS:" + cS + "   vS:" + vS);
        }
    }//end if(pass)
    
    
    
    
    
    char[] con = new char[conStr.length()];
    
    char[] vow = new char[vowStr.length()];
    
   
    for(int i =0;i<conStr.length();i++){
      con[i] = conStr.charAt(i);
    }
    
    for(int i =0;i<vowStr.length();i++){
      vow[i] = vowStr.charAt(i);
    }
    
   
  
   
    if(flop){
    
      int index = -1;
      
      for(int i = 0; i<con.length;i++){
        if(con[i] == c){
          
              if(i+1 == con.length){
                i = -1;
              }
              return con[i+1];
        }
        
      }
      
      if(index == -1){
        for(int i = 0; i<vow.length;i++){
           if(vow[i] == c){
       
              if(i+1 == vow.length){
                i = -1;
              }
              return vow[i+1];
              
           }
        
        }
      }
      
      
        
        
        
        
       
    
      
      
      
    }else{
      int index = -1;
      
      for(int i = 0; i<con.length;i++){
        if(con[i] == c){
          
              if(i-1 == -1){
                i = con.length;  //here
              }
              
              return con[i-1];
        }
        
      }
      
      if(index == -1){
        for(int i = 0; i<vow.length;i++){
           if(vow[i] == c){
       
              if(i-1 == -1){
                i = vow.length;
              }
              return vow[i-1];
              
           }
        
        }
      }
      
      
      
      
      
      
    
    
    }
    
    
    return '^';
    
    
    
    
    
  }

}




