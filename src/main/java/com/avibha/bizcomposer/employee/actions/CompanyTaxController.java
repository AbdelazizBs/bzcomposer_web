package com.avibha.bizcomposer.employee.actions;

import com.avibha.common.utility.Path;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CompanyTaxController {
    @RequestMapping(value = {"/companytax"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forward = "/employee/companytax";
        Path p = new Path();
        p.setPathvalue(request.getContextPath());
        request.getSession().setAttribute("path", p);
        if (request.getSession().isNew()
                || ((String) request.getSession().getAttribute("CID")) == null
                || ((Path) request.getSession().getAttribute("path")) == null) {
            forward = "Expired";
        } else {
            forward = "/employee/companytax";
        }
        return forward;
    }
}
