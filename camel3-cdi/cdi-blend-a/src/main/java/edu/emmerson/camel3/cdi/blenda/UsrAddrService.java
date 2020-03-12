package edu.emmerson.camel3.cdi.blenda;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Named;

import org.apache.camel.BeanInject;

import edu.emmerson.camel3.cdi.address.Address;
import edu.emmerson.camel3.cdi.address.AddressService;
import edu.emmerson.camel3.cdi.user.User;
import edu.emmerson.camel3.cdi.user.UserService;


@Named("usrAddrService")
public class UsrAddrService {
	
	@BeanInject("addressService")
	private AddressService addressService;
	
	@BeanInject("userService")
	private UserService userService;


    /**
     * List all users
     *
     * @return the list of all users
     */
    public Collection<UsrAddr> listUsrAddr() {
    	Map<String, UsrAddr> res = new TreeMap<String, UsrAddr>();
    	
    	Collection<User> users = userService.listUsers();
    	Iterator<User> it = users.iterator();
    	while(it.hasNext()) {
    		User u = it.next();
    		Address a = addressService.getAddressByUserId("" + u.getId());
    		
    		UsrAddr o = new UsrAddr(u.getId(), u.getName(), a.getLine1());
    		res.put(""+ u.getId(), o);
    	}
    	
        return res.values();
    }

}
