/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.sbt.services.client.connections.blogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author Swati Singh
 *
 */
public class BlogCommentCreateGetDeleteTest extends BaseBlogServiceTest {

	@Before
	public void createCommentInit() throws ClientServicesException {
		comment = createBlogComment();
	}

	@Test
	public void CreateComment() throws ClientServicesException {
		Comment commentReturned = blogService.createBlogComment(comment, blog.getHandle(), blogPost.getPostUuid());
		assertNotNull(commentReturned.getTitle());
		assertEquals(unRandomize(comment.getTitle()), unRandomize(commentReturned.getTitle()));
		deleteBlogComment(commentReturned);
	}
	
	@Test
	public void GetComment() throws ClientServicesException {
		Comment commentGot = blogService.getBlogComment(blog.getHandle(), comment.getCommentUuid());
		
		assertEquals(unRandomize(comment.getTitle()), unRandomize(commentGot.getTitle()));
		assertEquals(unRandomize(comment.getContent()), unRandomize(commentGot.getContent()));
		deleteBlogComment(comment);
	}

	@Test
	public void deleteComment() throws Exception {
		blogService.deleteBlogComment(blog.getHandle(), comment.getCommentUuid());
	}

	@After
	public void cleanUp() throws ClientServicesException {
		deleteBlogPost(blogPost);
		deleteBlog(blog);
	}
}
