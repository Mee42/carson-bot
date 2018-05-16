package com.carson.commands.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.carson.classes.FileIO;
import com.carson.classes.PhotoStream;
import com.carson.commandManagers.*;

import com.google.gson.*;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandXKCD extends Command implements ICommand{

	public CommandXKCD(IDiscordClient c) {
		super(c);
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return event.getMessage().getContent().startsWith("~xkcd");
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String[] args= event.getMessage().getContent().split(" ");
		if(args.length == 1) {
			try {
				runNoArgs(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}else {
			try {
				int i = Integer.parseInt(args[1]);
				
				runWithInt(event, i);
				
			}catch (Exception e) {
				sendMessage(event, "there was a problem processing your request.");
			}
			
		}
	}
	
	
	private void runNoArgs(MessageReceivedEvent event) throws Exception{
		runWithInt(event, (int)(Math.random() * 1991 + 1));
	}

	private void runWithInt(MessageReceivedEvent event, int number) throws JsonIOException{

		int no =(int)(Math.random() * 1800 + 100);
		
		FileIO file = new FileIO("/home/carson/discord/files/xkcd/" + no);
		FileIO alt = new FileIO("/home/carson/discord/files/xkcd/alt/" + no);
		
		if(file.exists()) {
			try {
				file.rename(no + ".png");
				event.getChannel().sendFile(file.getFile());
				file.rename(no + "");
				sendMessage(event, '`' + alt.readList().get(0) + '`');
			} catch (FileNotFoundException e) {
				sendMessage(event, "tryed to find a file we know exists. problem");
				System.out.println("ERROR: missread a file");
			}
			
			return;
		}
		
		file.create();
		List<String> pics;
		try {
			pics = PhotoStream.getUrl("https://www.xkcd.com/" + no  );
		        
				for(String pic : pics) {
					if(pic.startsWith("https://imgs.xkcd.com/comics")){
						
						saveImage(pic, file.getPath());
			        	String altText = parse("http://www.xkcd.com/" + no + "/info.0.json", "alt");
			        	alt.create();
			        	alt.write(altText);
					}
		        	
				}
				
		} catch (IOException e1) {
			System.out.println("ERROR: running ~xkcd threw a IOExecption");
			e1.printStackTrace();
		}
		try {
			file.rename(no + ".png");
			event.getChannel().sendFile(file.getFile());
			file.rename(no + "");
			sendMessage(event, '`' + alt.readList().get(0) + '`');
		} catch (FileNotFoundException e) {
			sendMessage(event, "tryed to find a file we know exists. problem");
			System.out.println("ERROR: missread a file");
		}
		return ;
		
		
	}
	
	public void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

	@Override
	public String getName() {
		return  "~xkcd";
	}

	@Override
	public String getDisciption() {
		return "posts a random xkcd comic";
	}

	//used to parse the json files for alt text
	private  String parse(String web, String get) throws IOException{
	    URL url = new URL(web);
	    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	    request.connect();

	    JsonParser jp = new JsonParser();
	    
	    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
	    
	    JsonObject rootobj = root.getAsJsonObject(); 
	    
	    String res = rootobj.get(get).getAsString();
	    
	    return res;
}
	
}
