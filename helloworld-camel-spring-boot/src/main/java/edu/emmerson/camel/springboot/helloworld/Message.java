package edu.emmerson.camel.springboot.helloworld;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Message entity
 *
 */
@XmlRootElement
public class Message {

    private String text;

    public Message() {
    }

    public Message(String txt) {
        this.text = txt;
    }

    public String getText() {
        return text;
    }

    public void setText(String txt) {
        this.text = txt;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
