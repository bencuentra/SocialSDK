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
package com.ibm.sbt.test.js.connections.activities.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseActivitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.activities.Activity;
import com.ibm.sbt.services.client.connections.common.Member;

public class UpdateMember extends BaseActivitiesTest {

	static final String SNIPPET_ID = "Social_Activities_API_UpdateMember";

	Activity activity;
	String id;
	Member member;

	@Before
	public void init() throws ClientServicesException {
		activity = createActivity();
		id = getProperty("sample.id2");
		if (environment.isSmartCloud()) {
			id = getProperty("smartcloud.id2");
		}
		member = addMember(activity.getActivityUuid(), id);
	}

	@After
	public void destroy() throws ClientServicesException {
		deleteActivity(activity.getActivityUuid());
	}

	@Ignore
	public void testUpdateActivityMember() {
		addSnippetParam("sample.activityId", activity.getActivityUuid());
		addSnippetParam("sample.memberId", member.getId());
		addSnippetParam("sample.userId2", member.getId());
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		Assert.assertEquals(200, json.getAsInt("status")); // OK
	}
}
