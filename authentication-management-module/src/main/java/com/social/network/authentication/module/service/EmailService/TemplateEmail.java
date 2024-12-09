package com.social.network.authentication.module.service.EmailService;

import lombok.Getter;
import lombok.Setter;

public class TemplateEmail {
    private final String requestMethod = null;
    private final String requestUrl = null;
    private String pageHTML;
    @Getter @Setter
    private TemplateEmailInformation templateEmail;

    public TemplateEmail(TokenTemplate token) {
        this.templateEmail = getTokenAccountByPageName(token.getAboutToken());
        this.pageHTML= getHTML(token.getTokenUUID(), null, templateEmail);
    }

    public TemplateEmail(TokenTemplate token, String code) {
        this.templateEmail = getTokenAccountByPageName(token.getAboutToken());
        this.pageHTML= getHTML(token.getTokenUUID(),code, templateEmail);
    }


    public TemplateEmailInformation getTokenAccountByPageName(String tokenName) {
        for (TemplateEmailInformation templateEmailInformation : TemplateEmailInformation.values()) {
            if (templateEmailInformation.name().equals(tokenName)) {
                return templateEmailInformation;
            }
        }
        return null;
    }


    private String getHTML(String token, String code, final TemplateEmailInformation pageParam){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>"+ pageParam.getSubject() +"</title>\n" +
                "    <style type=\"text/css\">\n" +
                "        .body{\n" +
                "            margin: 0px;\n" +
                "            background-color: #F5F5F5;\n" +
                "        }\n" +
                "        #titleDiv00001{\n" +
                "            position: relative;\n" +
                "            margin: 40px 5vw 10px 5vw;\n" +
                "        }\n" +
                "\n" +
                "        #title00001{\n" +
                "            font-size: 35px;\n" +
                "        }\n" +
                "\n" +
                "        #buttonDiv00001{\n" +
                "            height: 50px;\n" +
                "            width: 200px;\n" +
                "            margin: 35px 0px 0px 6vw;\n" +
                "\n" +
                "        }\n" +
                "        .textDiv00001 {\n" +
                "            width: 300px;\n" +
                "            margin: 0px 0px 0px 7vw;\n" +
                "        }\n" +
                "\n" +
                "        .textToken00001 {\n" +
                "            width: 1500px;\n" +
                "            margin-top: 0;\n" +
                "            margin-bottom: 5px;\n" +
                "            margin-left: 8vw;\n" +
                "        }\n" +
                "\n" +
                "        .textURL00001 {\n" +
                "            width: 300px;\n" +
                "            margin-top: 0;\n" +
                "            margin-bottom: 5px;\n" +
                "            margin-left: 5vw;\n" +
                "        }\n" +
                "\n" +
                "        #confirm00001 {\n" +
                "            height: 100%;\n" +
                "            width: 100%;\n" +
                "            border-radius: 50px;\n" +
                "            background-color: #ffffff;\n" +
                "            border: 0;\n" +
                "        }\n" +
                "\n" +
                "        .borrowDiv00001{\n" +
                "            height: 60px;\n" +
                "            width: 300px;\n" +
                "        }\n" +
                "\n" +
                "        .urlToken {\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "\n" +
                "        .text{\n" +
                "            font-family: \"Minion Pro\";\n" +
                "            color: #62767F;\n" +
                "            font-size: 15px;\n"+
                "        }\n" +
                "        .bottomURLBLOCK {\n" +
                "            margin-top: 50px;\n" +
                "        }\n" +
                "\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"body\">\n" +
                "    <div class=\"borrowDiv00001\">\n" +
                "\n" +
                "    </div>\n" +
                "    <div id=\"titleDiv00001\">\n" +
                "         <p id=\"title00001\">"+ pageParam.getSubject() + "</p>\n" +
                "    </div>\n" +
                "    <div id=\"textToken\" class=\"textDiv00001\">\n" +
                "        <p class=\"text\">\n" +
                pageParam.getMessage() + code +
                "        </p>\n" +
                "    </div>\n" +
                "    <form method=\""+ requestMethod +"\" action=\"" +pageParam.getUrl()+"\" target = \"_self\">\n" +
                "        <div id=\"buttonDiv00001\" class=\"buttonDiv\">\n" +
                "\t\t\t<button id=\"confirm00001\"  type=submit name=\"token\" value=\""+ token +"\"><em>GO</em></button>\n" +
                "        </div>\n" +
                "    </form>\n" +
                "    <div class=\"borrowDiv00001 bottomURLBLOCK\">\n" +
                "        <p class=\"textURL00001\">If button not work you go to this url </p>\n" +
                "        <p class=\"textToken00001\" align=\"left\">\n" +
                "            <a class=\"urlToken text\" href=\""+ requestUrl + pageParam.getUrl() + "?token="+ token +"\">Let's go action </a>\n" +
                "        </p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getPageHTML() {
        return pageHTML;
    }

    public enum TemplateEmailInformation {
        CONFIRM_ACCOUNT_TOKEN("To confirm registration, click...<br>\n" +
                "Welcome to bombulis messenger 🔥",
                "/token/registration","Confirm account"),
        RESET_PASSWORD_TOKEN("You reset your password, click...<br>\n"+
                "If reset not you lock your account...",
                "/token/password","Reset password"),
        UPDATE_ACCOUNT_DATA_TOKEN("404", "/404","Confirm!!!\n Update your account data"),
        ACCOUNT_LOCK_TOKEN("If you don't follow these actions,\n block your account by clicking on this link"
                ,"Написть", "Waring"),
        LOGIN_BY_CODE("Hello your code for login ", "/login/token", "Code for login"),
        UPDATE_EMAIL_CODE("Enter code for confirm update your email ", "", "Code for confirm update email"),
        UPDATE_EMAIL_CONFIRM("Go to url for confirm new email", "/token/email/update", "Confirm new email"),
        ACCOUNT_UNLOCK_TOKEN("Your account is locked, ","/token/unlock","Your account is locked"),
        REGISTRATION_CONFIRM("To confirm registration, click...<br>\n" +
                "Welcome to bombulis messenger 🔥",
                null,"Confirm account enter this code:");

        private final String url;
        private final String subject;
        @Setter
        private  String message;
        TemplateEmailInformation(String message, String url, String subject) {
            this.message = message;
            this.url = url;
            this.subject = subject;
        }

        public String getMessage() {
            return message;
        }

        public String getUrl() {
            return url;
        }

        public String getSubject() {
            return subject;
        }
    }
}
