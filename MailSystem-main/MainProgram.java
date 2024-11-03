import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The following file is the user side of the program that
 * handles the usage and input of files and user input.
 * @author Kiril Sikov
 *  Class: Comp 2631
 *  Assignment 1
 *  Main program
 */
public class MainProgram {
    static String fileName = "mail.txt"; //can be changed for a different text file

    /**
     * main begins the program, defines a MailSystem object and then
     * sends it to the rest of the program.
     * @param args default
     */
    public static void main(String[] args) {
        final Scanner keyboard = new Scanner(System.in);
        MailSystem mailList = new MailSystem();

        System.out.println("Welcome to the Secure Corp. messaging system" + "\n");
        System.out.print("Reading mail from " + fileName + "...");

        readFile(mailList);

        System.out.print(" done." + "\n");
        System.out.print("Enter your name (be honest): ");
        String name = keyboard.next();

        menu(name, mailList);

        keyboard.close();
        return;
    }

    /**
     * reads in a designated file and puts its contents into
     * a mailSystem object.
     * @param mailList the mailSystem object
     */
    public static void readFile(MailSystem mailList) {
        try {
            FileInputStream mailRead = new FileInputStream(fileName);
            Scanner fileRead = new Scanner(mailRead);
            mailList.readIn(fileRead, mailList);
            fileRead.close();
            mailRead.close();
        } catch (IOException noFile) {
            System.out.print("No File found");
            noFile.printStackTrace();
        }
        return;
    }

    /**
     * Once the file has been read in, the user is prompted for
     * their usage of the system.
     * @param name name of the user
     * @param mailList mailSystem object
     */
    public static void menu(String name, MailSystem mailList) {
        final Scanner menuKeyboard = new Scanner(System.in);
        int msgUnread = 0;
        int relMsgNumber = searchMessages(name, mailList);
        Message[] relaventMessages = new Message[relMsgNumber];
        mailList.copyMessages(mailList, name, relaventMessages);
        msgUnread = searchMessages(relaventMessages, msgUnread);
        System.out.println("Hello, " + name + ", you have " + msgUnread + " unread message(s).\n");
        System.out.println("Available Options");
        System.out.println("  L - List your messages");
        System.out.println("  V - View a message");
        System.out.println("  C - Compose a message");
        System.out.println("  S - Switch users");
        System.out.println("  Q - Quit \n");
        System.out.print("Please enter your choice: ");
        char menuChoice = menuKeyboard.next().charAt(0);

        choiceBranch(menuChoice, relaventMessages, mailList, menuKeyboard, name);

        menuKeyboard.close();

        return;
    }

    /**
     * choice branch passes in the choice the user made and enters branches determined
     * on the input (list, view, compose, switch user and quit program).
     * @param menuChoice the user choice
     * @param relaventMessages messsage array with relavent messages
     * @param mailList mailSystem object
     * @param menuKeyboard Scanner for future input
     * @param name name of the user
     */
    public static void choiceBranch(char menuChoice, Message[] relaventMessages, MailSystem mailList,
                                    Scanner menuKeyboard, String name) {
        if (menuChoice == 'L' || menuChoice == 'l') {
            System.out.println("Your messages are listed below: \n");
            printMessages(relaventMessages);
            menu(name, mailList);
        } else if (menuChoice == 'V' || menuChoice == 'v') {
            System.out.println("Please type in your message number");
            int msgNumber = menuKeyboard.nextInt();
            boolean found = searchmessage(mailList, msgNumber, name);
            if (found) {
                printMessages(mailList.getSpecificMessage(msgNumber - 1));
                if (mailList.getSpecificMessage(msgNumber - 1).getMsgStatus() == 'N') {
                    mailList.getSpecificMessage(msgNumber - 1).setMsgStatus('R');
                    mailList.writeToFile(fileName, mailList);
                }
            }
            else {
                System.out.println("Message was not found. Returning to menu screen");
                menu(name,  mailList);
            }
            menu(name, mailList);
        } else if (menuChoice == 'C' || menuChoice == 'c') {
            if (mailList.isFull()) {
                System.out.println("The message system is full. Please contact your system administrator.");
            }
            else {
                System.out.println("Who are you sending this message to?");
                String recieverID = menuKeyboard.next();
                System.out.println("What will be the subject line?");
                String subjectLine = menuKeyboard.next();
                System.out.println("Write your message below: ");
                String body = menuKeyboard.next();
                int msgNumber = guarenteeNewNumber(mailList);
                composeMessage(msgNumber, name, recieverID, subjectLine, body, mailList);
                mailList.writeToFile(fileName, mailList);
                menu(name, mailList);
            }
        } else if (menuChoice == 'S' || menuChoice == 's') {
            System.out.print("Okay, Please input your name (be honest):");
            String newName = menuKeyboard.next();
            menu(newName, mailList);
        } else if (menuChoice == 'Q' || menuChoice == 'q') {
            System.out.println("Have a nice day!");
        } else {
            System.out.println("That was not a valid entry, please try again.");
            menu(name, mailList);
        }
        return;
    }

