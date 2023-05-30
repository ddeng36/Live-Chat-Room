package team.shared;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String sender;
    private String receiver;
    private String content;
    private String sentTime;
    private String type;

    private  byte[] fileBytes;
    private int fileLen = 0;
    private String dest;
    private String source;


    public byte[] getFileBytes() {

        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {

        this.fileBytes = fileBytes;
    }

    public int getFileLen() {

        return fileLen;
    }

    public void setFileLen(int fileLen) {

        this.fileLen = fileLen;
    }

    public String getDest() {

        return dest;
    }

    public void setDest(String dest) {

        this.dest = dest;
    }

    public String getSource() {

        return source;
    }

    public void setSource(String source) {

        this.source = source;
    }

    public String getSender() {

        return sender;
    }

    public void setSender(String sender) {

        this.sender = sender;
    }

    public String getReceiver() {

        return receiver;
    }

    public void setReceiver(String receiver) {

        this.receiver = receiver;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public String getSentTime() {

        return sentTime;
    }

    public void setSentTime(String sentTime) {

        this.sentTime = sentTime;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }
}
