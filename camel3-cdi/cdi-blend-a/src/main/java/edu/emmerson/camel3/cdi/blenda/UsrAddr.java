package edu.emmerson.camel3.cdi.blenda;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents an user of the system")
public class UsrAddr {

    private int id;
    private String name;
    private String line1;

    public UsrAddr() {
    }

    public UsrAddr(int id, String name, String line1) {
        this.id = id;
        this.name = name;
        this.setLine1(line1);
    }

    @ApiModelProperty(value = "The id of the user", required = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ApiModelProperty(value = "The name of the user", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}
}