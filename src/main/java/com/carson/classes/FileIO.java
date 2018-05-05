package com.carson.classes;


//run help to print descriptions of all methods

import java.io.*;
import java.util.*;


//to add:



//Version 0.4.7
public class FileIO {
	
	private File file;
	//Constructor using String
	public FileIO(String path) {
	this.file = new File(path);	
	}
	
	//Constructor using File
	public FileIO(File file) {
		this.file = file;
	}
	
	//Constructor using path,.. for some reason
	
	
	public static FileIO use(File file) {
		return new FileIO(file);
	}
	
	public static FileIO use(String path) {
		return new FileIO(path);
	}
	
	public static FileIO use(FileIO f) {
		return f;
	}
	
	public boolean write(String text) {
		
		try {
			//open FileWriter
			FileWriter  fw = new FileWriter(file);
			//write
			fw.write(text);
			//close
			fw.close();
			//return operation successful 
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			//if fail, return false
			return false;
		}
	
		
	}
	
	public boolean writeArray(String[] strs) {
		String temp = "";
		for(int i = 0;i<strs.length;i++) {
			temp+=strs[i] + "\r\n";
		}
		return write(temp);
		
		
	}
	
	public boolean writeList(List<String> strs) {
		return writeArray(strs.toArray(new String[strs.size()]));
	}

	public boolean replaceLine(int line, String text) {
		
		List<String> list = readList();
		
		if(line == -1 || line > list.size()) {
			System.out.println("not a valed line, or text not indexable");
			return false;
		}else {
			list.set(line, text);
			return writeList(list);
		}
		
	}
	public boolean replaceText(String text0, String text1) {
		return replaceLine(index(text0), text1);
	}
	public boolean delete() {
		
		return file.delete();
		
	}
	public boolean isDir() {
		return file.isDirectory();
		
	}
	public String getPath() {
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			System.out.println("failed on getting file path");
			e.printStackTrace();
			return "";
		}
		
		
	}
	public boolean addln(String text) {

		
		List<String> list = readList();
		list.add(text);
		return writeList(list);
	}
	
	public boolean addln(int text) {
		return addln(Integer.toString(text));
	}
	
	public boolean add(int text) {
		return add(Integer.toString(text));
	}
	
	
	
	public boolean pExists() {
		boolean t = exists();
		if(t)
			System.out.println("file exists");
		else
			System.out.println("file doesn't exist");
	
		
		return t;
	}
	
	public boolean add(String text) {
		
	
		List<String> list = readList();
		if(list.size() == 0) {
			return write(text);
		}
		list.set(list.size() - 1, list.get(list.size() - 1) + text);
		
		return writeList(list);
		
		
	}
	
	public void print() {
		
		List<String> list = readList();
		System.out.println();
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
		list.clear();
		
		
		
	}
	
	public List<String> readList() {
		List<String> list = new ArrayList<String>();

		try {
			if(!exists()) {
				System.out.println("file does not exist, sending back a empty arrayList");
				return new ArrayList<String>();
			}
			FileReader fr = new FileReader(file);
		
			BufferedReader br = new BufferedReader(fr);
			
			String line;
			while ((line = br.readLine()) != null)
		    {
		      list.add(line);
		    }
			fr.close();
		    br.close();
		    
		    
	    } catch (Exception e ) {
	    	System.out.println("catched Execption in readList()");
			e.printStackTrace();
			
	    }
		
	    return list;
		
	}
	public String readString() {
		
		return readList().toString();
		
		
	}
	
	public int index(String text) {
		
		
		List<String> lines = readList();
		
		for(int i = 0;i<lines.size();i++) {
			if(lines.get(i).equals(text)) {
				return i;
			}
		
		
		}
		
		return -1;
		
	}

	public boolean exists() {
		return file.exists();
	}
	public boolean create() {
		try {
			
			return file.createNewFile();
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;

		}
		
		
	}
	
	
	
	
	
	public List<File>listFiles(){
		List<File> list = new ArrayList<File>();
		return listItemsDir(list, file);
	}
	
	
	public File[] listFilesArray() {
		return listFiles().toArray(new File[listFiles().size()]);
	}
	
	
	public void printAll() {
		printAll(file);
		
	}
	
	private void printAll(File file) {
		
		System.out.println("(dir)" + file.getName());
		
		File[] files = file.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				printAll(files[i]);
			}else {
					
					System.out.println(files[i].getName());
					
					
				
			}
		}
		
	}

	private List<File> listItemsDir(List<File> list, File dir) {
		
		
		File[] files = dir.listFiles();
		
		
		for (int i = 0; i < files.length; i++) {
			if(files[i].isDirectory()) {
				listItemsDir(list, files[i]);
			}else {
				list.add(files[i]);
			}
		}
		return list;
		
		
	}
	
	
	
	public boolean rename(String name) {
		
		String fileName = file.getName();
		String path = file.getPath();
		
		int count = (path.length() - path.replaceAll(fileName, "").length()) / fileName.length();
		
		System.out.println(count);
		
		if(count != 1) {
			System.out.println("path has folders w/the same name");
			return false;
		}
		
		
		path = path.replace(fileName,"");
		
		System.out.println(path);
		
		File newFile = new File(path + "\\" + name);
		
		System.out.println(newFile.getAbsolutePath());
		
		
		file.renameTo(newFile);
		
		
		
		file = newFile;
		
		return true;
		
		
		
		
	}
	
	
	
	public File getFile() {
		return this.file;
	}
	
	
	
	
	
	public static void help() {
		
		
		System.out.println("\n\n\nMy own FileIO class. Made to throw no exceptions, and make editing files easier"
				
			+ 	"\n\n Description of all methods:"
			
			+	"\n\n FileIO(String): the string is the path to the file"
			+	"\n FileIO(File): using a File object as the file"
			+	"\n FileIO(Path): uses a Path object as the file"
				  
			+	"\n\n Write(String): clears file and writes String"
			+	"\n WriteArray(String[]): clears file and writes String[], line breaks between each String"
			+	"\n WriteList(List<String>): clears file and writes the List, line breaks between each String"
				  
			
			+   "\n\n replaceLine(Int, String): replace all text at line Int with String"
			+	"\n replaceText(String0, String1): replaces line String0 with String1"
			+	"\n\n addln(String): adds String to the file with a line break"
			+	"\n add(String): adds String to the file without a line break"
			+ 	"\n add(int): runs an Int through add(String)"
			+	"\n addln(int): runs an Int through addln(String)"
				  
			+	"\n\n print(): prints the file in the console"
			+	"\n printAll() prints all files inside the file on the console"
			
			+	"\n readList(): reads entire file, returns List<String>"
			+	"\n readString(): reads entire file, returns a String version of readList(), may need formating"
			
			+	"\n\n index(String): returns the smallest line number (int) that equals String. returns -1 if not found"
			+ 	"\n\n delete(): deletes the file"
			+ 	"\n create(): creates the file"
			
			+	"\n\n exists(): returns if a file exists"
			+	"\n pExists(): prints if a file exists, also returns the value"
			+	"\n isDir(): returns true is file is a dir"
			
			+	"\n\n listFiles(): returns an List<File> of all files in that file. does not include dirs"
			+	"\n listFilesArray(): returns an File[] version of listFiles()"
			
			+	"\n\n use(): a static method. use like: FileIO.use(File).*any other method*() actualy returns a FileIO file"
			
			
			+	"\n\n help(): prints the help message"
		
			
				 

			+	"\n	all FileReaders FileWriters, and BufferedWriters are closed after use. "
				
				
				
				
				
				
				
				
				);
		
		
		
		
		
		
		
	}
		
	
	
}
