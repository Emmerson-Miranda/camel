package edu.emmerson.camel3.cdi.address;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents an user Address")
public class Address {

    private int userId;
    private String line1;

    public Address() {
    }

    public Address(int id, String name) {
        this.userId = id;
        this.line1 = name;
    }

    @ApiModelProperty(value = "The userId of the user", required = true)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    @ApiModelProperty(value = "The line1 of the address", required = true)
    public String getLine1() {
        return line1;
    }

    public void setLine1(String name) {
        this.line1 = name;
    }
}