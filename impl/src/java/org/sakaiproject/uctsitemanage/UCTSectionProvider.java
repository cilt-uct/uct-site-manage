package org.sakaiproject.uctsitemanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.api.common.type.Type;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.coursemanagement.api.CourseOffering;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.sitemanage.api.AffiliatedSectionProvider;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

public class UCTSectionProvider implements AffiliatedSectionProvider {

	private static final Log log = LogFactory.getLog(UCTSectionProvider.class);

	public void init() {
		log.info("init()");
	}
	
	public void destroy() {
		log.info("destroy()");
	}
	public List getAffiliatedSectionEids(String userId,
			String academicSessionEid) {
		log.debug("Geting Instructor sets for: " + userId);
		String dept = getUserDept(userId);
		log.debug("got department: " + dept);
		List ret = null;
		if (dept == null)
			return ret;
		
		if (cms.isCourseSetDefined(dept)) {
			log.debug("found courseSet: "  + dept);
			Set offerings = cms.getCourseOfferingsInCourseSet(dept);
		
		ret = new ArrayList();
		Iterator i = offerings.iterator();
		while (i.hasNext()) {
			CourseOffering co = (CourseOffering)i.next();
			Set en = cms.getSections(co.getEid());
			//we need to iterate through this list
			Iterator it = en.iterator();
			while (it.hasNext()) {
				Section sec = (Section)it.next();
				ret.add(sec.getEid());
			}
			
		}
		}
		return ret;
	}

	private SakaiPersonManager sakaiPersonManager;
	public void setSakaiPersonManager(SakaiPersonManager spm) {
		sakaiPersonManager = spm;
	}
	
	private UserDirectoryService userDirectoryService;
	public void setUserDirectoryService(UserDirectoryService uds) {
		userDirectoryService = uds;
	}
	
	private CourseManagementService cms;
	public void setCourseManagementService(CourseManagementService cm) {
		cms = cm;
	}
	
	
    /*
     * Get a users department affiliation from their system Profile
     */
    private String getUserDept(String userId) {
    	log.debug(this +" looking for department for " + userId);
    	if (sakaiPersonManager == null) {
    		log.warn("SakaipersonManager is not defined");
    		return null;
    	}
    	
    	try {
    		Type _type = sakaiPersonManager.getSystemMutableType();
    		User user = userDirectoryService.getUserByEid(userId);
    		if (user.getType().equals("staff")) {
    			SakaiPerson sakaiPerson = sakaiPersonManager.getSakaiPerson(user.getId(), _type);
    			String dept = sakaiPerson.getOrganizationalUnit();
    			if (dept != null) {	
    				log.debug(this + "found department " + dept);
    				return dept;
    			} else {
    				log.debug("user department is null!");
    				return null;
    			}
    		}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    	
    	
    	   	
    	
    }
	
}
