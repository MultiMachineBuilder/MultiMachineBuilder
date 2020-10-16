/**
 * 
 */
package mmb.addon.data;

import java.io.*;
import java.util.*;
import java.util.jar.*;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.provider.ftp.FtpFileObject;
import org.apache.commons.vfs2.provider.http4.Http4FileObject;
import org.apache.commons.vfs2.provider.http5.Http5FileObject;
import org.apache.commons.vfs2.provider.sftp.SftpFileObject;

import mmb.addon.module.AddonCentral;
import mmb.files.data.input.DataStream;

/**
 * @author oskar
 *
 */
public class AddonInfo {
	public AddonState state = AddonState.ENABLE;
	
	//In any case
	public String name = "";
	public String path = "";
		
	//If file exists
	public FileObject file = null;
	public int HTTP = 0;
	//If file is not corrupt
	public List<FileObject> files = null;
	public List<JarEntry> contents = new ArrayList<JarEntry>();
	
	//About MMB
	public ModMetadata mmbmod = null;
	public AddonCentral central = null;

	public boolean isOnline() {
		return (file instanceof Http4FileObject)||(file instanceof Http5FileObject)||(file instanceof FtpFileObject)||(file instanceof SftpFileObject);
	}
}
