package ar.com.bia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.entity.UserDoc;

@Service
public class UserService {


	@Autowired
	private UserRepositoryImpl userRepository;
	
	public UserDoc findUser(String userName){
		return this.userRepository.findUser(userName);
	}
	
	public List<String> organisms(UserDoc user){
		List<String> organisms = this.userRepository.organisms(user);
		if (!user.getAuthId().equals(UserDoc.publicUserId)){
			UserDoc publicUser = new UserDoc();
			publicUser.setAuthId(UserDoc.publicUserId);
			organisms.addAll(this.userRepository.organisms(publicUser));
		}
		
		return organisms;
		
	}
	
}
