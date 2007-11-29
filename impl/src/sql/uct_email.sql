 create table UCT_EMAIL (
 id serial,
     templateKey varchar(255),
     template text,
     subject text,
     Index  (templateKey)
     );
insert into UCT_EMAIL(templateKey,subject, template) values ('notifyNewUserEmail','$productionSiteName Site Notification',
'$userName,

You have been added to the $localSakaiName ($localSakaiUrl) by $currentUserName.

Your username is: $userName
Your password is
$newPassword

You can later go to the Account tool in your My Workspace site to reset it.');


insert into UCT_EMAIL(templateKey,subject,template) values ('notifyAddedParticipant','$productionSiteName New User Notification',
'Dear $userName,

You have been added to the following $localSakaiName site:
$siteName
by $currentUserName.

To log in:
1. In your internet browser open $localSakaiName: $localSakaiUrl
2. At the top of the page type your username and password, and then click ''Login''.
3. You can now access your site either by clicking on the Site Name, which appears as a tab in a row across the upper part of your screen.
');



insert into EMAIL_TEMPLATE_ITEM(template_Key,subject,message, last_modified) values ('sitemange.notifyAddedParticipant','$productionSiteName New User Notification',
'Dear $userName,

You have been added to the following $localSakaiName site:
$siteName
by $currentUserName.

To log in:
1. In your internet browser open $localSakaiName: $localSakaiUrl
2. At the top of the page type your username and password, and then click ''Login''.
3. You can now access your site either by clicking on the Site Name, which appears as a tab in a row across the upper part of your screen.
', now());


insert into EMAIL_TEMPLATE_ITEM(templateKey,subject,template, last_modified) values ('sitemanage.notifyAddedParticipant','$productionSiteName New User Notification',
'Dear $userName,

You have been added to the following $localSakaiName site:
$siteName
by $currentUserName.

To log in:
1. In your internet browser open $localSakaiName: $localSakaiUrl
2. At the top of the page type your username and password, and then click ''Login''.
3. You can now access your site either by clicking on the Site Name, which appears as a tab in a row across the upper part of your screen.
',now());

