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
import java.util.List;

import org.sakaiproject.sitemanage.api.SectionField;
import org.sakaiproject.sitemanage.api.SectionFieldProvider;
import org.sakaiproject.util.ResourceLoader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SectionFieldManagerImpl implements SectionFieldProvider {
    

    public List<SectionField> getRequiredFields() {
        ResourceLoader resourceLoader = new ResourceLoader("SectionFields");
        List<SectionField> fieldList = new ArrayList<SectionField>();

        fieldList.add(new SectionFieldImpl(resourceLoader.getString("required_fields_course_code"), null, 8));
        
        return fieldList;
    }

    public String getSectionEid(String academicSessionEid, List<SectionField> fields) {
        if(fields == null || fields.isEmpty()) {
            if(log.isDebugEnabled()) log.debug("Returning an empty sectionEID for an empty (or null) list of fields");
            return "";
        }
        
        String[] values = new String[fields.size()+1];
        for(int i = 0; i < fields.size(); i++) {
            SectionField sf = fields.get(i);
            values[i] = sf.getValue();
        }
        values[fields.size()] = academicSessionEid;
        
        ResourceLoader resourceLoader = new ResourceLoader("SectionFields");
        String sectionEid = resourceLoader.getFormattedMessage("section_eid", values);
        if(log.isDebugEnabled()) log.debug("Generated section eid = " + sectionEid);
        return sectionEid;
    }

    public String getSectionTitle(String eid ,List<SectionField> fields) {
        return getSectionEid(eid, fields);
        
    }
    
    public void init() {
        log.info("init()");
    }

    public void destroy() {
    }

}
