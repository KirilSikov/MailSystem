import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 *  Below is the mailSystem class, which defines a mailSystem object
 *  that contains a message object array and an integer that counts how
 *  many messages exist in the object, as well as the methods that make
 *  use of both.
 * @author Kiril Sikov
 *  Class: Comp 2631
 *  Assignment 1: Mail System
 */
public class MailSystem {
    private Message[] allMail = new Message[500];
    private int filledMessages = 0;

    public Message[] getAllMail() {
        return allMail;
    }

    /**
     * setMessage takes in an initialized message object and assigns it values before
     * returning the filled object.
     * @param number Message number
     * @param status Message read status
     * @param sender Who sent it
     * @param reciever Who will receive it
     * @param subject Header
     * @param body Actual body text
     * @param message object that gets the above values
     */
    public Message setMessage(int number, char status, String sender, String reciever,
                              String subject, String body, Message message) {
        message.setMsgNumber(number);
        message.setMsgStatus(status);
        message.setSenderID(sender);
        message.setRecieverID(reciever);
        message.setHeader(subject);
        message.setBody(body);

        return message;
    }

    /**
     * This method initializes a message in the mailSystem object array.
     */
    public Message initializeMessage() {
        this.allMail[filledMessages] = new Message(0, '0', null, null, null, null);
        return this.allMail[filledMessages];
    }

    /**
     * addMessage adds a message object to the mailSystem message array at the end.
     * @param newMessage message object to be added to the mailSystem message array.
     */
    public void addMessage(Message newMessage) {
        this.allMail[filledMessages] = initializeMessage();
        this.allMail[filledMessages] = setMessage(newMessage.getMsgNumber(), newMessage.getMsgStatus(),
                newMessage.getSenderID(), newMessage.getRecieverID(), newMessage.getHeader(),
                newMessage.getBody(), this.allMail[filledMessages]);
        this.filledMessages = this.incramentMsgCount();
    }

    public int getFilledMessages() {
        return filledMessages;
    }

    public void setFilledMessages(int filledMessages) {
        this.filledMessages = filledMessages;
    }

    /**
     * Increments the number of "filled messages" that exist inside the array.
     */
    public int incramentMsgCount() {
        this.filledMessages = filledMessages + 1;
        return this.filledMessages;
    }

    /**
     * Receives a message in a specific index and returns it.
     */
    public Message getSpecificMessage(int msgInArray) {
        return this.allMail[msgInArray];
    }

    /**
     * copyMessages takes in the mailSystem object and searches through the
     * messages looking for a matching name. Once this is done, it condenses
     * mailList down to an array of messages that share a recieverID.
     * @param mailList mailSystem object
     * @param name String that will be used to search mailSystem
     * @param relaventMessages a simple message object array to be filled
     * @returns an array of message objects
     */
    public Message[] copyMessages(MailSystem mailList, String name, Message[] relaventMessages) {
        int msgNum = 0;
        for (int i = 0; i < filledMessages; i++) {
            if (this.allMail[i].getRecieverID().equals(name)) {
                relaventMessages[msgNum] = new Message(0, '0', null, null, null, null);
                relaventMessages[msgNum] = setMessage(this.allMail[i].getMsgNumber(), this.allMail[i].getMsgStatus(),
                        this.allMail[i].getSenderID(), this.allMail[i].getRecieverID(), this.allMail[i].getHeader(),
                        this.allMail[i].getBody(), this.allMail[i]);
                msgNum++;
            }
        }
        return relaventMessages;
    }

    /**
     * writeToFile takes in a mailSystem object and a String that
     * defines the name of the file to be written and writes the contents
     * of the message array into the file.
     * @param fileName the file name for the write
     * @param mailList mailSystem object thats contents will be written
     */
    public void writeToFile (String fileName, MailSystem mailList) {
        try {
            FileOutputStream fileOutput = new FileOutputStream(fileName);
            BufferedWriter writeOutput = new BufferedWriter(new OutputStreamWriter(fileOutput));
            for (int i = 0; i < mailList.getFilledMessages();i++) {
                writeOutput.write(mailList.getSpecificMessage(i).getMsgNumber() + ' ' +
                                  mailList.getSpecificMessage(i).getMsgStatus() + "\n");
                writeOutput.write(mailList.getSpecificMessage(i).getSenderID() + "\n");
                writeOutput.write(mailList.getSpecificMessage(i).getRecieverID() + "\n");
                writeOutput.write(mailList.getSpecificMessage(i).getHeader() + "\n");
                writeOutput.write(mailList.getSpecificMessage(i).getBody() + "\n");
                writeOutput.write("EOF \n");
            }
            writeOutput.close();
        } catch (IOException fileProblem) {
            fileProblem.printStackTrace();
        }
    }

    /**
     * readIn() takes a scanner and a mailSystem object and places the
     * contents of the scanner into the file.
     * @param fileRead Scanner that contains the file to be read in
     * @param mailList mailSystem object whose array will be filled
     */
    public void readIn(Scanner fileRead, MailSystem mailList) {
        String senderID;
        String recieverID;
        while (fileRead.hasNext()) {
            final int msgNumber = fileRead.nextInt();
            String line = fileRead.nextLine();
            final char chr = line.charAt(1);
            senderID = fileRead.nextLine();
            recieverID = fileRead.nextLine();
            String header = fileRead.nextLine();
            String body = fileRead.nextLine();
            if (body.equals("EOF")) { //deals with edge case of no body in email
                mailList.setMessage(msgNumber, chr, senderID, recieverID, header, "", mailList.initializeMessage());
                mailList.incramentMsgCount();
            }
            else {
                String addOn = fileRead.nextLine();
                while (!addOn.equals("EOF")) {
                    body = body + "\n" + addOn;
                    addOn = fileRead.nextLine();
                }
                mailList.setMessage(msgNumber, chr, senderID, recieverID, header, body, mailList.initializeMessage());
                mailList.incramentMsgCount();
            }
        }
        return;
    }

    /**
     * checks to see if the array has reached its end. Returns
     * true if the number off messages hits fivehunded (500-1)
     * because of offset
     */
    public boolean isFull() {
        boolean isfull = false;
        if (this.getFilledMessages() >= 499) {
            isfull = true;
        }
        return isfull;
    }
}
