/*
 * Copyright IBM Corp. 2013
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
 
dojo.provide("sbt.connections.controls.forums.ForumView");

/**
 * 
 */
define([ "../../../declare", "../../../lang", "../../../stringUtil", "../../../log",
         "./ForumGrid", "../../../controls/view/BaseView" ,"./StartTopicAction", "./DeleteTopicAction"],
		function(declare, lang, stringUtil, log, ForumGrid, BaseView, StartTopicAction, DeleteTopicAction) {

	/*
	 * @module sbt.connections.forums.ForumView
	 */
	var ForumView = declare([ BaseView ], {

		title : "Forum Topics", 
    
		iconClass : "lotusIcon iconsComponentsBlue24 iconsComponentsBlue24-ForumsBlue24",
    	
		defaultActions : true,
		
		defaultGrid : true,
		
		grid : null,
		

		postMixInProperties : function() {
			this.inherited(arguments);
		},

		/**
		 * Post create function is called after widget has been created.
		 * 
		 * @method - postCreate
		 */
		postCreate : function() {
			this.inherited(arguments);
			
			if (!this.grid && this.defaultGrid) {
				var gridArgs = (this.type) ? {type : this.type} : {};
				gridArgs = lang.mixin(gridArgs, this.gridArgs || {});

				this.grid = new ForumGrid(gridArgs);
				this.setContent(this.grid);
			}
			
			if (this.grid && this.defaultActions) {
				this.addAction(new StartTopicAction({grid : this.grid}));
				this.addAction(new DeleteTopicAction({grid : this.grid}));
			}
		}
	
		//
		// Internals
		//

	});

	return ForumView;
});