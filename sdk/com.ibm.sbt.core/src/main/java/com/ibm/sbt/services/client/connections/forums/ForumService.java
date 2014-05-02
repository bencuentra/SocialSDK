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
package com.ibm.sbt.services.client.connections.forums;

import static com.ibm.sbt.services.client.base.CommonConstants.APPLICATION_ATOM_XML;
import static com.ibm.sbt.services.client.base.CommonConstants.CONTENT_TYPE;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;
import static com.ibm.sbt.services.client.base.ConnectionsConstants.v4_5;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.StatusLine;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.AuthType;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.ConnectionsService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.forums.transformers.BaseForumTransformer;
import com.ibm.sbt.services.endpoints.Endpoint;


/**
 * Forum model object
 * 
 * @author Manish Kataria 
 * @author Swati Singh
 * @author Carlos Manias
 */

public class ForumService extends ConnectionsService {

	private static final long serialVersionUID = -4926901916081556236L;

	/**
	 * Used in constructing REST APIs
	 */
	private static final String FORUM_UNIQUE_IDENTIFIER = "forumUuid";
	private static final String TOPIC_UNIQUE_IDENTIFIER = "topicUuid";
	private static final String REPLY_UNIQUE_IDENTIFIER = "replyUuid";
	private static final String POST_UNIQUE_IDENTIFIER  = "postUuid"; 
	private static final String COMM_UNIQUE_IDENTIFIER	= "communityUuid";
	public static final String CREATE_OP 				= "create";
	/**
	 * Default Constructor
	 */

