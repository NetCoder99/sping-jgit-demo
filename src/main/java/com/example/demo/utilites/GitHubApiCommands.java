package com.example.demo.utilites;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.example.demo.models.GitHubRepoProps;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@PropertySource("classpath:secrets.properties")
public class GitHubApiCommands {
	static Logger logger = LoggerFactory.getLogger(GitHubApiCommands.class);
	//	curl -u NetCoder99:?????? https://api.github.com/user/repos -d '{"name":"test","private":true}' 
	//	curl -u NetCoder99:?????? -X DELETE https://api.github.com/repos/NetCoder99/testfs
	//  curl -u NetCoder99:?????? https://api.github.com/user/repos

	private static String TOKEN;
	@Value( "${github.token}" )
	public void setToken(String token){
		GitHubApiCommands.TOKEN = token;
	}

	private static String USER;
	@Value("${github.user}")
	public void setUser(String user){
		GitHubApiCommands.USER = user;
	}

	private static final String baseUrl = "https://api.github.com";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	//curl -u NetCoder99:$TOKEN \
	//     -H "Accept: application/vnd.github.v3+json" \
	//     https://api.github.com/repos/NetCoder99Org/test1    
	// --------------------------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	public static GitHubRepoProps getRemoteRepo(String orgName, String repoName) throws Exception {
		// curl -u NetCoder99:?????? https://api.github.com/user/repos -d '{"name":"test","private":true}' 
		String apiUrl      = baseUrl + "/repos/" + orgName + "/" + repoName;

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl))
				.setHeader("Authorization", getAuthToken())
				.build();
		
		HttpResponse response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
		JsonNode jsonNode     = objectMapper.readTree(response.body().toString());
		JsonNode apiErr       = jsonNode.get("errors");
		if (apiErr != null) {
			String apiMsg = jsonNode.get("message").textValue();
			String errMsg = apiErr.get(0).get("message").textValue();
			throw new Exception(String.format("%s : %s", apiMsg, errMsg));
		}
		else {
			JsonNode repoId = jsonNode.get("id");
			if (repoId != null) {
				return extractRepoDetails(jsonNode);
			}
			else {
				if (jsonNode.get("message") != null && jsonNode.get("message").textValue().toLowerCase().contains("not found")) {
					return null;
				}
			}
			String excpMsg = String.format("Unexpected repsonse from Github check for remote repository: %s.%s", orgName, repoName);
			throw new Exception(excpMsg);
		}
	}


	// --------------------------------------------------------------------------------------------------
	@SuppressWarnings("rawtypes")
	public static GitHubRepoProps createRemoteRepo(String orgName, String repoName) throws Exception {
		logger.info("++++ Creating remote repository: {}:{}:{}", baseUrl, orgName, repoName);
		//curl -u NetCoder99:$TOKEN -X POST https://api.github.com/orgs/NetCoder99Org/repos -d '{"name":"test1","description":"This is your first repository"}'		
		String rqstBody    = String.format("{\"name\":\"%s\" , \"private\":true , \"description\":\"Demo Repo create from code\"}", repoName);
		String apiUrl      = baseUrl + "/orgs/" + orgName + "/repos";

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl))
				.setHeader("Authorization", getAuthToken())
				.POST(BodyPublishers.ofString(rqstBody))
				.build();
		
		HttpResponse response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
		JsonNode jsonNode = objectMapper.readTree(response.body().toString());
		JsonNode apiErr = jsonNode.get("errors");
		if (apiErr != null) {
			String apiMsg = jsonNode.get("message").textValue();
			String errMsg = apiErr.get(0).get("message").textValue();
			throw new Exception(String.format("%s : %s", apiMsg, errMsg));
		}
		else {
			return extractRepoDetails(jsonNode);
		}
	}

