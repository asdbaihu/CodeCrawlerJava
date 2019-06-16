package net.codecrawler;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/api")
public class MainController {
	
	public static final String ACCOUNT_SID = "AC7112ed2f159a9ee46f84005a6a6c8bc9";
	public static final String AUTH_TOKEN = "faba32df7d808a741c6a6238ada834c5";

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PostRepository postRepository;

	@PostMapping(path="/createaccount")
	public ModelAndView createUser (@RequestParam("uname") String uname, @RequestParam("email") String email, @RequestParam("mobile") String mobile,  @RequestParam("pword") String pword, @RequestParam("confirm") String confirm) {
		
		String auth =  UUID.randomUUID().toString().replace("-", "").substring(0,10);
		
		Member newMember = new Member(uname, email, mobile, pword, auth);
		
		if (pword.equals(confirm)) {
			
			memberRepository.save(newMember);
			
			return new ModelAndView("redirect:" + "https://codecrawler.net/welcome/" + auth);
			
		} else {
			
			return new ModelAndView("redirect:" + "https://codecrawler.net/signup/guest");
		}
	}
	
	@PostMapping(path="/login")
	public ModelAndView createUser (@RequestParam("email") String email, @RequestParam("pword") String pword) {

		String auth =  UUID.randomUUID().toString().replace("-", "").substring(0,10);
		
		
		
		for (Member member : memberRepository.findByEmail(email)) {
			
			byte [] decodePass = Base64.getDecoder().decode(member.getPass());
			
			if (pword.equals(new String(decodePass))) {
				auth = member.getAuth();
				
				return new ModelAndView("redirect:" + "https://codecrawler.net/welcome/" + auth);
			}
		}
		
	    return new ModelAndView("redirect:" + "https://codecrawler.net/signup/guest");
	}
	
	@PostMapping(path="/makepost")
	public ModelAndView makePost (@RequestParam String auth, @RequestParam String username, @RequestParam String blog, @RequestParam String post) {

		String stamp = new SimpleDateFormat("MM-dd-YY", Locale.ENGLISH).format(new Date());
		
		Integer reply = 1;
		
		for (Post posts : postRepository.findByBlog(blog)) {
			
			posts.getReply();
			
			reply++;
		}
		
		Post newPost = new Post(username, blog, post, reply, stamp);
		
		postRepository.save(newPost);
		
		return new ModelAndView("redirect:" + "https://codecrawler.net/" + blog + "/" + auth);
	}		
	
	@PostMapping(path="/getpass")
	public ModelAndView getPass (@RequestParam String mobile) {
		
		String password = "";
		
		for (Member member : memberRepository.findByMobile(mobile)) {
			
			byte [] decodePass = Base64.getDecoder().decode(member.getPass());
			
			if (mobile.equals(member.getMobile())) {		
				
				password = new String(decodePass);
			}
		}
		
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		@SuppressWarnings("unused")
		Message message = Message
	            .creator(new PhoneNumber("+1" + mobile), // to
	                    new PhoneNumber("+15184187502"), // from
	                    "Your CodeCrawler password is:\n" + password)
	            .create();

		return new ModelAndView("redirect:" + "https://codecrawler.net/login/guest");
	}
}
