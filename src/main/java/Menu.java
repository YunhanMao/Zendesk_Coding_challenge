import org.json.JSONObject;

import java.util.Scanner;

//this class is responsible for menu printing and input processing
public class Menu {
    //reading input
    public String readInput(){
        Scanner sc = new Scanner(System.in);
        return sc.next();
    }
    //print the subMenu
    public void subMenu(){
        while(true){
            System.out.println("\n\t Select view options:");
            String[] menuOptions = {"\t* Press 1 to view all tickets","\t* Press 2 to view a tickets","\t* Type 'quit' to exit"};
            for (String menuOption : menuOptions) {
                System.out.println(menuOption);
            }
            String input = readInput();
            //different based on different response
            switch (input) {
                case "1":
                    multiCase();
                    break;
                case "2":
                    singleCase();
                    break;
                case "quit":
                    System.out.println("Thanks for using the viewer. Goodbye.");
                    return;
                default:
                    System.out.println("Invalid input. Please try again");
                    break;
            }
        }
    }
    //print the mainMenu
    public void mainMenu(){
        System.out.println("Welcome to the ticket viewer");
        System.out.println("Type 'menu' to view options or 'quit' to exit");
        String input = readInput();
        while(true){
            //different based on different response
            if(input.equals("menu")){
                subMenu();
                return;
            }else if(input.equals("quit")){
                System.out.println("Thanks for using the viewer. Goodbye.");
                return;
            }else{
                System.out.println("Invalid input. Please try again");
            }
            input = readInput();
        }
    }

    public void multiCase(){
        JSONObject ticketsJSON;
        try{
            APIConnect handler = new APIConnect();
            ticketsJSON = handler.getAllTickets();
        }catch(Exception e){
            System.out.println("Could not get your tickets. There is something wrong with API access.");
            return;
        }
        int pageNum = 1;
        Ticket ticket = new Ticket();
        pageNum = ticket.printMultiTickets(ticketsJSON, pageNum);
        label:
        while(true){
            //different based on different response
            String input = readInput();
            switch (input) {
                case "quit":
                case "menu":
                    break label;
                case "next":
                    pageNum++;
                    pageNum = ticket.printMultiTickets(ticketsJSON, pageNum);
                    break;
                case "prev":
                    pageNum--;
                    pageNum = ticket.printMultiTickets(ticketsJSON, pageNum);
                    break;
                default:
                    System.out.println("Invalid command, enter 'next' for next page, enter 'prev' for previous page,enter 'menu' to return, enter 'quit' to exit the viewer");
                    break;
            }
        }
    }
    //simple look up
    public void singleCase(){
        JSONObject ticketsJSON;
        System.out.println("Enter ticket number:");
        String input = readInput();
        try{
            APIConnect handler = new APIConnect();
            ticketsJSON = handler.getSingleTicket(input);
        }catch(Exception e){
            System.out.println("Could not find ticket with id equals to "+ input +". Please try again.");
            return;
        }
        Ticket ticket = new Ticket();
        ticket.printSingleTicket(ticketsJSON);
    }
}
