package org.sakaiproject.uctsitemanage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.email.api.EmailService;
import org.sakaiproject.sitemanage.api.UserNotificationProvider;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.util.ResourceLoader;




public class UserNotificationProviderImpl implements UserNotificationProvider {
	
	private static Log M_log = LogFactory.getLog(UserNotificationProviderImpl.class);
	private EmailService emailService; 
	
	public void setEmailService(EmailService es) {
		emailService = es;
	}
	
	private ServerConfigurationService serverConfigurationService;
	public void setServerConfigurationService(ServerConfigurationService scs) {
		serverConfigurationService = scs;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		userDirectoryService = uds;
	}
	
	private SqlService sqlService;
	public void setSqlService(SqlService ss) {
		sqlService = ss;
	}
	
	
	/** portlet configuration parameter values* */
	/** Resource bundle using current language locale */
	private static ResourceLoader rb = new ResourceLoader("UserNotificationProvider");

	public void init() {
		//nothing realy to do
		M_log.info("init()");
	}
	
	public void notifyAddedParticipant(boolean newNonOfficialAccount,
			User user, String siteTitle) {
		
		String from = getSetupRequestEmailAddress();
		//we need to get the template
		

		EmailTemplate template = this.getTemplate("notifyAddedParticipant");
		if (template == null)
			return;
		if (from != null) {
			String productionSiteName = serverConfigurationService.getString(
					"ui.service", "");
			String productionSiteUrl = serverConfigurationService
					.getPortalUrl();
			String nonOfficialAccountUrl = serverConfigurationService.getString(
					"nonOfficialAccount.url", null);
			String emailId = user.getEmail();
			String to = emailId;
			String headerTo = emailId;
			String replyTo = emailId;
			Map<String, String> rv = new HashMap<String, String>();
			rv.put("productionSiteName", productionSiteName);
			String message_subject = TextTemplateLogicUtils.processTextTemplate(template.getSubject(), rv);   
			String content = "";
			/*
			 * $userName
			 * $localSakaiName
			 * $currentUserName
			 * $localSakaiUrl
			 */
			 Map<String, String> replacementValues = new HashMap<String, String>();
	            replacementValues.put("userName", user.getDisplayName());
	            replacementValues.put("localSakaiName",serverConfigurationService.getString(
	    				"ui.service", ""));
	            replacementValues.put("currentUserName",userDirectoryService.getCurrentUser().getDisplayName());
	            replacementValues.put("localSakaiUrl", serverConfigurationService.getPortalUrl());
	            replacementValues.put("siteName", siteTitle);
	            
	            		
			content = TextTemplateLogicUtils.processTextTemplate(template.getBody(), replacementValues);
			emailService.send(from, to, message_subject, content, headerTo,
					replyTo, null);

		} // if

	}

	public void notifyNewUserEmail(User user, String newUserPassword,
			String siteTitle) {
		
		
		String from = getSetupRequestEmailAddress();
		String productionSiteName = serverConfigurationService.getString(
				"ui.service", "");
		String productionSiteUrl = serverConfigurationService.getPortalUrl();
		
		String newUserEmail = user.getEmail();
		String to = newUserEmail;
		String headerTo = newUserEmail;
		String replyTo = newUserEmail;
		
		EmailTemplate template = this.getTemplate("notifyNewUserEmai");
		Map<String, String> rv = new HashMap<String, String>();
		rv.put("productionSiteName", productionSiteName);
		String message_subject = TextTemplateLogicUtils.processTextTemplate(template.getSubject(), rv); 
		String content = "";

		
		if (template == null)
			return;
		
		if (from != null && newUserEmail != null) {
			/*
			 * $userName
			 * $localSakaiName
			 * $currentUserName
			 * $localSakaiUrl
			 */
			 Map<String, String> replacementValues = new HashMap<String, String>();
	            replacementValues.put("userName", user.getDisplayName());
	            replacementValues.put("localSakaiName",serverConfigurationService.getString(
	    				"ui.service", ""));
	            replacementValues.put("currentUserName",userDirectoryService.getCurrentUser().getDisplayName());
	            replacementValues.put("localSakaiUrl", serverConfigurationService.getPortalUrl());
	            replacementValues.put("newPassword",newUserPassword);
	            replacementValues.put("siteName", siteTitle);
	            		
			content = TextTemplateLogicUtils.processTextTemplate(template.getBody(), replacementValues);
			emailService.send(from, to, message_subject, content, headerTo,
					replyTo, null);
		}
	}

	/*
	 *  Private methods
	 */
	
	private String getSetupRequestEmailAddress() {
		String from = serverConfigurationService.getString("setup.request",
				null);
		if (from == null) {
			M_log.warn(this + " - no 'setup.request' in configuration");
			from = "postmaster@".concat(serverConfigurationService
					.getServerName());
		}
		return from;
	}
	
	private EmailTemplate getTemplate(String templateKey) {
		
		Object[] fields = new Object[]{templateKey};
		String sql = "SELECT subject,template from UCT_EMAIL where TEMPLATEKEY= ?";
		List results = sqlService.dbRead(sql, fields, new SqlReader(){
			public Object readSqlResultRecord(ResultSet result) {
				EmailTemplate rv = null;
				try {
					 
					rv.setSubject(result.getString(1));
					rv.setBody(result.getString(2));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return rv;
			}
			
		});
		
		if (results.size()==0) {
			M_log.error("No template found for: " + templateKey);
			return null;
		} 
		return (EmailTemplate)results.get(0);
		
	}
	
	private class EmailTemplate {
		
		private String id;
		private String subject;
		private String body;
		
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		
		
	}
}
