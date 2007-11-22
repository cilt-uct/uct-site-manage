 create table UCT_EMAIL (
 id serial,
     templateKey varchar(255),
     template text,
     Index  (templateKey)
     );
insert into UCT_EMAIL(templateKey,template) values ('notifyNewUserEmail',
' $userName:

You have been added to $localSakaiName ($localSakaiUrl) by $currentUserName.

Your password is
$newPassword

You can later go to the Account tool in your My Workspace site to reset it.');


insert into UCT_EMAIL(templateKey,template) values ('notifyAddedParticipant',
'$userName:

You have been added to the following LocalSakaiName site:
$siteName
by $currentUserName.

To log in:
1. Open LocalSakaiName: $localSakaiUrl
2. Click the Login button.
3. Type your username and password, and click Login.
4. Go to the site, click on the site tab. (You will see two or more tabs in a row across the upper part of the screen.');