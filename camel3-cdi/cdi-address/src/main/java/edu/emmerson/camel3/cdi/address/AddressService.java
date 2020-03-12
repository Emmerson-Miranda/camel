package edu.emmerson.camel3.cdi.address;


import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Named;


@Named("addressService")
public class AddressService {

    // use a tree map so they become sorted
    private final Map<String, Address> address = new TreeMap<String, Address>();

    public AddressService() {
        address.put("123", new Address(123, "Line 1 of Cesar Vallejo's address."));
        address.put("456", new Address(456, "Line 1 of Ricardo Palma's address."));
        address.put("789", new Address(789, "Line 1 of Jose Maria Arguedas's address."));
    }


    /**
     * List all address
     *
     * @return the list of all address
     */
    public Collection<Address> listAddresses() {
        return address.values();
    }
    
    public Address getAddressByUserId(String userId) {
    	return address.get(userId);
    }

}
