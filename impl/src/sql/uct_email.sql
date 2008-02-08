
#delete from EMAIL_TEMPLATE_ITEM;

insert into EMAIL_TEMPLATE_ITEM(template_Key,subject,message, last_modified) values ('sitemange.notifyAddedParticipant','${productionSiteName} Site Notification',
'Dear ${userName},

You have been added to the following ${localSakaiName} site:
${siteName}
by ${currentUserName}.

To go to this site, login to ${localSakaiName} at ${localSakaiUrl} with your username (${userName}) and password.

You can then access the site by clicking on the site name, which appears as a tab in a row across the top part of the page, or selecting the site from the drop-down list on the right.

If you cannot login to ${localSakaiName}, please see http://vula.uct.ac.za/password/ for details on how to reset your password.

If you have any further questions about ${localSakaiName} or how to access this site, please feel free to contact the ${localSakaiName} helpdesk by replying to this email or emailing help@vula.uct.ac.za.

Online help is also available by clicking on the Help link in any page.

Regards
The Vula Team, University of Cape Town', now());


insert into EMAIL_TEMPLATE_ITEM(template_Key,subject, message, last_modified) values ('sitemanage.notifyNewUserEmail','${productionSiteName} New User Notification',
'Dear ${userName},

You have been added to ${localSakaiName} (${localSakaiUrl}) by ${currentUserName} (${currentUserEmail}).

Your username is: ${userName}
Your password is: ${newPassword}

Once you have logged in, you may change your password by clicking on Account in My Workspace. You may also add your full name and other profile information by clicking on Profile.

If you have any questions about Vula, please feel free to contact the Vula helpdesk by replying to this email or emailing help@vula.uct.ac.za.

Online help is also available by clicking on the Help link on any page.

Regards
The Vula team, University of Cape Town', now());
