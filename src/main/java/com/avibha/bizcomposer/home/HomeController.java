package com.avibha.bizcomposer.home;

import com.avibha.bizcomposer.login.dao.LoginDAOImpl;
import com.avibha.bizcomposer.login.forms.LoginForm;
import com.avibha.common.utility.MyUtility;
import com.avibha.common.utility.Path;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homepage2(HttpServletRequest request){
        System.out.println("index page called");
        String companyID = "1";
        ArrayList<LoginForm> list = LoginDAOImpl.getCompanyDetails(companyID, request);
        request.setAttribute("acList", list);
        request.setAttribute("currentLocale", LocaleContextHolder.getLocale().getLanguage());
        return "/index";
    }

    @GetMapping("/BzComposer")
    public String BzComposer(){
        System.out.println("BzComposer page called");
        return "/bzComposer";
    }

    @GetMapping("/aboutUS")
    public String aboutUS(){
        System.out.println("aboutUS page called");
        return "/company";
    }

    @GetMapping("/existingCompetitors")
    public String existingCompetitors(){
        System.out.println("existingCompetitors page called");
        return "/existingCompetitors";
    }

    @GetMapping("/ourServices")
    public String ourServices(){
        System.out.println("ourServices page called");
        return "/ourServices";
    }

    @GetMapping("/industries")
    public String industries(){
        System.out.println("industries page called");
        return "/industries";
    }

    @GetMapping("/features")
    public String features(){
        System.out.println("features page called");
        return "/features";
    }

    @GetMapping("/products")
    public String products(){
        System.out.println("products page called");
        return "/products";
    }

    @GetMapping("/contactUs")
    public String contactUs(){
        System.out.println("contactUs page called");
        return "/contactUs";
    }
}
