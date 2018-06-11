/**
 * Copyright (C) Zoomdata, Inc. 2012-2018. All rights reserved.
 */
package com.sandbox.filestorage.controller.page;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/login")
public class LoginPageController {



    @RequestMapping(value = "", method = GET)
    public String startPage(
            @RequestParam(required = false,
                    name = "login_error", defaultValue = "false") boolean loginError,
            @RequestParam(required = false,
                    name = "skip_registration", defaultValue = "false") boolean skipRegistration) {

        System.out.println("ds");
        return "login";
    }

}
