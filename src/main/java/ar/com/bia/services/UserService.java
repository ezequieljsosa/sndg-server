package ar.com.bia.services;

import ar.com.bia.backend.dao.impl.UserRepositoryImpl;
import ar.com.bia.entity.UserDoc;
import com.mashape.unirest.http.Unirest;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class UserService {


    private DataSource dataSource;


    private Map<String, Object> wpData;


    private UserRepositoryImpl userRepository;

    public UserService(DataSource dataSource, Map<String, Object> wpData, UserRepositoryImpl userRepository) {
        this.dataSource = dataSource;
        this.wpData = wpData;
        this.userRepository = userRepository;
    }

    public boolean loginUser(String user, String pass) {
        try {
            UserDoc userdoc = this.findUser(user);
//            String url = wpData.get("wplogin_url").toString() +
//                    "?plain=" + pass + "&pass=" + userdoc.getPassword();
//            System.out.println(url);
//            String res = Unirest.get(url).asString().getBody();
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
        if (dataSource != null) {
            Connection conn = null;
            String userPass = "";
            String institutions = "";
            String userEmail = "";
            String rid = null;
            try {
                try {
                    conn = dataSource.getConnection();

                    String sql = "SELECT u.ID,u.user_pass,u.user_email,m.meta_value as institutions " +
                            "FROM wp_users u,wp_usermeta m " +
                            "WHERE  m.meta_key = 'institutions' AND u.ID = m.user_id  AND u.user_login = ? ";



                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, userName);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        userPass = rs.getString("user_pass");
                        userEmail = rs.getString("user_email");

                        institutions = rs.getString("institutions");
                        rid = rs.getString("id");
                    }
                    rs.close();
                    ps.close();
                } finally {
                    if (conn != null) {
                        conn.close();
                    }
                }
            } catch (SQLException sql) {
                throw new RuntimeException(sql);
            }
            UserDoc userDoc = new UserDoc();
            userDoc.setId(rid);
            userDoc.setName(userName);
            userDoc.setUsername(userName);
            userDoc.setEmail(userEmail);
            userDoc.setInstitutions(institutions);
            userDoc.setAuthId(rid);
            userDoc.setPassword(userPass);
            return userDoc;
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
            publicUser.setAuthId(UserDoc.publicUserId.toString());
            organisms.addAll(this.userRepository.organisms(publicUser));
        }

        return organisms;

    }

}

