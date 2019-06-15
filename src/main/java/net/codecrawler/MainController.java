package net.codecrawler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/api")
public class MainController {

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PostRepository postRepository;

	@PostMapping(path="/createaccount")
	public ModelAndView  createUser ( @RequestParam("uname") String uname, @RequestParam("email") String email, @RequestParam("mobile") String mobile,  @RequestParam("pword") String pword, @RequestParam("confirm") String confirm) {
		
		String auth =  UUID.randomUUID().toString().replace("-", "").substring(0,10);
		
		Member newMember = new Member(uname, email, Integer.parseInt(mobile), pword, auth);
		
		if (pword.equals(confirm)) {
			
			memberRepository.save(newMember);
			
			return new ModelAndView("redirect:" + "https://codecrawler.net/welcome/" + auth);
			
		} else {
			
			return new ModelAndView("redirect:" + "https://codecrawler.net/signup/guest");
		}
	}
	
	@PostMapping(path="/login")
	public ModelAndView  createUser (@RequestParam("email") String email, @RequestParam("pword") String pword) {

		String auth =  UUID.randomUUID().toString().replace("-", "").substring(0,10);
		
		for (Member member : memberRepository.findByEmail(email)) {
			
			if (pword.equals(member.getPass())) {
				auth = member.getAuth();
				
				return new ModelAndView("redirect:" + "https://codecrawler.net/welcome/" + auth);
			}
		}
		
	    return new ModelAndView("redirect:" + "https://codecrawler.net/signup/guest");
	}
	
	@PostMapping(path="/makepost")
	public ModelAndView  makePost (@RequestParam String auth, @RequestParam String username, @RequestParam String blog, @RequestParam String post) {

		String stamp = new SimpleDateFormat("MM.dd.YY", Locale.ENGLISH).format(new Date());
		
		Integer reply = 1;
		
		for (Post posts : postRepository.findByBlog(blog)) {
			
			posts.getReply();
			
			reply++;
		}
		
		Post newPost = new Post(username, blog, post, reply, stamp);
		
		postRepository.save(newPost);
		
		return new ModelAndView("redirect:" + "https://codecrawler.net/" + blog + "/" + auth);
	}		
}