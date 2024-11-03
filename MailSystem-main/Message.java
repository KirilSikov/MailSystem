/**
 * The following file defines the message object, with 6
 *  data fields and 2 functions.
 * @author Kiril Sikov
 *  Class: Comp 2631
 *  Assignment 1
 *  Message Class Defining
 */
public class Message {
    private int msgNumber;
    private char msgStatus;
    private String senderID;
    private String recieverID;
    private String header;
    private String body;

    /**
     * message initializes a message object.
     * @param number message number
     * @param status message read status
     * @param sender who the message is meant for
     * @param reciever who will get the message
     * @param subject title of the message
     * @param message content of the message
     */
    public Message(int number, char status, String sender, String reciever, String subject, String message) {
        this.msgNumber = number;
        this.msgStatus = status;
        this.senderID = sender;
        this.recieverID = reciever;
        this.header = subject;
        this.body = message;

        return;
    }

    public int getMsgNumber() {
        return msgNumber;
    }

    public void setMsgNumber(int msgNumber) {
        this.msgNumber = msgNumber;
    }

    public char getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(char msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(String recieverID) {
        this.recieverID = recieverID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
