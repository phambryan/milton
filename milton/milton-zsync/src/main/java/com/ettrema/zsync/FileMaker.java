/* FileMaker.java

FileMaker: File reading and making class
Copyright (C) 2011 Tomáš Hlavnička <hlavntom@fel.cvut.cz>

This file is a part of Jazsync.

Jazsync is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2 of the License, or (at
your option) any later version.

Jazsync is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with Jazsync; if not, write to the

Free Software Foundation, Inc.,
59 Temple Place, Suite 330,
Boston, MA  02111-1307
USA
 */
package com.ettrema.zsync;

import com.bradmcevoy.http.Range;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;






import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is normally used to create a copy of a remote file, where a local copy is
 * already present which might contain some or all of the remote data.
 * 
 * FileMaker implements an algorithm similar to rsync which optimises the amount
 * of data downloaded by matching blocks of data in the local file against the 
 * remote and only downloading blocks which differ.
 * 
 * The given local file is not updated by this class, instead a copy is produced
 * as a temp file, and it is up to the calling code to replace the old local file
 * with the new one.
 * 
 * Most of the code in this package was taken from the jazsync project, please see
 * the header comments for license details.
 * 
 * @author Tomáš Hlavnička, modified by Brad McEvoy
 */
public class FileMaker {

	private static final Logger log = LoggerFactory.getLogger(FileMaker.class);
	private MapMatcher mapMatcher = new MapMatcher();
	private FileUpdater fileUpdater = new FileUpdater();
	private FileDownloader fileDownloader = new FileDownloader();

	public FileMaker() {
	}

	/**
	 * 
	 * @param inputFile - the "local" file, containing data which needs to be merged
	 * with that on the server
	 * @param metafile - meta file, containing headers and checksums of the "remote", generated by
	 * MetaFileMaker
	 * @param rangeLoader - accessor object, to get the binary data of the "remote"
	 * file
	 */
	public File make(File inputFile, File metafile, RangeLoader rangeLoader) throws IOException {
		MetaFileReader mfr = new MetaFileReader(metafile);
		return make(mfr, inputFile, rangeLoader);
	}

	private File make(MetaFileReader mfr, File inputFile, RangeLoader rangeLoader) throws IOException {
		MakeContext makeContext = new MakeContext(mfr.getHashtable(), new long[mfr.getBlockCount()]);
		Arrays.fill(makeContext.fileMap, -1);
		double complete = mapMatcher.mapMatcher(inputFile, mfr, makeContext);
		File dest = null;
		dest = File.createTempFile("zsyncM_", "_" + inputFile.getName());
		if (complete == 0) {
			log.info("local file has no corresponding blocks, so download whole file");
			fileDownloader.downloadWholeFile(rangeLoader, dest);
		} else {
			fileUpdater.update(inputFile, mfr, rangeLoader, makeContext, dest);
		}
		return dest;
	}

	/**
	 * Determine what ranges need to be provided to sync the file. 
	 * 
	 * @param mfr
	 * @param inputFile
	 * @param rangeLoader
	 * @param dryRun
	 * @return - null indicates everything is needed, ie whole file
	 * @throws Exception 
	 */
	public List<Range> findMissingRanges(File inputFile, File metafile) throws Exception {
		MetaFileReader mfr = new MetaFileReader(metafile);
		MakeContext mc = new MakeContext(mfr.getHashtable(), new long[mfr.getBlockCount()]);
		Arrays.fill(mc.fileMap, -1);
		double complete = mapMatcher.mapMatcher(inputFile, mfr, mc);
		if( complete == 0 ) {
			return null;
		}
		List<Range> ranges = new ArrayList<Range>();
		int blocksize = mfr.getBlocksize();
		for (int i=0; i < mc.fileMap.length; i++) {
			if (mc.fileMap[i] == -1) {
				ranges.add(new Range(i * blocksize,(i * blocksize) + blocksize));
			}
		}
		return ranges;		
	}
//
//	public void make(File fLocal, MetaData metaData, LocalFileRangeLoader rangeLoader) {
//		MetaFileReader mfr = new MetaFileReader(metaData);
//	}
}