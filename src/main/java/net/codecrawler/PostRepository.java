package net.codecrawler;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PostRepository extends CrudRepository<Post, Long> {
	
	List<Post> findByUsername(String username);
	List<Post> findByBlog(String blog);
}