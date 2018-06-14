package ar.com.bia.services;

import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.entity.UserDoc;

import java.math.BigInteger;
import java.util.List;


public class UserService {


    private UserRepositoryImpl userRepository;

    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public boolean loginUser(String user, String pass) {
        try {
            UserDoc userdoc = this.findUser(user);
//            String res = Unirest.get(wpData.get("wplogin_url").toString() +
//                    "?plain=" + pass + "&pass=" + userdoc.getPassword()).asString().getBody();
//            return res.equals("OK");


            return userdoc.getPassword().substring(userdoc.getPassword().length() - 10).equals(pass.substring(pass.length() - 10));
        } catch (Exception ex) {
            throw new RuntimeException("Login error", ex);


        }

    }

    public UserDoc findUser(String userName) {
        if (userName.equals("demo")) {
            UserDoc udoc = new UserDoc();
            udoc.setId(UserDoc.publicUserId.toString());
            udoc.setEmail("");
            udoc.setUsername(UserDoc.publicUserName);
            udoc.setName(UserDoc.publicUserName);
            udoc.setPassword("");
            return udoc;
        }


        return this.userRepository.findUser(userName);
    }

    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }

    public List<String> organisms(UserDoc user) {
        List<String> organisms = this.userRepository.organisms(user);
        if (!user.getAuthId().equals(UserDoc.publicUserId)) {
            UserDoc publicUser = new UserDoc();
            publicUser.setAuthId(UserDoc.publicUserId);
            organisms.addAll(this.userRepository.organisms(publicUser));
        }

        return organisms;

    }

}

