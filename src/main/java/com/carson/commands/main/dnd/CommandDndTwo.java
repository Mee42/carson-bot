package com.carson.commands.main.dnd;

import java.util.ArrayList;
import java.util.List;

import com.carson.commandManagers.Command;
import com.carson.commandManagers.ICommand;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class CommandDndTwo  extends Command implements ICommand{

	private DndObject d;
	
	public CommandDndTwo(IDiscordClient c, DndObject d) {
		super(c);
		this.d = d;
	}

	@Override
	public boolean test(MessageReceivedEvent event) {
		return d.v.verify(event);
	}

	@Override
	public void run(MessageReceivedEvent event) {
		String text = event.getMessage().getContent();
		try {
			int[] numbers = new int[] { 
					Integer.parseInt(text.split("d")[0]),
					Integer.parseInt(text.split("d")[1])
			};
			
			int total = 0;
			List<Integer> number = new ArrayList<Integer>();
		
			for(int i1 = 0;i1<numbers[1];i1++) {
				int temp1 = (int)(Math.random()*numbers[0]+1);
				number.add(temp1);
				total+=temp1;
			}
			
			System.out.println(total);
			sendMessage(event.getChannel(),"You got:" + total);
			
			d.v.setActive(false);
			return;
		}catch (ArrayIndexOutOfBoundsException e) {
			sendMessage(event, "I can't proccess that, please fix your input");
		}catch (NumberFormatException ef) {
			sendMessage(event, "please fix your inputs and try again");
		}
		d.v.setActive(false);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisciption() {
		// TODO Auto-generated method stub
		return null;
	}

}
