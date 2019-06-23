package net.codecrawler;

import java.util.ArrayList;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

@RestController
@RequestMapping("/api")
public class RequestController {

	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	PostRepository postRepository;
	
	private Gson g = new Gson();

	@RequestMapping(path = "member/{auth}")
	public String login (@PathVariable String auth) {		
		
		for (Member member : memberRepository.findByAuth(auth)) {

			String json = g.toJson(member);
			
			return json;
		}
		
		return "guest";
	}
	
	@RequestMapping(path = "posts/{blog}")
	@ResponseBody
	public String findPosts (@PathVariable String blog) {		
		
		ArrayList<Post> postList = new ArrayList<Post>();
		
		for (Post post : postRepository.findByBlog(blog)) {

			postList.add(post);
		}
		
		Comparator<Post> c = (s1, s2) -> s2.getId().compareTo(s1.getId());
		postList.sort(c);
		
		String json = g.toJson(postList);
		
		return json;
	}
}

