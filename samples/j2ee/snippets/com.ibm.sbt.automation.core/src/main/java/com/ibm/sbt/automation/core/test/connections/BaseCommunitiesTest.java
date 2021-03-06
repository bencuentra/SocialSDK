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
package com.ibm.sbt.automation.core.test.connections;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.FlexibleTest;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.Invite;
import com.ibm.sbt.services.client.connections.communities.Member;



/**
 * @author mwallace
 *  
 * @since 13 Mar 2013
 */
public class BaseCommunitiesTest extends FlexibleTest {
    
    protected boolean createCommunity = true;
    protected CommunityService communityService;
    protected Community community;
    protected Member member;
    
    protected String CommunityEventEntry =
    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		    "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\">" +
			      "<title xmlns:atom=\"http://www.w3.org/2005/Atom\" type=\"text\">{0}</title>" +
			      "<snx:startDate xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">{1}</snx:startDate>" +
			      "<snx:endDate xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">{2}</snx:endDate>" +
		    "</entry>";    		

    public BaseCommunitiesTest() {
        setAuthType(BaseApiTest.AuthType.AUTO_DETECT);
    }
    
    @Before
    public void createCommunity() {
    	//FIXME: Test is Broken
        	/*String type = "public";
        	if (getEnvironment().isSmartCloud()) {
        		type = "private";
        	}
        	String name = createCommunityName();
        	//System.out.println(name);
            community = createCommunity(name, type, name, "tag1,tag2,tag3");
            try {
				member = community.getMembers().get(0);
			} catch (ClientServicesException e) {
				e.printStackTrace();
			}*/
    }
    
	public String getProperty(String name) {
	    return getEnvironment().getProperty(name);
	}

	@After
    public void deleteCommunityAndQuit() {
    	deleteCommunity(community);
    	community = null;
    	
    	if (getEnvironment().isDebugTransport()) {
    		//saveTestDataAndResults();
    	}
    }
    
    public void deleteCommunity() {
    	deleteCommunity(community);
    	community = null;
    }
    
    public Community getCommunity() {
    	return community;
    }
    
    protected boolean createEvent(Community community, String title, Date start, Date end) {
    	try {
    		TimeZone tz = TimeZone.getTimeZone("UTC");
    		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
    		df.setTimeZone(tz);
    		
    		String entry = MessageFormat.format(CommunityEventEntry, title, df.format(start), df.format(end));
    		System.out.println(entry);
    		CommunityService communityService = getCommunityService();
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("communityUuid", community.getCommunityUuid());
    		Response response = communityService.createData("/communities/calendar/atom/calendar/event", params, entry);
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		return statusCode == 200;
    	} catch (Exception e) {
    		Assert.fail("Error creating event caused by: "+e.getMessage());
    	}
    	return false;
    }
    
    protected Invite createInvite(Community community, String userid) {
    	try {
    		Invite invite = new Invite(communityService);
    		invite.setCommunityUuid(community.getCommunityUuid());
    		invite.setUserid(userid);
    		return communityService.createInvite(invite);
    	} catch (ClientServicesException cse) {
    		fail("Error creating invite",cse);
    	}
    	return null;
    }
    
    protected String createCommunityName() {
    	return this.getClass().getName() + "#" + this.hashCode() + " Community - " + System.currentTimeMillis();
    }
    
    protected CommunityService getCommunityService() {
        if (communityService == null) {
            communityService = new CommunityService(getEnvironment().getEndpointName());
        }
        return communityService;
    }
    