	public ForumService() {
		this(DEFAULT_ENDPOINT_NAME);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ForumService Object with the specified endpoint
	 */
	public ForumService(String endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Constructor
	 * 
	 * @param endpoint
	 *            Creates ForumService Object with the specified endpoint
	 */
	public ForumService(Endpoint endpoint) {
		super(endpoint, DEFAULT_CACHE_SIZE);
	}

	/**
	 * Return mapping key for this service
	 */
	@Override
	public String getServiceMappingKey() {
		return "forums";
	}

	@Override
	public NamedUrlPart getAuthType(){
		String auth = super.getAuthType().getValue();
		auth = AuthType.BASIC.get().equalsIgnoreCase(auth)?"":auth;
		return new NamedUrlPart("authType", auth);
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<Forum> getForumFeedHandler() {
		return new AtomFeedHandler<Forum>(this) {
			@Override
			protected Forum entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Forum(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<ForumTopic> getForumTopicFeedHandler() {
		return new AtomFeedHandler<ForumTopic>(this) {
			@Override
			protected ForumTopic entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ForumTopic(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<ForumReply> getForumReplyFeedHandler() {
		return new AtomFeedHandler<ForumReply>(this) {
			@Override
			protected ForumReply entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new ForumReply(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<Recommendation> getRecommendationFeedHandler() {
		return new AtomFeedHandler<Recommendation>(this) {
			@Override
			protected Recommendation entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Recommendation(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public IFeedHandler<Tag> getTagFeedHandler() {
		return new AtomFeedHandler<Tag>(this) {
			@Override
			protected Tag entityInstance(BaseService service, Node node, XPathExpression xpath) {
				return new Tag(service, node, nameSpaceCtx, xpath);
			}
		};
	}

	/** This method returns the all forums
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getAllForums() throws ClientServicesException{
		return getAllForums(null);

	}

	/**
	 * This method returns the all forums
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getAllForums(Map<String, String> parameters) throws ClientServicesException {
		String allForumsUrl = ForumUrls.FORUMS.format(this);
		return getForumEntityList(allForumsUrl, parameters);
	}

	/**
	 * This method returns the public forums
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getPublicForums() throws ClientServicesException{
		return getPublicForums(null);

	}

	/**
	 * This method returns the public forums
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getPublicForums(Map<String, String> parameters) throws ClientServicesException {
		String publicForumsUrl = ForumUrls.FORUMS_PUBLIC.format(this);
		return getForumEntityList(publicForumsUrl, parameters);
	}


	/**
	 * This method returns the my forums
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getMyForums() throws ClientServicesException {
		return getMyForums(null);
	}

	/**
	 * This method returns the public forums
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<Forum> getMyForums(Map<String, String> parameters) throws ClientServicesException {
		String myForumsUrl = ForumUrls.FORUMS_MY.format(this);
		return getForumEntityList(myForumsUrl, parameters);
	}

	/**
	 * This method returns the tags that have been assigned to forums
	 * 
	 * @return TagList
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getForumsTags() throws ClientServicesException {
		String tagsUrl = ForumUrls.TAGS_FORUMS.format(this);
		return getTagEntityList(tagsUrl, null);
	}

	/**
	 * This method returns the tags that have been assigned to forum topics
	 * 
	 * @method getForumTopicsTags
	 * 
	 * @param forumUuid
	 * @return TagList
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getForumTopicsTags(String forumUuid) throws ClientServicesException {
		return getForumTopicsTags(forumUuid, null);
	}

	/**
	 * This method returns the tags that have been assigned to forum topics
	 * 
	 * @method getForumTopicsTags
	 *  
	 * @param forumUuid
	 * @param parameters
	 * @return TagList
	 * @throws ClientServicesException
	 */
	public EntityList<Tag> getForumTopicsTags(String forumUuid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
		String tagsUrl = ForumUrls.TAGS_TOPICS.format(this);
		return getTagEntityList(tagsUrl, null);
	}
	
	/**
	 * Wrapper method to get list of recommendations for a ForumTopic or ForumReply
	 * API Supported on Connections 4.5 or above
	 * <p>
	 * User should be authenticated to call this method
	 * @param postUuid
	 * @return RecommendationList
	 * @throws ClientServicesException
	 */
	public EntityList<Recommendation> getRecommendations(String postUuid) throws ClientServicesException{
		checkVersion();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
		return getRecommendationEntityList(recommendationsUrl, parameters);
	}

	/**
	 * Wrapper method to create a recommendation, API Supported on Connections 4.5 or above
	 * <p>
	 * User should be authenticated to call this method
	 * @param postUuid
	 * @return Recommendation
	 * @throws ClientServicesException
	 */
	public Recommendation createRecommendation(String postUuid) throws ClientServicesException{
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		Response result;
		Recommendation recommendation;
		// not using transformer, as the payload to be sent is constant
		String payload = "<entry xmlns='http://www.w3.org/2005/Atom'><category scheme='http://www.ibm.com/xmlns/prod/sn/type' term='recommendation'></category></entry>";
		try {
			result = createData(recommendationsUrl, parameters, null,payload);
			recommendation = getRecommendationFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
		return recommendation;

	}

	/**
	 * Wrapper method to delete a recommendation, API Supported on Connections 4.5 or above
	 * <p>
	 * User should be authenticated to call this method
	 * @param postUuid
	 * @return boolean
	 * @throws ClientServicesException
	 */
	public boolean deleteRecommendation(String postUuid) throws ClientServicesException{
		checkVersion();
		String recommendationsUrl = ForumUrls.RECOMMENDATION_ENTRIES.format(this);
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(POST_UNIQUE_IDENTIFIER, postUuid);
		try {
			deleteData(recommendationsUrl, parameters, postUuid);
			return true;
		} catch (Exception e) {
			throw new ClientServicesException(e);
		}
	}

	// todo : missing overloaded method accepting params for getForum

	/**
	 * Wrapper method to get a Stand-alone forum
	 * <p>
	 * fetches forum content from server and populates the data member of {@link Community} with the fetched content 
	 *
	 * @param forumUuid
	 *			   id of forum
	 * @return A Forum
	 * @throws ClientServicesException
	 */
	public Forum getForum(String forumUuid) throws ClientServicesException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
		String url = ForumUrls.FORUM.format(this);
		return getForumEntity(url, parameters);
	}

	/**
	 * Wrapper method to create a forum
	 * <p>
	 * User should be authenticated to call this method
	 * 
	 * 
	 * @param Forum
	 * @return Forum
	 * @throws ClientServicesException
	 */
	public Forum createForum(Forum forum) throws ClientServicesException {
		if (null == forum){
			throw new ClientServicesException(null,"null forum");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(forum);
			Object 	payload = transformer.transform(forum.getFieldsMap());

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");

			String url = ForumUrls.FORUMS.format(this);
			result = createData(url, null, headers, payload);
			forum = getForumFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum");
		}

		return forum;
	}

	/**
	 * Wrapper method to update a forum
	 * <p>
	 * User should be authenticated as owner of the forum to call this method
	 * 
	 * @param Forum
	 * @return Response
	 * @throws ClientServicesException
	 */
	public Response updateForum(Forum forum) throws ClientServicesException {
		if (null == forum){
			throw new ClientServicesException(null,"null forum");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(FORUM_UNIQUE_IDENTIFIER, forum.getUid());
			if(forum.getFieldsMap().get(ForumsXPath.title)== null)
				forum.setTitle(forum.getTitle());
			if(forum.getFieldsMap().get(ForumsXPath.content)== null)
				forum.setContent(forum.getContent());
			if(!forum.getFieldsMap().toString().contains(ForumsXPath.tags.toString()))
				forum.setTags(forum.getTags());
			
			BaseForumTransformer transformer = new BaseForumTransformer(forum);
			Object payload = transformer.transform(forum.getFieldsMap());

			String url = ForumUrls.FORUM.format(this);

			return updateData(url, parameters, payload, FORUM_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ClientServicesException(e, "error updating forum");
		}

	}

	/**
	 * Wrapper method to delete a forum
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param Forum
	 * 				forum which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForum(Forum forum) throws ClientServicesException {
		removeForum(forum.getUid());
	}

	/**
	 * Wrapper method to delete a forum
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param String
	 * 				forumUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeForum(String forumUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(forumUuid)){
			throw new ClientServicesException(null, "null forum id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUuid);
			String deleteForumUrl = ForumUrls.FORUM.format(this);

			Response response = super.deleteData(deleteForumUrl, parameters, FORUM_UNIQUE_IDENTIFIER);
			if (!isForumDeleted(response)){
				throw new ClientServicesException(new Exception(),"error deleting forum, received HTTP Status code "+response.getResponse().getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			throw new ClientServicesException(e,"error deleting forum");
		} 	

	}
	
	protected boolean isForumDeleted(Response response){
		StatusLine statusLine = response.getResponse().getStatusLine();
		return statusLine.getStatusCode() == 204;
	}

	/**
	 * This method returns the public topics
	 * 
	 * @return TopicList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getPublicForumTopics() throws ClientServicesException {
		return getPublicForumTopics(null);
	}

	/**
	 * This method returns the public topics
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getPublicForumTopics(Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.TOPICS.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * This method returns the my topics
	 * 
	 * @return TopicList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getMyForumTopics() throws ClientServicesException {
		return getMyForumTopics(null);
	}

	/**
	 * This method returns the my topics
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getMyForumTopics(Map<String, String> parameters) throws ClientServicesException {
		String myTopicsUrl = ForumUrls.TOPICS_MY.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * This method returns the topics for a particular forum
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getForumTopics(String forumUid) throws ClientServicesException {
		return getForumTopics(forumUid, null);
	}


	/**
	 * This method returns the topics for a particular forum
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumTopic> getForumTopics(String forumUid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(FORUM_UNIQUE_IDENTIFIER, forumUid);
		String myTopicsUrl = ForumUrls.TOPICS.format(this);
		return getForumTopicEntityList(myTopicsUrl, parameters);
	}

	/**
	 * This method returns topic
	 * 
	 * @param parameters
	 * @return TopicList
	 * @throws ClientServicesException
	 */
	public ForumTopic getForumTopic(String topicId) throws ClientServicesException {
		return getForumTopic(topicId, null);
	}

	/**
	 * This method returns topic
	 * 
	 * @param parameters
	 * @return
	 * @throws ClientServicesException
	 */
	public ForumTopic getForumTopic(String topicId, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicId);
		String myTopicsUrl = ForumUrls.TOPIC.format(this);
		return getForumTopicEntity(myTopicsUrl, parameters);
	}

	/**
	 * Wrapper method to create a Topic
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumTopic
	 * @return Topic
	 * @throws ClientServicesException
	 */
	public ForumTopic createForumTopic(ForumTopic topic) throws ClientServicesException {
		return createForumTopic(topic,topic.getForumUuid());
	}

	/**
	 * Wrapper method to create a Topic
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumTopic
	 * @return Topic
	 * @throws ClientServicesException
	 */
	public ForumTopic createForumTopic(ForumTopic topic,String forumId) throws ClientServicesException {
		if (null == topic){
			throw new ClientServicesException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> params = new HashMap<String, String>();
			params.put(FORUM_UNIQUE_IDENTIFIER, forumId);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);

			String url = ForumUrls.TOPICS.format(this);
			result = createData(url, params, headers,payload);
			topic = getForumTopicFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum");
		}

		return topic;
	}

	/**
	 * Wrapper method to create a Topic for default Forum of a Community
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumTopic
	 * @return Topic
	 * @throws ClientServicesException
	 */
	public ForumTopic createCommunityForumTopic(ForumTopic topic,String communityId) throws ClientServicesException {
		if (null == topic){
			throw new ClientServicesException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> params = new HashMap<String, String>();
			params.put(COMM_UNIQUE_IDENTIFIER, communityId);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);
			String postUrl = ForumUrls.TOPICS.format(this);
			result = createData(postUrl, params, headers,payload);
			topic = getForumTopicFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum");
		}

		return topic;
	}

	/**
	 * Wrapper method to update a topic
	 * <p>
	 * User should be authenticated as owner of the forum to call this method
	 * 
	 * @param ForumTopic
	 * @throws ClientServicesException
	 */
	public void updateForumTopic(ForumTopic topic) throws ClientServicesException {
		if (null == topic || StringUtil.isEmpty(topic.getUid())) {
			throw new ClientServicesException(null,"Topic object passed was null");
		}
		try {
			String url = ForumUrls.TOPICS.format(this);
			if(topic.getFieldsMap().get(ForumsXPath.title)== null)
				topic.setTitle(topic.getTitle());
			if(topic.getFieldsMap().get(ForumsXPath.content)== null)
				topic.setContent(topic.getContent());
			if(!topic.getFieldsMap().toString().contains(ForumsXPath.tags.toString()))
				topic.setTags(topic.getTags());
			
			BaseForumTransformer transformer = new BaseForumTransformer(topic);

			Object payload = transformer.transform(topic.getFieldsMap());

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(TOPIC_UNIQUE_IDENTIFIER, topic.getUid());

			Map<String, String> headers = new HashMap<String, String>();
			headers.put(CONTENT_TYPE, APPLICATION_ATOM_XML);

			getClientService().put(url, parameters,headers, payload,ClientService.FORMAT_NULL);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ClientServicesException(e, "error updating topic");
		}

	}

	/**
	 * Wrapper method to delete a topic
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param Forum
	 * 				forum which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForumTopic(ForumTopic topic) throws ClientServicesException {
		removeForum(topic.getUid());
	}

	/**
	 * Wrapper method to delete a topic
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param String
	 * 				topicUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeForumTopic(String topicUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(topicUuid)){
			throw new ClientServicesException(null, "null topic id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicUuid);
			String deleteTopicUrl = ForumUrls.TOPIC.format(this);
			super.deleteData(deleteTopicUrl, parameters, TOPIC_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ClientServicesException(e,"error deleting forum");
		} 	
	}

	private EntityList<ForumReply> getReplies(Map<String, String> parameters) throws ClientServicesException {
		if (parameters != null){
			if(StringUtil.isEmpty(parameters.get(TOPIC_UNIQUE_IDENTIFIER)) && 
					StringUtil.isEmpty(parameters.get(REPLY_UNIQUE_IDENTIFIER))){
					throw new ClientServicesException(null, "null post Uuid");
			}
		} else {
			parameters = new HashMap<String, String>();
		}
		String myRepliesUrl = ForumUrls.REPLIES.format(this);
		return getForumReplyEntityList(myRepliesUrl, parameters);
	}

	 /**
     * Get a list for forum replies that includes the replies in the specified post.
     * The post uuid must be specified in the parametetrs as either:
     * topicUuid or replyUuid 
     * 
	 * @param parameters
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplies(Map<String, String> parameters) throws ClientServicesException {
		return getReplies(parameters);
	}
	/**
     * Get a list for forum replies that includes the replies of a Forum Topic.
     * 
     * @param topicUuid
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumTopicReplies(String topicUuid) throws ClientServicesException {
		return getForumTopicReplies(topicUuid, null);
	}
	 /**
     * Get a list for forum replies that includes the replies of a Forum Topic.
     * 
     * @param topicUuid
	 * @param parameters
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumTopicReplies(String topicUuid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(TOPIC_UNIQUE_IDENTIFIER, topicUuid);
		return getReplies(parameters);
	}

	/**
     * Get a list for forum replies that includes the replies of a Forum Reply.
     * 
     * @param topicUuid
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplyReplies(String replyUuid) throws ClientServicesException {
		return getForumReplyReplies(replyUuid, null);
	}
	/**
     * Get a list for forum replies that includes the replies of a Forum Reply.
     * 
     * @param replyUuid
	 * @param parameters
	 * @return ReplyList
	 * @throws ClientServicesException
	 */
	public EntityList<ForumReply> getForumReplyReplies(String replyUuid, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		parameters.put(REPLY_UNIQUE_IDENTIFIER, replyUuid);
		return getReplies(parameters);
	}

	/**
	 * This method returns reply
	 * 
	 * @param replyId
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply getForumReply(String replyId) throws ClientServicesException {
		return getForumReply(replyId, null);
	}

	/**
	 * This method returns reply
	 * @param replyId
	 * @param parameters
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply getForumReply(String replyId, Map<String, String> parameters) throws ClientServicesException {
		parameters = getParameters(parameters);
		String myRepliesUrl = ForumUrls.REPLY.format(this);
		parameters.put(REPLY_UNIQUE_IDENTIFIER, replyId);
		return getForumReplyEntity(myRepliesUrl, parameters);
	}

	/**
	 * Wrapper method to create a Reply
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumReply
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply createForumReply(ForumReply reply) throws ClientServicesException {
		return createForumReply(reply, reply.getTopicUuid());
	}

	/**
	 * Wrapper method to create a Reply
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumReply
	 * @param topicId
	 * @return ForumReply
	 * @throws ClientServicesException
	 */
	public ForumReply createForumReply(ForumReply reply,String topicId) throws ClientServicesException {
		if (null == reply || StringUtil.isEmpty(topicId)){
			throw new ClientServicesException(null,"Reply object passed was null");
		}
		Response result = null;
		try {
			if(StringUtil.isEmpty(reply.getTopicUuid())){
				reply.setTopicUuid(topicId);
			}
			BaseForumTransformer transformer = new BaseForumTransformer(reply, CREATE_OP);
			Object 	payload = transformer.transform(reply.getFieldsMap());
			Map<String, String> params = new HashMap<String, String>();

			params.put(TOPIC_UNIQUE_IDENTIFIER, topicId);

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String url = ForumUrls.REPLIES.format(this);
			result = createData(url, params, headers,payload);
			reply = getForumReplyFeedHandler().createEntity(result);

		} catch (Exception e) {
			throw new ClientServicesException(e, "error creating forum reply");
		}

		return reply;
	}

	/**
	 * Wrapper method to update a Reply
	 * <p>
	 * User should be authenticated to call this method
	 * @param ForumReply
	 * @return void
	 * @throws ClientServicesException
	 */
	public void updateForumReply(ForumReply reply) throws ClientServicesException {
		if (null == reply){
			throw new ClientServicesException(null,"Reply object passed was null");
		}
		try {
			
			if(reply.getFieldsMap().get(ForumsXPath.title)== null)
				reply.setTitle(reply.getTitle());
			if(reply.getFieldsMap().get(ForumsXPath.content)== null)
				reply.setContent(reply.getContent());
			if(!reply.getFieldsMap().toString().contains(ForumsXPath.tags.toString()))
				reply.setTags(reply.getTags());
			
			BaseForumTransformer transformer = new BaseForumTransformer(reply);
			Object 	payload = transformer.transform(reply.getFieldsMap());
			Map<String, String> params = new HashMap<String, String>();
			params.put(REPLY_UNIQUE_IDENTIFIER, reply.getUid());

			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			String url = ForumUrls.REPLY.format(this);
			updateData(url, params, headers,payload, reply.getUid());

		} catch (Exception e) {
			throw new ClientServicesException(e, "error updating forum reply");
		}

	}

	/**
	 * Wrapper method to delete a forum reply
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param ForumReply
	 * 				reply which is to be deleted
	 * @throws CommunityServiceException
	 */
	public void removeForumReply(ForumReply reply) throws ClientServicesException {
		removeForum(reply.getUid());
	}

	/**
	 * Wrapper method to delete a forum reply
	 * <p>
	 * User should be logged in as a owner of the forum to call this method.
	 * 
	 * @param String
	 * 				replyUuid which is to be deleted
	 * @throws ClientServicesException
	 */
	public void removeForumReply(String replyUuid) throws ClientServicesException {
		if (StringUtil.isEmpty(replyUuid)){
			throw new ClientServicesException(null, "null reply id");
		}

		try {
			Map<String, String> parameters = new HashMap<String, String>();

			parameters.put(REPLY_UNIQUE_IDENTIFIER, replyUuid);
			String deleteReplyUrl = ForumUrls.REPLY.format(this);

			super.deleteData(deleteReplyUrl, parameters, REPLY_UNIQUE_IDENTIFIER);
		} catch (Exception e) {
			throw new ClientServicesException(e,"error deleting forum reply");
		} 	

	}

	protected void checkVersion() throws ClientServicesException{
		if (!getApiVersion().isAtLeast(v4_5)){
			UnsupportedOperationException ex = new UnsupportedOperationException("This API is only supported on connections 4.5 or above");
			throw new ClientServicesException(ex);
		}
	}

	protected Forum getForumEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (Forum)getEntity(requestUrl, parameters, getForumFeedHandler());
	}

	protected ForumTopic getForumTopicEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (ForumTopic)getEntity(requestUrl, parameters, getForumTopicFeedHandler());
	}

	protected ForumReply getForumReplyEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (ForumReply)getEntity(requestUrl, parameters, getForumReplyFeedHandler());
	}

	protected Recommendation getRecommendationEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return (Recommendation)getEntity(requestUrl, parameters, getRecommendationFeedHandler());
	}

	protected EntityList<Forum> getForumEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumFeedHandler());
	}

	protected EntityList<ForumTopic> getForumTopicEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumTopicFeedHandler());
	}

	protected EntityList<ForumReply> getForumReplyEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getForumReplyFeedHandler());
	}

	protected EntityList<Recommendation> getRecommendationEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getRecommendationFeedHandler());
	}

	protected EntityList<Tag> getTagEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
		return getEntities(requestUrl, parameters, getTagFeedHandler());
	}
}
