package ar.com.bia.config;

/*
<?php
error_reporting(-1);
ini_set("display_errors", 1);
require_once($_SERVER['DOCUMENT_ROOT']."/wordpress/wp-load.php");
$password = 'target123';
//if( wp_check_password("123",wp_hash_password("123"))){
if( wp_check_password($_GET["plain"],$_GET["pass"])){
echo "OK";}
else{
echo "NO!!!";
}
?>
 */

import ar.com.bia.services.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;


public class WordPressAuthenticationProvider implements AuthenticationProvider {


    private UserService userService;

    public WordPressAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (userService.loginUser(name,password)) {

            return new UsernamePasswordAuthenticationToken(
                    name, password, new ArrayList<>());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