    protected void assertCommunityValid(JsonJavaObject json) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(community.getCommunityUuid(), json.getString("getCommunityUuid"));
        Assert.assertEquals(community.getTitle(), json.getString("getTitle"));
        Assert.assertEquals(community.getSummary(), json.getString("getSummary"));
        Assert.assertEquals(community.getContent(), json.getString("getContent"));
        Assert.assertEquals(community.getCommunityUrl(), json.getString("getCommunityUrl"));
        Assert.assertEquals(community.getLogoUrl(), json.getString("getLogoUrl"));
        Assert.assertEquals(community.getMemberCount(), json.getInt("getMemberCount"));
        Assert.assertEquals(community.getCommunityType(), json.getString("getCommunityType"));
        Assert.assertEquals(community.getAuthor().getName(), json.getJsonObject("getAuthor").getString("name"));
        Assert.assertEquals(community.getAuthor().getEmail(), json.getJsonObject("getAuthor").getString("email"));
        Assert.assertEquals(community.getAuthor().getUserid(), json.getJsonObject("getAuthor").getString("userid"));
        Assert.assertEquals(community.getContributor().getName(), json.getJsonObject("getContributor").getString("name"));
        Assert.assertEquals(community.getContributor().getEmail(), json.getJsonObject("getContributor").getString("email"));
        Assert.assertEquals(community.getContributor().getUserid(), json.getJsonObject("getContributor").getString("userid"));
    }
    
    protected void assertMemberValid(JsonJavaObject json, String communityUuid, String name, String userid, String email, String role) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(communityUuid, json.getString("getCommunityUuid"));
        Assert.assertEquals(name, json.getString("getName"));
        Assert.assertEquals(userid, json.getString("getUserid"));
        if (!getEnvironment().isSmartCloud()) {
        	Assert.assertTrue("Expect match "+email+" <> "+json.getString("getEmail"), email.equalsIgnoreCase(json.getString("getEmail")));
        }
        Assert.assertEquals(role, json.getString("getRole"));
    }
    
    protected void assertInviteValid(JsonJavaObject json, Invite invite) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(invite.getCommunityUuid(), json.getString("getCommunityUuid"));
        //Assert.assertEquals(invite.getTitle(), json.getString("getTitle"));
        //Assert.assertEquals(invite.getContributor().getName(), json.getAsObject("getCommunityUuid").getAsString("name"));
        //Assert.assertEquals(invite.getContributor().getUserid(), json.getAsObject("getCommunityUuid").getAsString("userid"));
    }
    
    protected Community getLastCreatedCommunity() {
        Community community = null;
        try {
            CommunityService communityService = getCommunityService();
            Collection<Community> communities = communityService.getMyCommunities();
            community = communities.iterator().next();
            Trace.log("Last created community: "+community.getCommunityUuid());
            Trace.log("Last created community: "+community.getPublished());
            Iterator<Community> i = communities.iterator();
            while (i.hasNext()) {
            	Community c= i.next();
            	Trace.log("Last created community: "+c.getCommunityUuid());
            	Trace.log("Last created community: "+c.getTitle());
            	Trace.log("Last created community: "+c.getPublished());
            }
        } catch (ClientServicesException cse) {
            fail("Error getting last created community", cse);
        } 
        
        return community;
    }
    
    protected Community getCommunity(String communityUuid) {
        return getCommunity(communityUuid, true);
    }
    
    protected Community getCommunity(String communityUuid, boolean failOnCse) {
        Community community = null;
        try {
          
            
            CommunityService communityService = getCommunityService();
            community = communityService.getCommunity(communityUuid);
            Trace.log("Got community: "+community.getCommunityUuid());
        } catch (ClientServicesException cse) {
        	if (failOnCse) {
        		fail("Error retrieving community", cse);
        	}
        } 
        return community;
    }
    
    protected Community createCommunity(String title, String type, String content, String tags) {
    	return createCommunity(title, type, content, tags, true);
    }
    
    protected Community createCommunity(String title, String type, String content, String tags, boolean retry) {
        Community community = null;
        try {
            CommunityService communityService = getCommunityService();
            
        	long start = System.currentTimeMillis();
            community = new Community(communityService, "");
            community.setTitle(title);
            community.setCommunityType(type);
            community.setContent(content);
            community.setTags(tags);
            String communityUuid = communityService.createCommunity(community);
            community = communityService.getCommunity(communityUuid);
            
            long duration = System.currentTimeMillis() - start;
            Trace.log("Created test community: "+communityUuid + " took "+duration+"(ms)");
        }
        catch (ClientServicesException cse) {
        	// TODO remove this when we upgrade the QSI
        	Throwable t = cse.getCause();
        	if (t instanceof ClientServicesException) {
        		ClientServicesException csex = (ClientServicesException)t;
        		int statusCode = csex.getResponseStatusCode();
        		if (statusCode == 500 && retry) {
        			return createCommunity(title + " (retry)", type, content, tags, false);
        		}
        	}
            fail("Error creating test community with title: '"+title+"'", cse);
        } 
        
        return community;
    }

    protected void deleteCommunity(Community community) {
        if (community != null) {
            try {
            
                CommunityService communityService = getCommunityService();
                communityService.deleteCommunity(community.getCommunityUuid());
            } catch (ClientServicesException cse) {
                community = null;
            	// check if community delete failed because
            	// community was already deleted
            	Throwable t = cse.getCause();
            	if (t instanceof ClientServicesException) {
            		ClientServicesException csex = (ClientServicesException)t;
            		int statusCode = csex.getResponseStatusCode();
            		if (statusCode == 404) {
            			Trace.log(this.getClass().getName() + " attempt to delete already deleted Community: " + csex.getLocalizedMessage());
            			return;
            		}
            	}
                fail("Error deleting community "+community, cse);
            }
        }
    }
    
    protected void deleteCommunity(String communityId) {
        if (communityId != null) {
            try {
            	
                CommunityService communityService = getCommunityService();
                communityService.deleteCommunity(communityId);
            } catch (ClientServicesException cse) {
                fail("Error deleting community "+communityId, cse);
            }
        }
    }
    
    protected void addMember(Community community, String id, String role) throws Exception {
        CommunityService communityService = getCommunityService();
        Member member = new Member(communityService, id);
        member.setRole(role);
        communityService.addMember(community.getCommunityUuid(), member);
    }

    protected boolean hasMember(Community community, String id) throws Exception {
        CommunityService communityService = getCommunityService();
        EntityList<Member> memberList = communityService.getMembers(community.getCommunityUuid());
        for (int i=0; i<memberList.size(); i++) {
        	Member member = (Member)memberList.get(i);
            if (id.equals(member.getEmail()) || id.equals(member.getUserid())) {
                return true;
            }
        }
        return false;
    }   
    
    protected void fail(String message, ClientServicesException cse) {
    	String failure = message;
    	
    	Throwable cause = cse.getCause();
    	if (cause != null) {
    		cause.printStackTrace();
    		failure += ", " + cause.getMessage();
    	} else {
    		cse.printStackTrace();
    		failure += ", " + cse.getMessage();
    	}
    	
    	Assert.fail(failure);
    }

}