    //returns a message number that is not copied
    private static int guarenteeNewNumber(MailSystem mailList) {
        int uniqueMsgNum = mailList.getFilledMessages();
        return uniqueMsgNum;
    }

    //looks through the message array for identical recieverID's in message objects
    private static int searchMessages(String name, MailSystem mailList) {
        int containedMessages = 0;
        for (int i = 0; i < mailList.getFilledMessages(); i++) {
            if (mailList.getSpecificMessage(i).getRecieverID().equals(name)) {
                containedMessages++;
            }
        }
        return containedMessages;
    }

    //looks through the message array for unread messages
    private static int searchMessages(Message[] relaventMessages, int msgUnread) {
        for (int i = 0; i < relaventMessages.length; i++) {
            if (relaventMessages[i].getMsgStatus() == 'N') {
                msgUnread++;
            }
        }
        return msgUnread;
    }

    /**
     * Using the passed in values, the function creates a message object and
     * adds it to the mailSystem message array.
     * @param msgNumber Passed in message number
     * @param name name of the user.
     * @param recieverID name of the recipient.
     * @param subjectLine header.
     * @param body text.
     * @param mailList mailSystem object.
     */
    private static void composeMessage(int msgNumber, String name, String recieverID,
                                       String subjectLine, String body, MailSystem mailList) {
        char msgStatus = 'N';
        Message newMessage = new Message(msgNumber, msgStatus, name, recieverID, subjectLine, body);
        mailList.addMessage(newMessage);
        return;
    }

    /**
     * searches through the mailSystem message array for recieverID(s) matching the passed in number.
     * @param mailList mailSystem object
     * @param msgNumber message number being searched
     * @param name ensures that the user doesnt see other's emails
     */
    private static boolean searchmessage(MailSystem mailList, int msgNumber, String name) {
        boolean msgFound = false;
        for (int i = 0; i < mailList.getFilledMessages(); i++) {
            if ((mailList.getSpecificMessage(i).getMsgNumber() == msgNumber) &&
                 (name.equals(mailList.getSpecificMessage(i).getRecieverID()))) {
                msgFound = true;
            }
        }
        return msgFound;
    }

    /**
     * Prints a message object's parameters to the user.
     * @param relaventMessage message object who's fields will print
     */
    public static void printMessages(Message relaventMessage) {
        System.out.println(relaventMessage.getMsgNumber() + " " + relaventMessage.getMsgStatus());
        System.out.println(relaventMessage.getSenderID());
        System.out.println(relaventMessage.getRecieverID());
        System.out.println(relaventMessage.getHeader());
        System.out.println(relaventMessage.getBody() + "\n" + "EOF");
        return;
    }

    /**
     * Prints an array of messages using the printMessages function.
     * @param relaventMessages array of message objects
     */
    private static void printMessages(Message[] relaventMessages) {
        for (int i = 0; i < relaventMessages.length; i++) {
            printMessages(relaventMessages[i]);
        }
        return;
    }
}