/**
 * Copyright (c) 2007-2020 University of Cape Town
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://opensource.org/licenses/ecl2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.uctsitemanage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.sakaiproject.api.common.edu.person.SakaiPerson;
import org.sakaiproject.api.common.edu.person.SakaiPersonManager;
import org.sakaiproject.api.common.type.Type;
import org.sakaiproject.coursemanagement.api.CourseManagementService;
import org.sakaiproject.coursemanagement.api.CourseOffering;
import org.sakaiproject.coursemanagement.api.Section;
import org.sakaiproject.sitemanage.api.AffiliatedSectionProvider;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UCTSectionProvider implements AffiliatedSectionProvider {

    @Setter private SakaiPersonManager sakaiPersonManager;
    @Setter private UserDirectoryService userDirectoryService;
    @Setter private CourseManagementService courseManagementService;

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
        List<String> ret = null;
        if (dept == null)
            return ret;
        
        if (courseManagementService.isCourseSetDefined(dept)) {
            log.debug("found courseSet: "  + dept);
            Set<CourseOffering> offerings = courseManagementService.getCourseOfferingsInCourseSet(dept);
        
	        ret = new ArrayList<String>();
	        Iterator<CourseOffering> i = offerings.iterator();
	        while (i.hasNext()) {
	            CourseOffering co = (CourseOffering)i.next();
	            Set<Section> en = courseManagementService.getSections(co.getEid());
	            //we need to iterate through this list
	            Iterator<Section> it = en.iterator();
	            while (it.hasNext()) {
	                Section sec = (Section)it.next();
	                ret.add(sec.getEid());
	            }
	            
	        }
        }
        return ret;
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
            if ("staff".equals(user.getType())) {
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
            log.warn(e.getLocalizedMessage(), e);
        }
        return null;
        
        
    
        
    }
    
}
