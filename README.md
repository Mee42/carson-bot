# carson-bot
a bot made for just general commands
written in java, using Discord4J
I'm still learning Java, so my code isn't the greatest.... 


The CarsonBot class contains all of the event handlers, currently only 
MessageReceivedEvent, and after running a couple checks sends it to the 
register.

the register then sends it to each subRegister, which sends it to each 
ICommand.

a command extends Command and implements ICommand
ICommand has a couple methods:

boolean test(event)
void run(event)
String getName()
String getDiscription()


test() should return true if the command should be called
run() runs the command
the two get() methods return the name and discription. this is mainly 
for the help method, which isn't hardcoded

setting the name to "hidden" causes it to not show up in the help menu