//	// --------------------------------------------------------------------------------------------------
//	@SuppressWarnings("rawtypes")
//	public static GitHubRepoProps createRemoteRepo(String repoName) throws Exception {
//		//    	curl -u NetCoder99:?????? https://api.github.com/user/repos -d '{"name":"test","private":true}' 
//		String userPass    = USER + ":" + TOKEN;
//		String basicAuth   = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
//		String rqstBody    = String.format("{\"name\":\"%s\",\"private\":true}", repoName);
//		String apiUrl      = baseUrl + "/user/repos";
//		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl))
//				.setHeader("Authorization", basicAuth)
//				.POST(BodyPublishers.ofString(rqstBody))
//				.build();
//		HttpResponse response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
//		JsonNode jsonNode = objectMapper.readTree(response.body().toString());
//		JsonNode apiErr = jsonNode.get("errors");
//		if (apiErr != null) {
//			String apiMsg = jsonNode.get("message").textValue();
//			String errMsg = apiErr.get(0).get("message").textValue();
//			throw new Exception(String.format("%s : %s", apiMsg, errMsg));
//		}
//		else {
//			return extractRepoDetails(jsonNode);
//		}
//	}

    // --------------------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
	public static int deleteRemoteRepo(String repoName) throws Exception {
//    	curl -X DELETE -u NetCoder99:?????? https://api.github.com/repos/NetCoder99/test2    	
    	String userPass    = USER + ":" + TOKEN;
    	String basicAuth   = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
    	String apiUrl      = baseUrl + "/repos/NetCoder99/" + repoName;
    	HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl))
                .setHeader("Authorization", basicAuth)
                .DELETE()
                .build();
        HttpResponse response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        return response.statusCode();
    }

    // --------------------------------------------------------------------------------------------------
    @SuppressWarnings("rawtypes")
	public static List<GitHubRepoProps> getRepoNames(String path) throws Exception {
    	String userPass = USER + ":" + TOKEN;
    	String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userPass.getBytes()));
    	HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + "/user/repos" + "?per_page=100"))
                .setHeader("Authorization", basicAuth)
                .GET()
                .build();
    	HttpResponse response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
        JsonNode jNode = objectMapper.readTree(response.body().toString());
        List<GitHubRepoProps> repoNames =  extractRepoNames(jNode);
        return repoNames;
    }

    // --------------------------------------------------------------------------------------------------
    private static List<GitHubRepoProps> extractRepoNames(JsonNode jsonNode) {
    	List<GitHubRepoProps> rtnList = new ArrayList<>();
        if (jsonNode.isArray()) {
            for (JsonNode jsonArrNode : jsonNode) {
                rtnList.add(extractRepoDetails(jsonArrNode));
            }
        } 
        return rtnList;
    }

    // --------------------------------------------------------------------------------------------------
    private static GitHubRepoProps extractRepoDetails(JsonNode jsonNode) {
        GitHubRepoProps tmpProps = new GitHubRepoProps();
        tmpProps.setRepoId(jsonNode.get("id").asText());	
        tmpProps.setRepoName(jsonNode.get("name").textValue());	
        tmpProps.setCreateDate(cnvrtDateTime(jsonNode.get("created_at").textValue()));	
        tmpProps.setUpdateDate(cnvrtDateTime(jsonNode.get("updated_at").textValue()));	
        tmpProps.setPushDate(cnvrtDateTime(jsonNode.get("pushed_at").textValue()));
        
        tmpProps.setHtmlUrl(jsonNode.get("html_url").asText());
        tmpProps.setApiUrl(jsonNode.get("url").asText());
        tmpProps.setBranchesUrl(jsonNode.get("branches_url").asText());
    	
        return tmpProps;
    }
    
    // --------------------------------------------------------------------------------------------------
    private static LocalDateTime cnvrtDateTime(String dateStr) {
    	Instant instant = Instant.parse(dateStr);
    	return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
    
    // --------------------------------------------------------------------------------------------------
    private static String getAuthToken() {
		return "Basic " + new String(Base64.getEncoder().encode((USER + ":" + TOKEN).getBytes()));
    }

}
