package org.mkgroup.zaga.authorizationservice.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailNotification {

	@Autowired
	JavaMailSender javaMailSender;
	
	private static final Logger logger = LoggerFactory.getLogger(MailNotification.class);
	
	public void sendEmail(String mail, String pass) throws MessagingException, IOException {
		logger.info("Sending mail");
        MimeMessage msg = javaMailSender.createMimeMessage();
        
        String htmlMsg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
				"<html lang=\"sr\">\r\n" + 
				"  \r\n" + 
				"  <head>\r\n" + 
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
				"    <title>MK IT BS</title>\r\n" + 
				"  </head>\r\n" + 
				"  \r\n" + 
				"  <body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\"\r\n" + 
				"  style=\"margin: 0pt auto; padding: 0px; background:#F4F7FA;\">\r\n" + 
				"    <table id=\"main\" width=\"100%\" height=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\r\n" + 
				"    bgcolor=\"#F4F7FA\">\r\n" + 
				"      <tbody>\r\n" + 
				"        <tr>\r\n" + 
				"          <td valign=\"top\">\r\n" + 
				"            <table class=\"innermain\" cellpadding=\"0\" width=\"580\" cellspacing=\"0\" border=\"0\"\r\n" + 
				"            bgcolor=\"#F4F7FA\" align=\"center\" style=\"margin:0 auto; table-layout: fixed;\">\r\n" + 
				"              <tbody>\r\n" + 
				"                <!-- START of MAIL Content -->\r\n" + 
				"                <tr>\r\n" + 
				"                  <td colspan=\"4\">\r\n" + 
				"                    <!-- Logo start here -->\r\n" + 
				"                    <table class=\"logo\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n" + 
				"                      <tbody>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td colspan=\"2\" height=\"30\"></td>\r\n" + 
				"                        </tr>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td valign=\"top\" align=\"center\">\r\n" + 
				"                            <a href=\"localhost:4200/home\" style=\"display:inline-block; cursor:pointer; text-align:center;\">\r\n" + 
				"                            </a>\r\n" + 
				"                          </td>\r\n" + 
				"                        </tr>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td colspan=\"2\" height=\"30\"></td>\r\n" + 
				"                        </tr>\r\n" + 
				"                      </tbody>\r\n" + 
				"                    </table>\r\n" + 
				"                    <!-- Logo end here -->\r\n" + 
				"                    <!-- Main CONTENT -->\r\n" + 
				"                    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"#ffffff\"\r\n" + 
				"                    style=\"border-radius: 4px; box-shadow: 0 2px 8px rgba(0,0,0,0.05);\">\r\n" + 
				"                      <tbody>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td height=\"40\"></td>\r\n" + 
				"                        </tr>\r\n" + 
				"                        <tr style=\"font-family: -apple-system,BlinkMacSystemFont,&#39;Segoe UI&#39;,&#39;Roboto&#39;,&#39;Oxygen&#39;,&#39;Ubuntu&#39;,&#39;Cantarell&#39;,&#39;Fira Sans&#39;,&#39;Droid Sans&#39;,&#39;Helvetica Neue&#39;,sans-serif; color:#4E5C6E; font-size:14px; line-height:20px; margin-top:20px;\">\r\n" + 
				"                          <td class=\"content\" colspan=\"2\" valign=\"top\" align=\"center\" style=\"padding-left:90px; padding-right:90px;\">\r\n" + 
				"                            <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" bgcolor=\"#ffffff\">\r\n" + 
				"                              <tbody>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td align=\"center\" valign=\"bottom\" colspan=\"2\" cellpadding=\"3\">\r\n" + 
				"                                    <img alt=\"Coinbase\" width=\"80\" src=\"https://www.coinbase.com/assets/app/icon_email-e8c6b940e8f3ec61dcd56b60c27daed1a6f8b169d73d9e79b8999ff54092a111.png\"\r\n" + 
				"                                    />\r\n" + 
				"                                  </td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td height=\"30\" &nbsp;=\"\"></td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td align=\"center\"> <span style=\"color:#48545d;font-size:22px;line-height: 24px;\">\r\n" + 
				"          Va&scaron; korisni&#269;ki nalog je uspe&scaron;no kreiran\r\n" + 
				"        </span>\r\n" + 
				"\r\n" + 
				"                                  </td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td height=\"24\" &nbsp;=\"\"></td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td height=\"1\" bgcolor=\"#DAE1E9\"></td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td height=\"24\" &nbsp;=\"\"></td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td align=\"center\"> <span style=\"color:#48545d;font-size:14px;line-height:24px;\">\r\n" + 
				"          Na sistem se mo&#382;ete logovoti pomo&#263;u slede&#263;ih kredencijala:\r\n" + 
				"        </span>\r\n" + 
				"\r\n" + 
				"                                  </td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td height=\"20\" &nbsp;=\"\"></td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td valign=\"top\" width=\"48%\" align=\"center\"> <span>\r\n" + 
				"          <a  style=\"display:block; padding:15px 25px; background-color:#0087D1;cursor:pointer; color:#ffffff; border-radius:3px; text-decoration:none;\">Korisni&#269;ko ime:<b>"+mail+"</b><br>"+
						 " Lozinka:<b>"+pass+"</b></a>\r\n" + 
				"        </span>\r\n" + 
				"\r\n" + 
				"                                  </td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td height=\"20\" &nbsp;=\"\"></td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td align=\"center\">\r\n" + 
				"                                    <img src=\"https://s3.amazonaws.com/app-public/Coinbase-notification/hr.png\" width=\"54\"\r\n" + 
				"                                    height=\"2\" border=\"0\">\r\n" + 
				"                                  </td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td height=\"20\" &nbsp;=\"\"></td>\r\n" + 
				"                                </tr>\r\n" + 
				"                                <tr>\r\n" + 
				"                                  <td align=\"center\">\r\n" + 
				"                                    <p style=\"color:#a2a2a2; font-size:12px; line-height:17px; font-style:italic;\">Ukoliko imate bilo kakve probleme sa prijavom na sistem javite se na&scaron;em timu.\r\n" + 
				"                                      </p>\r\n" + 
				"                                  </td>\r\n" + 
				"                                </tr>\r\n" + 
				"                              </tbody>\r\n" + 
				"                            </table>\r\n" + 
				"                          </td>\r\n" + 
				"                        </tr>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td height=\"60\"></td>\r\n" + 
				"                        </tr>\r\n" + 
				"                      </tbody>\r\n" + 
				"                    </table>\r\n" + 
				"                    <!-- Main CONTENT end here -->\r\n" + 
				"                    <!-- FOOTER start here -->\r\n" + 
				"                    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n" + 
				"                      <tbody>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td height=\"10\">&nbsp;</td>\r\n" + 
				"                        </tr>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td valign=\"top\" align=\"center\"> <span style=\"font-family: -apple-system,BlinkMacSystemFont,&#39;Segoe UI&#39;,&#39;Roboto&#39;,&#39;Oxygen&#39;,&#39;Ubuntu&#39;,&#39;Cantarell&#39;,&#39;Fira Sans&#39;,&#39;Droid Sans&#39;,&#39;Helvetica Neue&#39;,sans-serif; color:#9EB0C9; font-size:10px;\">&copy;\r\n" + 
				"                            <a href=\"https://www.mksolutions.rs/\" target=\"_blank\" style=\"color:#9EB0C9 !important; text-decoration:none;\">MK IT BS</a> 2020\r\n" + 
				"                          </span>\r\n" + 
				"\r\n" + 
				"                          </td>\r\n" + 
				"                        </tr>\r\n" + 
				"                        <tr>\r\n" + 
				"                          <td height=\"50\">&nbsp;</td>\r\n" + 
				"                        </tr>\r\n" + 
				"                      </tbody>\r\n" + 
				"                    </table>\r\n" + 
				"                    <!-- FOOTER end here -->\r\n" + 
				"                  </td>\r\n" + 
				"                </tr>\r\n" + 
				"              </tbody>\r\n" + 
				"            </table>\r\n" + 
				"          </td>\r\n" + 
				"        </tr>\r\n" + 
				"      </tbody>\r\n" + 
				"    </table>\r\n" + 
				"  </body>\r\n" + 
				"\r\n" + 
				"</html>";
        
        
        
        msg.setContent(htmlMsg,"text/html");
        
        MimeMessageHelper helper = new MimeMessageHelper(msg, false, "utf-8");
        helper.setTo("vrgovicm@gmail.com"); //postaviti kasnije na mail iz zaglavlja
        helper.setFrom("af083085@gmail.com");
        helper.setSubject("ZAGA APP - kreiran korisnički nalog");
        javaMailSender.send(msg);

    }
	public MailNotification() {}
}
