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
package com.ibm.sbt.test.js.connections.communities.api;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @since 25 Mar 2013
 */
public class GetCommunityEvents extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_GetCommunityEvents";

    public GetCommunityEvents() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetCommunityEvents() {
    	Date now = new Date();
    	Date later = new Date(now.getTime() + 360000);
    	//createEvent(community, "Event One", now, later);
    	//createEvent(community, "Event Two", now, later);
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.startDate", "2012-1-1");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        //List jsonList = previewPage.getJsonList();
        //assertCommunityValid((JsonJavaObject)jsonList.get(0));
        //Assert.assertEquals(community.getCommunityUuid(), ((JsonJavaObject)jsonList.get(1)).getString("entityId"));
    }
    
    @Test
    public void testGetCommunityEventsError() {
        addSnippetParam("CommunityService.communityUuid", "Foo");
        addSnippetParam("sample.startDate", "2012-1-1");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
    }
    
    @Test
    public void testGetCommunityEventsArg() {
        addSnippetParam("CommunityService.communityUuid", "");
        addSnippetParam("sample.startDate", "2012-1-1");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Invalid argument, expected communityUuid.", json.getString("message"));
    }
    
}
