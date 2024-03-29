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
	
	public static final String ACCOUNT_SID = "";
	public static final String AUTH_TOKEN = "";

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
	public ModelAndView loginUser (@RequestParam("email") String email, @RequestParam("pword") String pword) {		
		
		
		for (Member member : memberRepository.findByEmail(email)) {
			
			byte [] decodePass = Base64.getDecoder().decode(member.getPass());
			
			if (pword.equals(new String(decodePass))) {
				
				return new ModelAndView("redirect:" + "https://codecrawler.net/welcome/" + member.getAuth());
			}
		}
		
	    return new ModelAndView("redirect:" + "https://codecrawler.net/signup/guest");
	}
	
	@PostMapping(path="/logout")
	public ModelAndView createUser (@RequestParam String auth) {

		String newAuth =  UUID.randomUUID().toString().replace("-", "").substring(0,10);
		
		for (Member member : memberRepository.findByAuth(auth)) {

			if (member.getAuth().equals(auth)) {
				
				byte [] decodePass = Base64.getDecoder().decode(member.getPass());
				
				memberRepository.save(new Member(member.getUsername(), member.getEmail(), member.getMobile(),
						new String(decodePass), newAuth, member.getId()));
			
				return new ModelAndView("redirect:" + "https://codecrawler.net/welcome/guest");
			}
			
		}
		
	    return new ModelAndView("redirect:" + "https://codecrawler.net/signup/guest");
	}
	
	@PostMapping(path="/getpass")
	public ModelAndView forgotPass (@RequestParam String mobile) {
		
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
		
		return new ModelAndView("redirect:" + "https://codecrawler.net/" + blog + "/" + auth + "#comments");
	}		
	
	@PostMapping(path="/deletepost")
	public ModelAndView deletePost (@RequestParam String blog, @RequestParam String comment, @RequestParam String uname, @RequestParam String auth) {
		
		for (Post post : postRepository.findByUsername(uname)) {

			if (post.getPost().equals(comment)) {

				postRepository.delete(post);
			}
		}
		
		return new ModelAndView("redirect:" + "https://codecrawler.net/" + blog + "/" + auth + "#comments");
	}
	
	@PostMapping(path="/update")
	public ModelAndView updateUser (@RequestParam("uname") String uname, @RequestParam("email") String email, @RequestParam("mobile") String mobile,  @RequestParam("pword") String pword, @RequestParam("confirm") String confirm, @RequestParam String auth) {
		
		for (Member member : memberRepository.findByAuth(auth)) {

			if (member.getAuth().equals(auth) && pword.equals(confirm)) {
								
			
				for (Post post : postRepository.findAll()) {

					if (post.getUsername().equals(member.getUsername())) {
						
						postRepository.save(new Post(uname, post.getBlog(), post.getPost(), post.getReply(), post.getStamp(), post.getId()));
					}
				}
				
				memberRepository.save(new Member(uname, email, mobile, pword, auth, member.getId()));

				return new ModelAndView("redirect:" + "https://codecrawler.net/settings/" + auth);
			}
		}
		
	    return new ModelAndView("redirect:" + "https://codecrawler.net/signup/guest");
	}
	
	@PostMapping(path="/deleteaccount")
	public ModelAndView deleteUser (@RequestParam String auth) {
		
		for (Member member : memberRepository.findByAuth(auth)) {

			if (member.getAuth().equals(auth)) {

				memberRepository.delete(member);
			}
		}
		
		return new ModelAndView("redirect:" + "https://codecrawler.net/welcome/guest");
	}

}
