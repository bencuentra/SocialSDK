require([ "sbt/connections/FileService", "sbt/dom", "sbt/json" ], function(FileService, dom, json) {

	var fileService = new FileService();
	var fileId = "%{name=sample.communityFileId|helpSnippetId=Social_Files_Get_Community_Files}";
	var communityId = "%{name=sample.fileCommunityId|helpSnippetId=Social_Communities_Get_My_Communities}";

	fileService.updateCommunityFileMetadata({
		id : fileId,
		label : "New Label" + (new Date()).getTime(),
		summary : "New Summary",
		visibility : "public"
	}, communityId).then(function(file) {
		dom.setText("json", json.jsonBeanStringify(file));

	}, function(error) {
		dom.setText("json", json.jsonBeanStringify(error));
	});

});