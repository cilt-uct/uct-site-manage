package org.sakaiproject.uct.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.sakaiproject.api.common.edu.person.BasePhotoService;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileSystemMultiFolderPhotoService extends BasePhotoService {

	
	private String photoRepositoryPath = null;
	
	/**
	 * Setters
	 *
	 */
	
	private ServerConfigurationService serverConfigurationService;
	public void setServerConfigurationService(ServerConfigurationService scs) {
		serverConfigurationService = scs;
	}
	
	private UserDirectoryService userDirectoryService;
	/**
	 * @param userDirectoryService
	 *        The userDirectoryService to set.
	 */
	public void setUserDirectoryService(UserDirectoryService userDirectoryService)
	{
		if (log.isDebugEnabled())
		{
			log.debug("setUserDirectoryService(userDirectoryService " + userDirectoryService + ")");
		}

		this.userDirectoryService = userDirectoryService;
	}
	
	
	
	public void init() {
		photoRepositoryPath = serverConfigurationService.getString("profile.photoRepositoryPath", null);
		log.info("init() photoPath: " + photoRepositoryPath);
	}
	
	public byte[] getPhotoAsByteArray(String userId) {
		// TODO Auto-generated method stub
		log.debug("getPhotoAsByteArray(" + userId +") repo path" + this.photoRepositoryPath );
		return this.getInstitutionalPhotoFromDiskRespository(userId);
	}

	

	public void savePhoto(byte[] data, String userId) {
		this.savePhotoToDiskRepository(data, userId);
		
	}
	
	private byte[] getInstitutionalPhotoFromDiskRespository(String uid) {
		
		log.debug("fetching photo's from: " + photoRepositoryPath);
			if(photoRepositoryPath != null) {
				
				FileInputStream fileInput = null;
				
				try {
				
					String eid = userDirectoryService.getUserEid(uid);
					
					String photoPath = photoRepositoryPath+"/" + this.getFolderName(eid) + "/"  +eid+".jpg";
					
					log.debug("Get photo from disk: "+photoPath);
				
					File file = new File(photoPath);
				
					byte[] bytes = new byte[(int)file.length()];
				
		            // Open an input stream
		            fileInput = new FileInputStream (file);
					
		            // Read in the bytes
		            int offset = 0;
		            int numRead = 0;
		            while (offset < bytes.length
		                   && (numRead=fileInput.read(bytes, offset, bytes.length-offset)) >= 0) {
		                offset += numRead;
		            }
		        
		            // Ensure all the bytes have been read in
		            if (offset < bytes.length) {
		                throw new IOException("Could not completely read file :"+file.getName());
		            }
		        
		           return bytes;
		
				} catch (FileNotFoundException e) {
					// file not found, this user does not have a photo ID on file
					log.debug("FileNotFoundException: " + e);
				} catch (IOException e) {
					log.error("IOException: "+e);
				} catch (UserNotDefinedException e) {
					log.debug("UserNotDefinedException: "+e);
				} finally {
					// Close the input stream 
			        try {
			        	if(fileInput != null) fileInput.close();
					} catch (IOException e) {
						log.error("Exception in finally block: "+e);
					}
				}
			}
			return null;
	}
	
	private void savePhotoToDiskRepository(byte[] photo, String uid) {
		log.debug("savePhotoToDiskRepository for " + uid);
		if (photoRepositoryPath != null ) {
			if (photo == null || photo.length == 0)
				return;
			
			FileOutputStream fileOutput = null;
			try {
				String eid = userDirectoryService.getUserEid(uid);
				String photoPath = photoRepositoryPath+"/" + this.getFolderName(eid);
				log.debug("writing file to: " + photoPath);
				checkCreateFolder(photoPath);
				photoPath = photoPath + "/" + eid + ".jpg";
				fileOutput = new FileOutputStream(photoPath);
				fileOutput.write(photo);
				log.debug("done writing file");
			}
			catch (UserNotDefinedException e) {
				log.debug("UserNotDefinedException: "+e);
			} catch (FileNotFoundException e) {
				// file not found, this user does not have a photo ID on file
				log.debug("FileNotFoundException: "+e);
			} catch (IOException e) {
				log.error("IOException: "+e);
			} finally {
				// Close the input stream 
		        try {
		        	if(fileOutput != null) fileOutput.close();
				} catch (IOException e) {
					log.error("Exception in finally block: "+e);
				}
			}
			
			
		}
	}
	
	private String getFolderName(String ref) {
		String ret = "a";
		
		if (ref.length() >= 2)
			ret = ref.substring(0, 2);
		else if (ref.length() == 1)
			ret = ref.substring(0, 1);
		
		return ret;
		
	}
	
	private void checkCreateFolder(String path) {
		
		if (!new File(path).exists())
			new File(path).mkdir();
		
	}
	
	
	@Override
	public boolean overRidesDefault() {
		return true;
	}
}



